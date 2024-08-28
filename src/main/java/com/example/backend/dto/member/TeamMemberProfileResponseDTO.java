package com.example.backend.dto.member;

import static java.util.stream.Collectors.toList;

import com.example.backend.dto.recruitmentteam.TeamResponseDTO;
import com.example.backend.model.entity.member.MemberProfile;
import com.example.backend.model.entity.recruitmentteam.TeamMember;
import com.example.backend.model.entity.recruitmentteam.TeamMemberRole;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TeamMemberProfileResponseDTO {

    private Long id;
    private Long memberId;
    private String memberName;
    private TeamMemberRole memberRole;
    private int memberLevel;
    private String hopeJob;
    private String githubLink;
    private String developmentField; // 예: "Java, JavaScript, C, C++"
    private String developmentTool;     // 예: "IntelliJ, VSCode"

    public static TeamMemberProfileResponseDTO toResponseDTO(MemberProfile memberProfile, TeamMember teamMember) {
       return TeamMemberProfileResponseDTO.builder()
               .id(memberProfile.getId())
               .memberId(memberProfile.getMember().getId())
               .memberName(memberProfile.getMember().getName())
                .memberRole(teamMember.getTeamMemberRole())
               .memberLevel(memberProfile.getMember().getLevel())
               .hopeJob(memberProfile.getHopeJob())
               .githubLink(memberProfile.getGithubLink())
               .developmentField(memberProfile.getDevelopmentField())
               .developmentTool(memberProfile.getDevelopmentTool())
               .build();
    }

    public static List<TeamMemberProfileResponseDTO> toResponseDTOList(List<MemberProfile> memberProfiles, List<TeamMember> teamMembers) {
        return memberProfiles.stream()
                .map(MemberProfile -> toResponseDTO(MemberProfile, teamMembers.stream().filter(TeamMember -> TeamMember.getMember().getId().equals(MemberProfile.getMember().getId())).findFirst().get()
                ))
                .collect(toList());
    }
}