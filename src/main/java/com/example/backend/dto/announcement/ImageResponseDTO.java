package com.example.backend.dto.announcement;

import com.example.backend.model.entity.announcement.Image;
import com.example.backend.service.announcment.ImageService;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageResponseDTO {
    private static ImageService fileService;

    private Long id;
    private String imageName;
    private byte[] imageData;

    public ImageResponseDTO(Image image) {
        this.id = image.getId();
        this.imageName = image.getOriginalFilename();
        try {
            this.imageData = fileService.getImageData(image.getId()) == null ? null : fileService.getImageData(image.getId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
