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
    private String imagePath; // 파일 경로를 저장
    private String imageType;
    private String imageUrl;
    private String imageHash;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "announcement_id")
    private Announcement announcement;

    @Builder
    public Image(String originalFilename, String imagePath, String imageType, String imageUrl,String imageHash,Announcement announcement) {
        this.originalFilename = originalFilename;
        this.imagePath = imagePath;
        this.imageType = imageType;
        this.imageUrl=imageUrl;
        this.imageHash = imageHash;
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
