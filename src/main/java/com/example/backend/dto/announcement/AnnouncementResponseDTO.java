package com.example.backend.dto.announcement;

import com.example.backend.dto.comment.CommentResponseDTO;
import com.example.backend.dto.vote.VoteResponseDTO;
import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.announcement.File;
import com.example.backend.model.entity.vote.Vote;
import lombok.*;

import java.util.ArrayList;
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
    private boolean announcementImportant;
    private int announcementLevel;
    private List<String> imgUrls;
    private List<FileResponseDTO> files;
    private boolean visible;
    private Long managerId;
    private List<CommentResponseDTO> comments;
    private List<VoteResponseDTO> votes;

    public static AnnouncementResponseDTO toResponseDTO(Announcement announcement) {
        return AnnouncementResponseDTO.builder()
                .id(announcement.getId())
                .announcementTitle(announcement.getAnnouncementTitle())
                .announcementContent(announcement.getAnnouncementContent())
                .announcementCategory(announcement.getAnnouncementCategory())
                .announcementImportant(announcement.getAnnouncementImportant())
                .announcementLevel(announcement.getAnnouncementLevel())
                .imgUrls(convertFilesToUrls(announcement.getFiles()))
                .files(convertFilesToDTOs(announcement.getFiles()))
                .visible(announcement.isVisible())
                .managerId(announcement.getManager().getId())
                .comments(announcement.getComments() != null
                        ? announcement.getComments().stream()
                        .map(CommentResponseDTO::toResponseDTO)
                        .collect(Collectors.toList())
                        : new ArrayList<>())
                .votes(announcement.getVotes() != null
                        ? announcement.getVotes().stream()
                        .map(VoteResponseDTO::toResponseDTO)
                        .collect(Collectors.toList())
                        : new ArrayList<>())
                .build();
    }

    public static AnnouncementResponseDTO toResponseDTO(Announcement announcement, List<Vote> votes) {
        return AnnouncementResponseDTO.builder()
                .id(announcement.getId())
                .announcementTitle(announcement.getAnnouncementTitle())
                .announcementContent(announcement.getAnnouncementContent())
                .announcementCategory(announcement.getAnnouncementCategory())
                .announcementImportant(announcement.getAnnouncementImportant())
                .announcementLevel(announcement.getAnnouncementLevel())
                .imgUrls(convertFilesToUrls(announcement.getFiles()))
                .files(convertFilesToDTOs(announcement.getFiles()))
                .visible(announcement.isVisible())
                .managerId(announcement.getManager().getId())
                .comments(announcement.getComments() != null
                        ? announcement.getComments().stream()
                        .map(CommentResponseDTO::toResponseDTO)
                        .collect(Collectors.toList())
                        : new ArrayList<>())
                .votes(votes != null
                        ? votes.stream()
                        .map(VoteResponseDTO::toResponseDTO)
                        .collect(Collectors.toList())
                        : new ArrayList<>())
                .build();
    }
    private static List<String> convertFilesToUrls(List<File> files) {
        return files != null
                ? files.stream()
                .map(file -> "http://localhost:9000/api/v1/files/download/" + file.getId())
                .collect(Collectors.toList())
                : new ArrayList<>();
    }
    private static List<FileResponseDTO> convertFilesToDTOs(List<File> files) {
        return files != null
                ? files.stream()
                .map(FileResponseDTO::fromEntity)
                .collect(Collectors.toList())
                : new ArrayList<>();
    }
}
