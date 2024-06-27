package com.example.backend.dto.recruitmentteam;

import com.example.backend.model.entity.recruitmentteam.AcceptMember;
import com.example.backend.model.entity.recruitmentteam.Recruitment;
import com.example.backend.model.entity.recruitmentteam.Team;
import com.example.backend.model.entity.recruitmentteam.TeamMember;
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
public class TeamRequestDTO {
    private Long recruitmentId;
    private String teamName;
    private String kakaoUrl;

    public Team toEntity(Recruitment recruitment) {
        return Team.builder()
                .id(null)
                .teamName(teamName)
                .teamCategory(recruitment.getRecruitmentCategory())
                .kakaoUrl(kakaoUrl)
                .build();
    }

    public List<TeamMember> toTeamMemberEntity(Team team, List<AcceptMember> acceptMembers) {
        List<TeamMember> teamMembers = new ArrayList<>();
        for(AcceptMember acceptMember : acceptMembers) {
            TeamMember teamMember = TeamMember.builder()
                    .id(null)
                    .team(team)
                    .teamMemberRole(acceptMember.getMemberRole())
                    .member(acceptMember.getMember())
                    .build();
            teamMembers.add(teamMember);
        }
        return teamMembers;
    }
}
