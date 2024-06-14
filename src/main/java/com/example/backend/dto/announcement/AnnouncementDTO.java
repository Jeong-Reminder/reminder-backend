package com.example.backend.dto.announcement;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AnnouncementDTO {
    private Long id;
    private String announcementTitle;
    private String announcementContent;
    private AnnouncementCategory announcementCategory;
    private Boolean announcementImportant;
    private int announcementLevel;
    private String img;
    private String file;
    private boolean visible;
    private Long managerId;
    private int good;

    @Builder
    public AnnouncementDTO(Long id, String announcementTitle, String announcementContent, AnnouncementCategory announcementCategory, Boolean announcementImportant, int announcementLevel, String img, String file, boolean visible, Long managerId, int good) {
        this.id = id;
        this.announcementTitle = announcementTitle;
        this.announcementContent = announcementContent;
        this.announcementCategory = announcementCategory;
        this.announcementImportant = announcementImportant;
        this.announcementLevel = announcementLevel;
        this.img = img;
        this.file = file;
        this.visible = visible;
        this.managerId = managerId;
        this.good = good;
    }
}
