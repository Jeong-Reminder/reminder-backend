package com.example.backend.dto.recruitmentteam;

import com.example.backend.model.entity.member.Profile;
import com.example.backend.model.entity.recruitmentteam.Recruitment;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RecruitmentResponseDTO {
    private Long id;
    private String memberName;
    private Profile memberProfile;
    private String recruitmentCategory;
    private String recruitmentTitle;
    private String recruitmentContent;
    private int studentCount;
    private String hopeField;
    private String kakaoUrl;
    private boolean recruitmentStatus;
    private LocalDateTime createdTime;
    private LocalDateTime endTime;
    private Long announcementId;
    private Set<TeamApplicationResponseDTO> teamApplicationIdList;

    public static RecruitmentResponseDTO toResponseDTO(Recruitment recruitment) {
        return RecruitmentResponseDTO.builder()
                .id(recruitment.getId())
                .memberName(recruitment.getMember().getName())
                .memberProfile(recruitment.getMember().getProfile())
                .recruitmentCategory(recruitment.getRecruitmentCategory())
                .recruitmentTitle(recruitment.getRecruitmentTitle())
                .recruitmentContent(recruitment.getRecruitmentContent())
                .studentCount(recruitment.getStudentCount())
                .hopeField(recruitment.getHopeField())
                .kakaoUrl(recruitment.getKakaoUrl())
                .recruitmentStatus(recruitment.isRecruitmentStatus())
                .createdTime(recruitment.getCreatedTime())
                .endTime(recruitment.getEndTime())
                .announcementId(recruitment.getAnnouncement().getId())
                .teamApplicationIdList(TeamApplicationResponseDTO.toResponseDTOSet(recruitment.getTeamApplications()))
                .build();
    }

    public static List<RecruitmentResponseDTO> toResponseDTOList(List<Recruitment> recruitmentList) {
        return recruitmentList.stream()
                .map(RecruitmentResponseDTO::toResponseDTO)
                .toList();
    }
}
