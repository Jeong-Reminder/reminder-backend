package com.example.backend.controller.announcement;

import com.example.backend.model.entity.announcement.Image;
import com.example.backend.service.announcment.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<Long> uploadImage(@RequestParam("image") MultipartFile image,
                                            @RequestParam("announcementId") Long announcementId) {
        try {
            Long imageId = imageService.saveImage(image, null); // Announcement 객체는 서비스에서 처리하거나 직접 주입
            return new ResponseEntity<>(imageId, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadImage(@PathVariable Long id) {
        try {
            Image image = imageService.getImage(id);
            byte[] imageData = imageService.getImageData(id);

            String contentType = image.getFileType();
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentDispositionFormData("attachment", URLEncoder.encode(image.getOriginalFilename(), "UTF-8").replace("+", "%20"));

            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
