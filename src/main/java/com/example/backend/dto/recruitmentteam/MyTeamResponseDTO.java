package com.example.backend.dto.recruitmentteam;

import com.example.backend.model.entity.recruitmentteam.Team;
import com.example.backend.model.entity.recruitmentteam.TeamMember;
import com.example.backend.model.entity.recruitmentteam.TeamMemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyTeamResponseDTO {
    private Long id;
    private String teamName;
    private String teamCategory;
    private TeamMemberRole teamMemberRole;

    public static MyTeamResponseDTO toResponseDTO(Team team, TeamMember teamMember) {
        return MyTeamResponseDTO.builder()
                .id(team.getId())
                .teamName(team.getTeamName())
                .teamCategory(team.getTeamCategory())
                .teamMemberRole(teamMember.getTeamMemberRole())
                .build();
    }
}
