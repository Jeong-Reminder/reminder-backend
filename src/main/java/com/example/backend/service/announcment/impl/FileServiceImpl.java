package com.example.backend.service.announcment.impl;

import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.announcement.File;
import com.example.backend.model.repository.announcement.FileRepository;
import com.example.backend.service.announcment.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    @Value("${file.dir}")
    private String uploadDir;


    @Override
    public Long saveFile(MultipartFile file, Announcement announcement) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String sanitizedFilename = sanitizeFilename(originalFilename);
        String newFilename = UUID.randomUUID().toString() + "_" + sanitizedFilename;
        Path filePath = Paths.get(uploadDir).resolve(newFilename).normalize();

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        File uploadedFile = File.builder()
                .originalFilename(originalFilename)
                .filePath(filePath.toString())
                .fileType(file.getContentType())
                .announcement(announcement)
                .build();

        File savedFile = fileRepository.save(uploadedFile);
        return savedFile.getId();
    }

    @Override
    public void deleteFileById(Long fileId) {
        File file = getFile(fileId);
        fileRepository.delete(file);
        try {
            Files.deleteIfExists(Paths.get(file.getFilePath()));
        } catch (IOException e) {
            throw new RuntimeException("파일 삭제 중 오류가 발생했습니다: " + fileId, e);
        }
    }

    @Override
    public File getFile(Long id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 ID로 파일을 찾을 수 없습니다: " + id));
    }

    @Override
    public ResponseEntity<byte[]> downloadFile(Long id) {
        try {
            File file = getFile(id);
            Path filePath = Paths.get(file.getFilePath()).normalize();
            byte[] fileContent = Files.readAllBytes(filePath);

            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentDispositionFormData("attachment", URLEncoder.encode(file.getOriginalFilename(), "UTF-8").replace("+", "%20"));

            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);

        } catch (IOException e) {
            throw new RuntimeException("파일 다운로드 중 오류가 발생했습니다.", e);
        }
    }

    private String sanitizeFilename(String filename) {
        return filename.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
