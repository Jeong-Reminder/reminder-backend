package com.example.backend.dto.announcement;

import com.example.backend.dto.comment.CommentResponseDTO;
import com.example.backend.dto.vote.VoteResponseDTO;
import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.announcement.File;
import com.example.backend.model.entity.announcement.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public static AnnouncementResponseDTO toResponseDTO(Announcement announcement) {
        return AnnouncementResponseDTO.builder()
                .id(announcement.getId())
                .announcementTitle(announcement.getAnnouncementTitle())
                .announcementContent(announcement.getAnnouncementContent())
                .announcementCategory(announcement.getAnnouncementCategory() != null ? announcement.getAnnouncementCategory().toString() : null)
                .announcementImportant(announcement.getAnnouncementImportant())
                .announcementLevel(announcement.getAnnouncementLevel())
                .build();
    }
}