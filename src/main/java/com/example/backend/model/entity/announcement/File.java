package com.example.backend.model.entity.announcement;

import com.example.backend.dto.announcement.FileResponseDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originalFilename;
    @Column(nullable = false)
    private String filePath;
    @Column(nullable = false)
    private String fileType;
    private String fileUrl;
    private String fileHash;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "announcement_id", nullable = false)
    private Announcement announcement;
    @Builder
    public File(String originalFilename, String filePath, String fileType,String fileUrl,String fileHash, Announcement announcement) {
        this.originalFilename = originalFilename;
        this.filePath = filePath;
        this.fileType = fileType;
        this.fileUrl=fileUrl;
        this.fileHash = fileHash;
        this.announcement = announcement;
    }

    public static FileResponseDTO toResponseDTO(File file, byte[] fileData) {
        return FileResponseDTO.builder()
                .id(file.id)
                .originalFilename(file.originalFilename)
                .fileData(fileData)
                .fileUrl(file.fileUrl)
                .build();
    }
}
