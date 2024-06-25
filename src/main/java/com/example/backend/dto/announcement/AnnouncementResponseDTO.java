package com.example.backend.dto.announcement;

import com.example.backend.dto.comment.CommentResponseDTO;
import com.example.backend.dto.vote.VoteResponseDTO;
import com.example.backend.model.entity.announcement.Announcement;
import lombok.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class AnnouncementResponseDTO {
    private Long id;
    private String announcementTitle;
    private String announcementContent;
    private AnnouncementCategory announcementCategory;
    private Boolean announcementImportant;
    private int announcementLevel;
    private List<String> imgUrls;
    private List<String> fileUrls;
    private boolean visible;
    private Long managerId;
    private List<CommentResponseDTO> comments;
    private List<VoteResponseDTO> votes; // Include votes

    public static AnnouncementResponseDTO toResponseDTO(Announcement announcement) {
        return AnnouncementResponseDTO.builder()
                .id(announcement.getId())
                .announcementTitle(announcement.getAnnouncementTitle())
                .announcementContent(announcement.getAnnouncementContent())
                .announcementCategory(announcement.getAnnouncementCategory())
                .announcementImportant(announcement.getAnnouncementImportant())
                .announcementLevel(announcement.getAnnouncementLevel())
                .imgUrls(Arrays.stream(announcement.getImg().split(","))
                        .map(path -> "/files/" + path.substring(path.lastIndexOf("/") + 1))
                        .collect(Collectors.toList()))
                .fileUrls(Arrays.stream(announcement.getFile().split(","))
                        .map(path -> "/files/" + path.substring(path.lastIndexOf("/") + 1))
                        .collect(Collectors.toList()))
                .visible(announcement.isVisible())
                .managerId(announcement.getManager().getId())
                .comments(announcement.getComments().stream()
                        .map(CommentResponseDTO::toResponseDTO)
                        .collect(Collectors.toList()))
                .votes(announcement.getVotes().stream()
                        .map(VoteResponseDTO::toResponseDTO)
                        .collect(Collectors.toList()))
                .build();
    }
}
