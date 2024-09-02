package com.example.backend.service.announcment.impl;

import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.announcement.Image;
import com.example.backend.model.repository.announcement.ImageRepository;
import com.example.backend.service.announcment.ImageService;
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
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final Environment env;

    @Value("${file.dir}")
    private String uploadDir;

    @Override
    public Long saveImage(MultipartFile image, Announcement announcement) throws IOException {
        String imageHash = generateFileHash(image);
        Image existingImage = imageRepository.findByImageHash(imageHash);

        if (existingImage != null) {
            return existingImage.getId();
        }

        String originalFilename = sanitizeFilename(Objects.requireNonNull(image.getOriginalFilename()));
        Path imagePath = Paths.get(uploadDir).resolve(originalFilename).normalize();
        Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

        Image uploadedImage = Image.builder()
                .originalFilename(originalFilename)
                .imagePath(imagePath.toString())
                .imageType(image.getContentType())
                .imageHash(imageHash)
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
                .orElseThrow(() -> new RuntimeException("Image with ID " + id + " not found"));
    }

    @Override
    public byte[] getImageData(Long id) throws IOException {
        Image image = getImage(id);
        Path imagePath = Paths.get(image.getImagePath()).normalize();
        return Files.readAllBytes(imagePath);
    }

    @Override
    @Transactional
    public void deleteImage(Long id) {
        Image image = getImage(id);
        Path imagePath = Paths.get(image.getImagePath()).normalize();

        try {
            Files.deleteIfExists(imagePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image file", e);
        }

        imageRepository.delete(image);
    }
    @Override
    public void deleteImageData(Long id) {
        Image image = getImage(id);
        Path imagePath = Paths.get(image.getImagePath()).normalize();

        try {
            Files.deleteIfExists(imagePath);
        } catch (IOException e) {
            throw new RuntimeException("이미지 파일을 삭제하는 데 실패했습니다: " + imagePath, e);
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
