package com.example.backend.dto.announcement;

import com.example.backend.dto.comment.CommentResponseDTO;
import com.example.backend.dto.vote.VoteResponseDTO;
import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.announcement.File;
import com.example.backend.model.entity.announcement.Image;
import java.util.ArrayList;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementDetailResponseDTO {

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

    public static AnnouncementDetailResponseDTO toResponseDTO(Announcement announcement,List<FileResponseDTO> fileResponseDTO, List<ImageResponseDTO> imageResponseDTO) {
        return AnnouncementDetailResponseDTO.builder()
                .id(announcement.getId())
                .announcementTitle(announcement.getAnnouncementTitle())
                .announcementContent(announcement.getAnnouncementContent())
                .announcementCategory(announcement.getAnnouncementCategory() != null ? announcement.getAnnouncementCategory().toString() : null)
                .announcementImportant(announcement.getAnnouncementImportant())
                .announcementLevel(announcement.getAnnouncementLevel())
                .fileIds(announcement.getFiles() != null ?
                        announcement.getFiles().stream()
                                .map(File::getId)
                                .collect(Collectors.toList())
                        : new ArrayList<>())
                .imageIds(announcement.getImages() != null ?
                        announcement.getImages().stream()
                                .map(Image::getId)
                                .collect(Collectors.toList())
                        : new ArrayList<>())
                .files(fileResponseDTO)
                .images(imageResponseDTO)
                .visible(announcement.isVisible())
                .managerId(announcement.getManager() != null ? announcement.getManager().getId() : null)
                .comments(announcement.getComments() != null ?
                        announcement.getComments().stream()
                                .map(CommentResponseDTO::new)
                                .collect(Collectors.toList())
                        : new ArrayList<>())
                .votes(announcement.getVotes() != null ?
                        announcement.getVotes().stream()
                                .map(vote -> VoteResponseDTO.toResponseDTO(vote, false))
                                .collect(Collectors.toList())
                        : new ArrayList<>())
                .build();
    }
}
