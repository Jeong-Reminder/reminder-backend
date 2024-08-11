package com.example.backend.dto.announcement;

import com.example.backend.model.entity.announcement.File;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FileResponseDTO {
    private Long id;
    private String orgNm;
    private String saveNm;
    private String savedPath;

    public static FileResponseDTO fromEntity(File file) {
        return FileResponseDTO.builder()
                .id(file.getId())
                .orgNm(file.getOriginalFilename())
                .saveNm(file.getFilePath())
                .savedPath(file.getSavedPath())
                .build();
    }
}
