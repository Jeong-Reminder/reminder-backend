package com.example.backend.dto.announcement;

import com.example.backend.model.entity.announcement.File;
import com.example.backend.service.announcment.FileService;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileResponseDTO {
    private static FileService fileService;

    private Long id;
    private String originalFilename;
    private byte[] fileData;

    public FileResponseDTO(File file){
        this.id = file.getId();
        this.originalFilename = file.getOriginalFilename();
        try {
            this.fileData = fileService.getFileData(file.getId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
