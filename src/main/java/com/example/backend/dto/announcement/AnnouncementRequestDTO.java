package com.example.backend.dto.announcement;

import com.example.backend.dto.vote.VoteRequestDTO;
import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.vote.Vote;
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
    private VoteRequestDTO voteRequest;

    public Announcement toEntity(Member manager, List<String> imgPaths, List<String> filePaths, Vote vote) {
        return Announcement.builder()
                .announcementTitle(announcementTitle)
                .announcementContent(announcementContent)
                .announcementCategory(announcementCategory)
                .announcementImportant(announcementImportant)
                .announcementLevel(announcementLevel)
                .imgUrls(imgPaths != null ? imgPaths : List.of()) // imgUrls 설정
                .fileUrls(filePaths != null ? filePaths : List.of()) // fileUrls 설정
                .visible(visible)
                .manager(manager)
                .votes(vote != null ? List.of(vote) : null)
                .build();
    }
}
