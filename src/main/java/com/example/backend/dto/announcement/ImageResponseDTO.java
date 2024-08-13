package com.example.backend.dto.announcement;

import com.example.backend.model.entity.announcement.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageResponseDTO {
    private Long id;
    private String imageName;
    private byte[] imageData;

    public ImageResponseDTO(Image image, byte[] imageData) {
        this.id = image.getId();
        this.imageName = image.getOriginalFilename();
        this.imageData = imageData;
    }
}
