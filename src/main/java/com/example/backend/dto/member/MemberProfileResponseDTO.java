package com.example.backend.dto.member;

import java.util.List;

import com.example.backend.model.entity.member.MemberProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberProfileResponseDTO {

    private Long id;
    private Long memberId;
    private String memberName;
    private int memberLevel;
    private String hopeJob;
    private String githubLink;
    private String developmentField; // 예: "Java, JavaScript, C, C++"
    private String developmentTool;     // 예: "IntelliJ, VSCode"

    public static MemberProfileResponseDTO toResponseDTO(MemberProfile memberProfile) {
       return MemberProfileResponseDTO.builder()
               .id(memberProfile.getId())
               .memberId(memberProfile.getMember().getId())
               .memberName(memberProfile.getMember().getName())
               .memberLevel(memberProfile.getMember().getLevel())
                .hopeJob(memberProfile.getHopeJob())
               .githubLink(memberProfile.getGithubLink())
               .developmentField(memberProfile.getDevelopmentField())
               .developmentTool(memberProfile.getDevelopmentTool())
               .build();
    }

    public static List<MemberProfileResponseDTO> toResponseDTOList(List<MemberProfile> memberProfiles) {
        return memberProfiles.stream()
                .map(MemberProfileResponseDTO::toResponseDTO)
                .collect(java.util.stream.Collectors.toList());
    }
}