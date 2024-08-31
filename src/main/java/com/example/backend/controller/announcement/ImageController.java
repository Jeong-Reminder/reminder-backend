package com.example.backend.controller.announcement;

import com.example.backend.model.entity.announcement.Image;
import com.example.backend.service.announcment.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadImage(@PathVariable Long id) {
        try {
            Image image = imageService.getImage(id);
            byte[] imageData = imageService.getImageData(id);

            String contentType = image.getImageType();
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            String encodedFileName = UriUtils.encode(image.getOriginalFilename(), StandardCharsets.UTF_8);

            String contentDisposition = "attachment; filename*=UTF-8''" + encodedFileName;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);

            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
