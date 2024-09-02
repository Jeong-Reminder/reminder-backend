package com.example.backend.service.announcment;

import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.announcement.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    Long saveImage(MultipartFile image, Announcement announcement) throws IOException;
    Image getImage(Long id);
    byte[] getImageData(Long id) throws IOException;

    void deleteImage(Long id);

    void deleteImageData(Long id);
}
