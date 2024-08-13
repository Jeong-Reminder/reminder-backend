package com.example.backend.dto.announcement;

import com.example.backend.dto.comment.CommentResponseDTO;
import com.example.backend.dto.vote.VoteResponseDTO;
import com.example.backend.model.entity.announcement.Announcement;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@Builder
public class AnnouncementResponseDTO {
    private Long id;
    private String announcementTitle;
    private String announcementContent;
    private String announcementCategory;
    private Boolean announcementImportant;
    private Integer announcementLevel;
    private List<FileResponseDTO> files;
    private List<ImageResponseDTO> images;
    private Boolean visible;
    private Long managerId;
    private List<CommentResponseDTO> comments;
    private List<VoteResponseDTO> votes;

    public static AnnouncementResponseDTO toResponseDTO(Announcement announcement) {
        return AnnouncementResponseDTO.builder()
                .id(announcement.getId())
                .announcementTitle(announcement.getAnnouncementTitle())
                .announcementContent(announcement.getAnnouncementContent())
                .announcementCategory(announcement.getAnnouncementCategory().name())
                .announcementImportant(announcement.getAnnouncementImportant())
                .announcementLevel(announcement.getAnnouncementLevel())
                .files(announcement.getFiles() != null ?
                        announcement.getFiles().stream().map(FileResponseDTO::fromEntity).collect(Collectors.toList()) : List.of())
                .images(announcement.getImages() != null ?
                        announcement.getImages().stream().map(ImageResponseDTO::fromEntity).collect(Collectors.toList()) : List.of())
                .visible(announcement.isVisible())
                .managerId(announcement.getManager().getId())
                .comments(announcement.getComments() != null ?
                        announcement.getComments().stream().map(CommentResponseDTO::fromEntity).collect(Collectors.toList()) : List.of())
                .votes(announcement.getVotes() != null ?
                        announcement.getVotes().stream().map(VoteResponseDTO::fromEntity).collect(Collectors.toList()) : List.of())
                .build();
    }

}
