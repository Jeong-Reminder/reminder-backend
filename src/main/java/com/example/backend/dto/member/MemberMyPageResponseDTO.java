package com.example.backend.dto.member;

import com.example.backend.dto.recruitmentteam.TeamResponseDTO;
import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.member.MemberProfile;
import com.example.backend.model.entity.recruitmentteam.Team;
import java.util.List;
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
    List<TeamResponseDTO> teamResponseDTOList;

    public static MemberMyPageResponseDTO toResponseDTO(Member member, MemberProfile memberProfile, List<TeamResponseDTO> teamList){
        return MemberMyPageResponseDTO.builder()
                .studentId(member.getStudentId())
                .name(member.getName())
                .level(member.getLevel())
                .status(member.getStatus())
                .hopeJob(memberProfile.getHopeJob())
                .githubLink(memberProfile.getGithubLink())
                .developmentField(memberProfile.getDevelopmentField())
                .developmentTool(memberProfile.getDevelopmentTool())
                .teamResponseDTOList(teamList)
                .build();
    }


}
