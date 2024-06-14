package com.example.backend.dto.recruitmentteam;

import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.recruitmentteam.Recruitment;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RecruitmentRequestDTO {
    private String recruitmentCategory;
    private String recruitmentTitle;
    private String recruitmentContent;
    private int studentCount;
    private String hopeField;
    private String kakaoUrl;
    private boolean recruitmentStatus;
    private LocalDateTime endTime;
    private Long memberId;
    private Long announcementId;

    public Recruitment toEntity(Member member, Announcement announcement) {
        return Recruitment.builder()
                .id(null)
                .recruitmentCategory(recruitmentCategory)
                .recruitmentTitle(recruitmentTitle)
                .recruitmentContent(recruitmentContent)
                .studentCount(studentCount)
                .hopeField(hopeField)
                .kakaoUrl(kakaoUrl)
                .recruitmentStatus(recruitmentStatus)
                .endTime(endTime)
                .member(member)
                .announcement(announcement)
                .build();
    }
}
