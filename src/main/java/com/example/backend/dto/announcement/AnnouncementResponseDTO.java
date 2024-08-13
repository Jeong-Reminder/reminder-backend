package com.example.backend.dto.announcement;

import com.example.backend.dto.comment.CommentResponseDTO;
import com.example.backend.dto.vote.VoteResponseDTO;
import com.example.backend.model.entity.announcement.Announcement;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AnnouncementResponseDTO {

    private Long id;
    private String announcementTitle;
    private String announcementContent;
    private String announcementCategory;
    private boolean announcementImportant;
    private int announcementLevel;
    private List<Long> fileIds = new ArrayList<>();
    private List<Long> imageIds = new ArrayList<>();
    private boolean visible;
    private Long managerId;
    private List<CommentResponseDTO> comments;
    private List<VoteResponseDTO> votes;
    private List<FileResponseDTO> files = new ArrayList<>();
    private List<ImageResponseDTO> images = new ArrayList<>();

    public static AnnouncementResponseDTO toResponseDTO(Announcement announcement) {
        List<FileResponseDTO> files = announcement.getFiles().stream()
                .map(FileResponseDTO::new)
                .collect(Collectors.toList());

        List<ImageResponseDTO> images = announcement.getImages().stream()
                .map(image -> {
                    try {
                        return new ImageResponseDTO(image, Files.readAllBytes(Paths.get(image.getFilePath())));
                    } catch (IOException e) {
                        throw new RuntimeException("이미지 데이터 업로드 오류", e);
                    }
                })
                .collect(Collectors.toList());

        return AnnouncementResponseDTO.builder()
                .id(announcement.getId())
                .announcementTitle(announcement.getAnnouncementTitle())
                .announcementContent(announcement.getAnnouncementContent())
                .announcementCategory(announcement.getAnnouncementCategory().toString())
                .announcementImportant(announcement.getAnnouncementImportant()) // or getAnnouncementImportant()
                .announcementLevel(announcement.getAnnouncementLevel())
                .images(images)
                .files(files)
                .visible(announcement.isVisible())
                .managerId(announcement.getManager().getId())
                .comments(announcement.getComments().stream().map(CommentResponseDTO::new).collect(Collectors.toList()))
                .votes(announcement.getVotes().stream().map(VoteResponseDTO::new).collect(Collectors.toList()))
                .build();
    }
}
