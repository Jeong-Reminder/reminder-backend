package com.example.backend.service.announcment.impl;

import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.announcement.File;
import com.example.backend.model.repository.announcement.FileRepository;
import com.example.backend.service.announcment.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final Environment env;

    @Value("${file.dir}")
    private String uploadDir;

    @Override
    public Long saveFile(MultipartFile file, Announcement announcement) throws IOException {
        String fileHash = generateFileHash(file);
        File existingFile = fileRepository.findByFileHash(fileHash);

        if (existingFile != null) {
            return existingFile.getId();
        }

        String originalFilename = sanitizeFilename(Objects.requireNonNull(file.getOriginalFilename()));
        Path filePath = Paths.get(uploadDir).resolve(originalFilename).normalize();
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        File uploadedFile = File.builder()
                .originalFilename(originalFilename)
                .filePath(filePath.toString())
                .fileType(file.getContentType())
                .fileHash(fileHash)
                .announcement(announcement)
                .build();

        File savedFile = fileRepository.save(uploadedFile);

        String serverHost = env.getProperty("server.address", "localhost");
        String serverPort = env.getProperty("server.port", "9000");
        String fileUrl = "http://" + serverHost + ":" + serverPort + "/api/v1/files/download/" + savedFile.getId();

        savedFile.setFileUrl(fileUrl);
        fileRepository.save(savedFile);

        return savedFile.getId();
    }

    @Override
    public File getFile(Long id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File with ID " + id + " not found"));
    }

    @Override
    public byte[] getFileData(Long id) throws IOException {
        File file = getFile(id);
        Path filePath = Paths.get(file.getFilePath()).normalize();
        return Files.readAllBytes(filePath);
    }

    @Override
    @Transactional
    public void deleteFile(Long id) {
        File file = getFile(id);
        Path filePath = Paths.get(file.getFilePath()).normalize();

        try {
            Files.deleteIfExists(filePath);
            fileRepository.delete(file);
        } catch (IOException e) {
            throw new RuntimeException("파일 시스템에서 파일을 삭제하는 데 실패했습니다", e);
        } catch (Exception e) {
            throw new RuntimeException("데이터베이스에서 파일을 삭제하는 데 실패했습니다", e);
        }
    }

    @Override
    public void deleteFileData(Long id) {
        File file = getFile(id);
        Path filePath = Paths.get(file.getFilePath()).normalize();

        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("파일 시스템에서 파일을 삭제하는 데 실패했습니다: " + filePath, e);
        }
    }

    private String sanitizeFilename(String filename) {
        String name = filename.replaceAll("[^a-zA-Z0-9._-]", "_");
        String extension = "";
        int extensionIndex = filename.lastIndexOf('.');
        if (extensionIndex > 0) {
            name = filename.substring(0, extensionIndex);
            extension = filename.substring(extensionIndex);
        }
        return name + extension;
    }

    private String generateFileHash(MultipartFile file) throws IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] fileBytes = file.getBytes();
            byte[] hashBytes = digest.digest(fileBytes);
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hash algorithm not found", e);
        }
    }
}
