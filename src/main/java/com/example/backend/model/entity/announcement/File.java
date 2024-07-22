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

    private String originalFilename;
    private String filePath;
    private String fileType;

    @ManyToOne
    @JoinColumn(name = "announcement_id")
    private Announcement announcement;
    @Builder
    public File(String originalFilename, String filePath, String fileType) {
        this.originalFilename = originalFilename;
        this.filePath = filePath;
        this.fileType = fileType;
    }
}
