package com.example.backend.dto.recruitmentteam;

import com.example.backend.dto.member.MemberProfileResponseDTO;
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
    private Long memberId;
    private String memberName;
    private int memberLevel;
    private MemberProfileResponseDTO memberProfile;
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
    private Set<TeamApplicationResponseDTO> teamApplicationList;
    private List<AcceptMemberResponseDTO> acceptMemberList;

    public static RecruitmentResponseDTO toResponseDTO(Recruitment recruitment) {
        return RecruitmentResponseDTO.builder()
                .id(recruitment.getId())
                .memberId(recruitment.getMember().getId())
                .memberName(recruitment.getMember().getName())
                .memberLevel(recruitment.getMember().getLevel())
                .memberProfile(MemberProfileResponseDTO.toResponseDTO(recruitment.getMember().getMemberProfile()))
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
                .teamApplicationList(TeamApplicationResponseDTO.toResponseDTOSet(recruitment.getTeamApplications()))
                .acceptMemberList(AcceptMemberResponseDTO.toResponseDTOSet(recruitment.getAcceptMembers()))
                .build();
    }

    public static List<RecruitmentResponseDTO> toResponseDTOList(List<Recruitment> recruitmentList) {
        return recruitmentList.stream()
                .map(RecruitmentResponseDTO::toResponseDTO)
                .toList();
    }
}
