package com.example.backend.dto.member;

import com.example.backend.model.entity.member.MemberExperience;
import com.example.backend.model.entity.recruitmentteam.TeamMemberRole;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberExperienceResponseDTO {
    private Long id;
    private Long memberId;
    private String experienceName;
    private TeamMemberRole experienceRole;
    private String experienceContent;
    private String experienceGithub;
    private String experienceJob;
    private String experienceDate;

    public static MemberExperienceResponseDTO toResponseDTO(MemberExperience memberExperience) {
        return MemberExperienceResponseDTO.builder()
                .id(memberExperience.getId())
                .memberId(memberExperience.getMember().getId())
                .experienceName(memberExperience.getExperienceName())
                .experienceRole(memberExperience.getExperienceRole())
                .experienceContent(memberExperience.getExperienceContent())
                .experienceGithub(memberExperience.getExperienceGithub())
                .experienceJob(memberExperience.getExperienceJob())
                .experienceDate(memberExperience.getExperienceDate())
                .build();
    }

    public static List<MemberExperienceResponseDTO> toResponseDTOList(List<MemberExperience> memberExperiences) {
        return memberExperiences.stream()
                .map(MemberExperienceResponseDTO::toResponseDTO)
                .collect(java.util.stream.Collectors.toList());
    }
}
