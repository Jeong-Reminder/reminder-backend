package com.example.backend.service.announcment.impl;

import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.announcement.File;
import com.example.backend.model.repository.announcement.FileRepository;
import com.example.backend.service.announcment.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    @Value("${file.dir}")
    private String uploadDir;

    @Override
    public Long saveFile(MultipartFile file) throws IOException {
        return saveFile(file, null);
    }

    @Override
    public Long saveFile(MultipartFile file, Announcement announcement) throws IOException {
        String originalFilename = sanitizeFilename(file.getOriginalFilename());
        String newFilename = UUID.randomUUID().toString() + "_" + originalFilename;
        Path filePath = Paths.get(uploadDir).resolve(newFilename).normalize();

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        File uploadedFile = File.builder()
                .originalFilename(originalFilename)
                .filePath(filePath.toString())
                .fileType(file.getContentType())
                .announcement(announcement)
                .build();

        File savedFile = fileRepository.save(uploadedFile);
        savedFile.setSavedPath(buildDownloadUrl(savedFile.getId()));
        fileRepository.save(savedFile);

        return savedFile.getId();
    }

    @Override
    public List<File> uploadFiles(List<MultipartFile> files) {
        return files.stream().map(file -> {
            try {
                return getFileById(saveFile(file));
            } catch (IOException e) {
                throw new RuntimeException("Error uploading file: " + file.getOriginalFilename(), e);
            }
        }).collect(Collectors.toList());
    }

    @Override
    public File getFile(Long id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found with id " + id));
    }

    @Override
    public ResponseEntity<Resource> downloadFile(Long id) {
        try {
            File file = getFile(id);
            Path filePath = Paths.get(file.getFilePath()).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            String encodedFilename = URLEncoder.encode(file.getOriginalFilename(), "UTF-8").replace("+", "%20");
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFilename + "\"")
                    .body(resource);

        } catch (IOException e) {
            throw new RuntimeException("Error occurred while downloading the file.", e);
        }
    }

    private String sanitizeFilename(String filename) {
        return filename.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    private String buildDownloadUrl(Long fileId) {
        return "http://localhost:9000/api/v1/files/download/" + fileId;
    }

    private File getFileById(Long fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found with id " + fileId));
    }
}
