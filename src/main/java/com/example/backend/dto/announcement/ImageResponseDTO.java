package com.example.backend.dto.announcement;

import com.example.backend.model.entity.announcement.Image;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ImageResponseDTO {
    private Long id;
    private String orgNm;
    private String saveNm;
    private String savedPath;

    public static ImageResponseDTO fromEntity(Image image) {
        return ImageResponseDTO.builder()
                .id(image.getId())
                .orgNm(image.getOriginalFilename())
                .saveNm(image.getFilePath())
                .savedPath(image.getSavedPath())
                .build();
    }
}
