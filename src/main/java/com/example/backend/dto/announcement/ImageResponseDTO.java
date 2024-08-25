package com.example.backend.dto.announcement;

import com.example.backend.model.entity.announcement.Image;
import com.example.backend.service.announcment.ImageService;
import com.example.backend.service.announcment.impl.ImageServiceImpl;
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
public class ImageResponseDTO {
    private Long id;
    private String imageName;
    private byte[] imageData;
    private String imageUrl;
}
