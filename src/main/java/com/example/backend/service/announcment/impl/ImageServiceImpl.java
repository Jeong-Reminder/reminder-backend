package com.example.backend.service.announcment.impl;

import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.announcement.Image;
import com.example.backend.model.repository.announcement.ImageRepository;
import com.example.backend.service.announcment.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final Environment env;

    @Value("${image.dir}")
    private String uploadDir;

    @Override
    public Long saveImage(MultipartFile image, Announcement announcement) throws IOException {
        String originalFilename = sanitizeFilename(image.getOriginalFilename());
        String newFilename = UUID.randomUUID().toString() + "_" + originalFilename;
        Path imagePath = Paths.get(uploadDir).resolve(newFilename).normalize();

        Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

        Image uploadedImage = Image.builder()
                .originalFilename(originalFilename)
                .filePath(imagePath.toString())
                .fileType(image.getContentType())
                .announcement(announcement)
                .build();

        Image savedImage = imageRepository.save(uploadedImage);

        String serverHost = env.getProperty("server.address", "localhost");
        String serverPort = env.getProperty("server.port", "9000");
        String imageUrl = "http://" + serverHost + ":" + serverPort + "/api/v1/images/download/" + savedImage.getId();

        savedImage.setImageUrl(imageUrl);
        imageRepository.save(savedImage);

        return savedImage.getId();
    }

    @Override
    public Image getImage(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 ID로 이미지를 찾을 수 없습니다: " + id));
    }

    @Override
    public byte[] getImageData(Long id) throws IOException {
        Image image = getImage(id);
        Path imagePath = Paths.get(image.getFilePath()).normalize();
        return Files.readAllBytes(imagePath);
    }

    private String sanitizeFilename(String filename) {
        return filename.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}