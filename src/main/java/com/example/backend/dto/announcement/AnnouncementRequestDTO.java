package com.example.backend.dto.announcement;

import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.member.Member;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class AnnouncementRequestDTO {
    private String announcementTitle;
    private String announcementContent;
    private AnnouncementCategory announcementCategory;
    private Boolean announcementImportant;
    private int announcementLevel;
    private List<MultipartFile> img;
    private List<MultipartFile> file;
    private boolean visible;
    private Long managerId;
    private int good;

    public Announcement toEntity(Member managerId, List<String> imgPaths, List<String> filePaths) {
        return Announcement.builder()
                .announcementTitle(announcementTitle)
                .announcementContent(announcementContent)
                .announcementCategory(announcementCategory)
                .announcementImportant(announcementImportant)
                .announcementLevel(announcementLevel)
                .img(String.join(",", imgPaths))
                .file(String.join(",", filePaths))
                .visible(visible)
                .managerId(managerId)
                .good(good)
                .build();
    }


}
