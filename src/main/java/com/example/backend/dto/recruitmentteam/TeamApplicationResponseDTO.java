package com.example.backend.dto.recruitmentteam;

import com.example.backend.model.entity.member.Profile;
import com.example.backend.model.entity.recruitmentteam.ApplicationStatus;
import com.example.backend.model.entity.recruitmentteam.TeamApplication;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TeamApplicationResponseDTO {
    private Long id;
    private Long memberId;
    private String applicationContent;
    private ApplicationStatus applicationStatus;
    private Long recruitmentId;
    private String memberName;
    private String githubLink;
    private String developmentField;
    private String developmentTool;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public static TeamApplicationResponseDTO toResponseDTO(TeamApplication teamApplication) {
        return TeamApplicationResponseDTO.builder()
                .id(teamApplication.getId())
                .memberId(teamApplication.getMember().getId())
                .applicationContent(teamApplication.getApplicationContent())
                .applicationStatus(teamApplication.getApplicationStatus())
                .recruitmentId(teamApplication.getRecruitment().getId())
                .memberName(teamApplication.getMember().getName())
                .githubLink(teamApplication.getMember().getProfile().getGithubLink())
                .developmentField(teamApplication.getMember().getProfile().getDevelopmentField())
                .developmentTool(teamApplication.getMember().getProfile().getDevelopmentTool())
                .createdTime(teamApplication.getCreatedTime())
                .updatedTime(teamApplication.getUpdatedTime())
                .build();
    }

    public static Set<TeamApplicationResponseDTO> toResponseDTOSet(Set<TeamApplication> teamApplications) {
        return teamApplications.stream()
                .map(TeamApplicationResponseDTO::toResponseDTO)
                .collect(Collectors.toSet());
    }
}
