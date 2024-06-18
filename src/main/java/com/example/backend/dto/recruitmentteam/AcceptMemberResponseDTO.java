package com.example.backend.dto.recruitmentteam;

import com.example.backend.model.entity.member.Profile;
import com.example.backend.model.entity.recruitmentteam.AcceptMember;
import java.util.Set;
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

    public static AcceptMemberResponseDTO toResponseDTO(AcceptMember acceptMember, Profile profile) {
        return AcceptMemberResponseDTO.builder()
                .id(acceptMember.getId())
                .memberName(acceptMember.getMember().getName())
                .memberLevel(acceptMember.getMember().getLevel())
                .memberRole(acceptMember.getMemberRole().toString())
                .githubLink(profile.getGithubLink())
                .developmentField(profile.getDevelopmentField())
                .developmentTool(profile.getDevelopmentTool())
                .build();
    }

    public static Set<AcceptMemberResponseDTO> toResponseDTOSet(Set<AcceptMember> acceptMembers) {
        return acceptMembers.stream()
                .map(acceptMember -> toResponseDTO(acceptMember, acceptMember.getMember().getProfile()))
                .collect(java.util.stream.Collectors.toSet());
    }
}
