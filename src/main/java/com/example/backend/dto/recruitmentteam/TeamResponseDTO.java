package com.example.backend.dto.recruitmentteam;

import com.example.backend.dto.member.MemberProfileResponseDTO;
import com.example.backend.dto.member.TeamMemberProfileResponseDTO;
import com.example.backend.model.entity.member.MemberProfile;
import com.example.backend.model.entity.recruitmentteam.Team;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TeamResponseDTO {
    private Long id;
    private String teamName;
    private String teamCategory;
    private List<TeamMemberProfileResponseDTO> teamMember;

    public static TeamResponseDTO toResponseDTO(List<MemberProfile> memberProfiles, Team team) {
        return TeamResponseDTO.builder()
                .id(team.getId())
                .teamName(team.getTeamName())
                .teamCategory(team.getTeamCategory())
                .teamMember(TeamMemberProfileResponseDTO.toResponseDTOList(memberProfiles,team.getTeamMembers()))
                .build();
    }
}
