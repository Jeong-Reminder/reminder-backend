package com.example.backend.model.entity.announcement;

import com.example.backend.dto.announcement.ImageResponseDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalFilename;
    private String filePath; // 파일 경로를 저장
    private String fileType;
    private String imageUrl;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "announcement_id")
    private Announcement announcement;

    @Builder
    public Image(String originalFilename, String filePath, String fileType, String imageUrl,Announcement announcement) {
        this.originalFilename = originalFilename;
        this.filePath = filePath;
        this.fileType = fileType;
        this.imageUrl=imageUrl;
        this.announcement = announcement;
    }

    public static ImageResponseDTO toResponseDTO(Image image, byte[] imageData) {
        return ImageResponseDTO.builder()
                .id(image.id)
                .imageName(image.originalFilename)
                .imageData(imageData)
                .imageUrl(image.imageUrl)
                .build();
    }
}
