package com.example.backend.dto.announcement;

import com.example.backend.model.entity.announcement.File;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileResponseDTO {
    private Long id;
    private String fileName;
    private byte[] fileData;

    public FileResponseDTO(File file) {
        this.id = file.getId();
        this.fileName = file.getOriginalFilename();
        try {
            this.fileData = Files.readAllBytes(Paths.get(file.getFilePath()));
        } catch (IOException e) {
            throw new RuntimeException("Error reading file data", e);
        }
    }
}
