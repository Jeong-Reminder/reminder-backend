package com.example.backend.dto.announcement;

import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.announcement.File;
import com.example.backend.model.entity.announcement.Image;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementResponseDTO {

    private Long id;
    private String announcementTitle;
    private String announcementContent;
    private String announcementCategory;
    private boolean announcementImportant;
    private int announcementLevel;
    private List<FileResponseDTO> files; // 수정된 필드
    private List<ImageResponseDTO> images; // 수정된 필드
    private LocalDateTime createdTime;

    public static AnnouncementResponseDTO toResponseDTO(Announcement announcement) {
        List<FileResponseDTO> fileResponseDTOS = announcement.getFiles() != null ?
                announcement.getFiles().stream()
                        .map(file -> FileResponseDTO.builder()
                                .id(file.getId())
                                .originalFilename(file.getOriginalFilename())
                                .fileUrl(file.getFileUrl())
                                .build())
                        .collect(Collectors.toList()) : null;

        List<ImageResponseDTO> imageResponseDTOS = announcement.getImages() != null ?
                announcement.getImages().stream()
                        .map(image -> ImageResponseDTO.builder()
                                .id(image.getId())
                                .imageName(image.getOriginalFilename())
                                .imageUrl(image.getImageUrl())
                                .build())
                        .collect(Collectors.toList()) : null;

        return AnnouncementResponseDTO.builder()
                .id(announcement.getId())
                .announcementTitle(announcement.getAnnouncementTitle())
                .announcementContent(announcement.getAnnouncementContent())
                .announcementCategory(announcement.getAnnouncementCategory() != null ? announcement.getAnnouncementCategory().toString() : null)
                .announcementImportant(announcement.getAnnouncementImportant())
                .announcementLevel(announcement.getAnnouncementLevel())
                .files(fileResponseDTOS)
                .images(imageResponseDTOS)
                .createdTime(announcement.getCreatedTime())
                .build();
    }
}
