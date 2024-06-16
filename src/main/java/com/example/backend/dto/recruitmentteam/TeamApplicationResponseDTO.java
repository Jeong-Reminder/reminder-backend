package com.example.backend.dto.recruitmentteam;

import com.example.backend.model.entity.member.Profile;
import com.example.backend.model.entity.recruitmentteam.TeamApplication;
import java.time.LocalDateTime;
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
    private String applicationContent;
    private boolean applicationStatus;
    private Long recruitmentId;
    private String memberName;
    private Profile memberProfile;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public static TeamApplicationResponseDTO toResponseDTO(TeamApplication teamApplication) {
        return TeamApplicationResponseDTO.builder()
                .id(teamApplication.getId())
                .applicationContent(teamApplication.getApplicationContent())
                .applicationStatus(teamApplication.isApplicationStatus())
                .recruitmentId(teamApplication.getRecruitment().getId())
                .memberName(teamApplication.getMember().getName())
                .memberProfile(teamApplication.getMember().getProfile())
                .createdTime(teamApplication.getCreatedTime())
                .updatedTime(teamApplication.getUpdatedTime())
                .build();
    }
}
