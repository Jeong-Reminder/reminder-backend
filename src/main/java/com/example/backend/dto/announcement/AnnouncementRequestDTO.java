package com.example.backend.dto.announcement;

import com.example.backend.dto.vote.VoteRequestDTO;
import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.announcement.File;
import com.example.backend.model.entity.announcement.Image;
import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.vote.Vote;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
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
    @Builder.Default
    private List<MultipartFile> newImages = new ArrayList<>();
    @Builder.Default
    private List<MultipartFile> newFiles = new ArrayList<>();

    private List<Long> fileIds;
    private List<Long> imageIds;
    private boolean visible;
    private Long managerId;
    private VoteRequestDTO voteRequest;

    public Announcement toEntity(Member manager, List<Image> images, List<File> files, Vote vote) {
        return Announcement.builder()
                .announcementTitle(announcementTitle)
                .announcementContent(announcementContent)
                .announcementCategory(announcementCategory)
                .announcementImportant(announcementImportant)
                .announcementLevel(announcementLevel)
                .images(images != null ? images : List.of())
                .files(files != null ? files : List.of())
                .visible(visible)
                .manager(manager)
                .votes(vote != null ? List.of(vote) : new ArrayList<>())
                .build();
    }
}
