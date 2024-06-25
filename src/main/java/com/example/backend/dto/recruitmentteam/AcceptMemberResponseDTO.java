package com.example.backend.dto.recruitmentteam;

import com.example.backend.model.entity.member.MemberProfile;
import com.example.backend.model.entity.recruitmentteam.AcceptMember;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AcceptMemberResponseDTO {
    private Long id;
    private String memberName;
    private int memberLevel;
    private String memberRole;
    private String githubLink;
    private String developmentField;
    private String developmentTool;

    public static AcceptMemberResponseDTO toResponseDTO(AcceptMember acceptMember, MemberProfile memberProfile) {
        return AcceptMemberResponseDTO.builder()
                .id(acceptMember.getId())
                .memberName(acceptMember.getMember().getName())
                .memberLevel(acceptMember.getMember().getLevel())
                .memberRole(acceptMember.getMemberRole().toString())
                .githubLink(memberProfile.getGithubLink())
                .developmentField(memberProfile.getDevelopmentField())
                .developmentTool(memberProfile.getDevelopmentTool())
                .build();
    }

    public static List<AcceptMemberResponseDTO> toResponseDTOSet(List<AcceptMember> acceptMembers) {
        return acceptMembers.stream()
                .map(acceptMember -> toResponseDTO(acceptMember, acceptMember.getMember().getMemberProfile()))
                .toList();
    }
}
