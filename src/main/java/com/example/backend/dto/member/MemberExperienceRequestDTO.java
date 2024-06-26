package com.example.backend.dto.member;

import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.member.MemberExperience;
import com.example.backend.model.entity.recruitmentteam.TeamMemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberExperienceRequestDTO {
    private String experienceName;
    private TeamMemberRole experienceRole;
    private String experienceContent;
    private String experienceGithub;
    private String experienceJob;
    private String experienceDate;

    public MemberExperience toEntity(Member member) {
        return MemberExperience.builder()
                .id(null)
                .experienceName(experienceName)
                .experienceRole(experienceRole)
                .experienceContent(experienceContent)
                .experienceGithub(experienceGithub)
                .experienceJob(experienceJob)
                .experienceDate(experienceDate)
                .member(member)
                .build();
    }
}
