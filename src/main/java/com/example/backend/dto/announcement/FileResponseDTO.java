package com.example.backend.dto.announcement;

import com.example.backend.model.entity.announcement.File;
import com.example.backend.service.announcment.FileService;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileResponseDTO {
    private Long id;
    private String originalFilename;
    private byte[] fileData;

}
