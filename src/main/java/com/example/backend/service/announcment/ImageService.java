package com.example.backend.service.announcment;

import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.announcement.Image;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    Long saveImage(MultipartFile image) throws IOException;
    Long saveImage(MultipartFile image, Announcement announcement) throws IOException;
    void deleteImageById(Long imageId);
    Image getImage(Long id);

    ResponseEntity<Resource> downloadImage(Long id);
}
