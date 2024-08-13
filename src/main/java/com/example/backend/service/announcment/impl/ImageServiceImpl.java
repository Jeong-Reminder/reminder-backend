package com.example.backend.service.announcment.impl;

import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.announcement.Image;
import com.example.backend.model.repository.announcement.ImageRepository;
import com.example.backend.service.announcment.ImageService;
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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    @Value("${image.dir}")
    private String imageUploadDir;

    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("png", "jpeg", "jpg");

    @Override
    public Long saveImage(MultipartFile image) throws IOException {
        return saveImage(image, null);
    }

    @Override
    public Long saveImage(MultipartFile image, Announcement announcement) throws IOException {
        String originalFilename = image.getOriginalFilename();
        String extension = getFileExtension(originalFilename);

        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new IllegalArgumentException("지원되지 않는 이미지 파일 형식입니다: " + extension);
        }

        String sanitizedFilename = sanitizeFilename(originalFilename);
        String newFilename = UUID.randomUUID().toString() + "_" + sanitizedFilename;
        Path imagePath = Paths.get(imageUploadDir).resolve(newFilename).normalize();

        Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

        // 이미지를 먼저 저장하여 ID를 얻음
        Image uploadedImage = Image.builder()
                .originalFilename(originalFilename)
                .filePath(imagePath.toString())
                .fileType(image.getContentType())
                .announcement(announcement)
                .build();

        Image savedImage = imageRepository.save(uploadedImage);

        // 이미지 ID를 이용해 다운로드 URL을 생성
        String downloadUrl = "http://localhost:9000/api/v1/images/download/" + savedImage.getId();

        // savedPath를 업데이트하고 다시 저장
        savedImage.setSavedPath(downloadUrl);
        imageRepository.save(savedImage);

        return savedImage.getId();
    }

    @Override
    public void deleteImageById(Long imageId) {
        Image image = getImage(imageId);
        imageRepository.delete(image);
        try {
            Files.deleteIfExists(Paths.get(image.getFilePath()));
        } catch (IOException e) {
            throw new RuntimeException("이미지 삭제 중 오류가 발생했습니다: " + imageId, e);
        }
    }

    @Override
    public Image getImage(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 ID로 이미지를 찾을 수 없습니다: " + id));
    }

    @Override
    public ResponseEntity<Resource> downloadImage(Long id) {
        try {
            Image image = getImage(id);
            Path imagePath = Paths.get(image.getFilePath()).normalize();
            Resource resource = new UrlResource(imagePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            String encodedFilename = URLEncoder.encode(image.getOriginalFilename(), "UTF-8").replace("+", "%20");
            String contentType = Files.probeContentType(imagePath);
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFilename + "\"")
                    .body(resource);

        } catch (IOException e) {
            throw new RuntimeException("이미지 다운로드 중 오류가 발생했습니다.", e);
        }
    }

    private String sanitizeFilename(String filename) {
        return filename.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
