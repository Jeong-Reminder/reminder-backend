package com.example.backend.dto.member;

import com.example.backend.dto.recruitmentteam.MyTeamResponseDTO;
import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.member.MemberProfile;
import com.example.backend.model.entity.recruitmentteam.Team;
import com.example.backend.model.entity.recruitmentteam.TeamMember;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberMyPageResponseDTO {
    private String studentId;
    private String name;
    private int level;
    private String status;
    private String hopeJob;
    private String githubLink;
    private String developmentField;
    private String developmentTool;
    private List<MyTeamResponseDTO> myTeamList;

    public static MemberMyPageResponseDTO toResponseDTO(Member member, MemberProfile memberProfile, List<Team> teamList, List<TeamMember> teamMemberList){
        return MemberMyPageResponseDTO.builder()
                .studentId(member.getStudentId())
                .name(member.getName())
                .level(member.getLevel())
                .status(member.getStatus())
                .hopeJob(memberProfile.getHopeJob())
                .githubLink(memberProfile.getGithubLink())
                .developmentField(memberProfile.getDevelopmentField())
                .developmentTool(memberProfile.getDevelopmentTool())
                .myTeamList(teamList == null || teamList.isEmpty() ? null : toMyTeamResponseDTOList(teamList, teamMemberList))
                .build();
    }

    private static List<MyTeamResponseDTO> toMyTeamResponseDTOList(List<Team> teamList, List<TeamMember> teamMemberList) {
        return teamList.stream()
                .map(team -> {
                    TeamMember teamMember = teamMemberList.stream()
                            .filter(tm -> tm.getTeam().getId().equals(team.getId()))
                            .findFirst()
                            .orElse(null);
                    return MyTeamResponseDTO.toResponseDTO(team, teamMember);
                })
                .collect(Collectors.toList());
    }
}