package com.example.backend.dto.announcement;

import com.example.backend.dto.comment.CommentResponseDTO;
import com.example.backend.dto.vote.VoteResponseDTO;
import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.announcement.File;
import com.example.backend.model.entity.announcement.Image;
import lombok.*;

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
    private List<Long> fileIds;
    private List<Long> imageIds;
    private boolean visible;
    private Long managerId;
    private List<CommentResponseDTO> comments;
    private List<VoteResponseDTO> votes;
    private List<FileResponseDTO> files;
    private List<ImageResponseDTO> images;

    public static AnnouncementResponseDTO toResponseDTO(Announcement announcement) {
        return AnnouncementResponseDTO.builder()
                .id(announcement.getId())
                .announcementTitle(announcement.getAnnouncementTitle())
                .announcementContent(announcement.getAnnouncementContent())
                .announcementCategory(announcement.getAnnouncementCategory().toString())
                .announcementImportant(announcement.getAnnouncementImportant())
                .announcementLevel(announcement.getAnnouncementLevel())
                .fileIds(announcement.getFiles().stream()
                        .map(File::getId)
                        .collect(Collectors.toList()))
                .imageIds(announcement.getImages().stream()
                        .map(Image::getId)
                        .collect(Collectors.toList()))
                .files(announcement.getFiles().stream()
                        .map(FileResponseDTO::new)
                        .collect(Collectors.toList()))
                .images(announcement.getImages().stream()
                        .map(ImageResponseDTO::new)
                        .collect(Collectors.toList()))
                .visible(announcement.isVisible())
                .managerId(announcement.getManager().getId())
                .comments(announcement.getComments().stream().map(CommentResponseDTO::new).collect(Collectors.toList()))
                .votes(announcement.getVotes().stream()
                        .map(vote -> VoteResponseDTO.toResponseDTO(vote, false))
                        .collect(Collectors.toList()))
                .build();
    }
}
