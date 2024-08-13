package com.example.backend.model.entity.announcement;

import jakarta.persistence.*;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "announcement_id", nullable = false)
    private Announcement announcement;
    @Builder
    public File(String originalFilename, String filePath, String fileType, Announcement announcement) {
        this.originalFilename = originalFilename;
        this.filePath = filePath;
        this.fileType = fileType;
        this.announcement = announcement;
    }
}
