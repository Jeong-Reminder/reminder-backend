package com.example.backend.service.announcment.impl;

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
        String originalFilename = file.getOriginalFilename();
        String fileType = file.getContentType();
        String newFilename = UUID.randomUUID().toString() + "_" + originalFilename;
        Path filePath = Paths.get(uploadDir, newFilename);

        Files.copy(file.getInputStream(), filePath);

        File uploadedFile = File.builder()
                .originalFilename(originalFilename)
                .filePath(filePath.toString())
                .fileType(fileType)
                .build();

        File savedFile = fileRepository.save(uploadedFile);
        return savedFile.getId();
    }

    @Override
    public List<File> uploadFiles(List<MultipartFile> files) {
        return files.stream().map(file -> {
            try {
                String originalFilename = file.getOriginalFilename();
                String fileType = file.getContentType();
                String newFilename = UUID.randomUUID().toString() + "_" + originalFilename;
                Path filePath = Paths.get(uploadDir, newFilename);

                Files.copy(file.getInputStream(), filePath);

                File uploadedFile = File.builder()
                        .originalFilename(originalFilename)
                        .filePath(filePath.toString())
                        .fileType(fileType)
                        .build();

                return fileRepository.save(uploadedFile);
            } catch (IOException e) {
                throw new RuntimeException("Error uploading file: " + file.getOriginalFilename(), e);
            }
        }).collect(Collectors.toList());
    }

    @Override
    public File getFile(Long id) {
        return fileRepository.findById(id).orElseThrow(() -> new RuntimeException("File not found"));
    }

    @Override
    public ResponseEntity<Resource> downloadFile(Long id) {
        try {
            File file = fileRepository.findById(id).orElseThrow(() -> new RuntimeException("파일을 찾지 못했습니다."));
            Path filePath = Paths.get(file.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                String originalFilename = file.getOriginalFilename();
                String encodedFilename = URLEncoder.encode(originalFilename, "UTF-8").replace("+", "%20");

                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFilename + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (IOException e) {
            throw new RuntimeException("다운로드 중 오류가 발생하였습니다.", e);
        }
    }
}
