package com.example.backend.controller.announcement;

import com.example.backend.service.announcment.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<Long> uploadImage(@RequestParam("image") MultipartFile image) throws IOException {
        Long imageId = imageService.saveImage(image);
        return ResponseEntity.ok(imageId);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long id) {
        return imageService.downloadImage(id);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        imageService.deleteImageById(id);
        return ResponseEntity.noContent().build();
    }
}
