package com.example.backend.dto.announcement;

import com.example.backend.model.entity.announcement.File;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileResponseDTO {
    private Long id;
    private String originalFilename;
    private byte[] fileData;

    public FileResponseDTO(File file) {
        this.id = file.getId();
        this.originalFilename = file.getOriginalFilename();
    }
}
