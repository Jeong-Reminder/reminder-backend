package com.example.backend.dto.member;

import com.example.backend.model.entity.member.Profile;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TechStackResponseDTO {

    private Long id;
    private Long memberId;
    private String memberName;
    private int memberLevel;
    private String githubLink;
    private String developmentField; // 예: "Java, JavaScript, C, C++"
    private String developmentTool;     // 예: "IntelliJ, VSCode"

    public static TechStackResponseDTO toResponseDTO(Profile profile) {
       return TechStackResponseDTO.builder()
               .id(profile.getId())
               .memberId(profile.getMember().getId())
                .memberName(profile.getMember().getName())
                .memberLevel(profile.getMember().getLevel())
               .githubLink(profile.getGithubLink())
               .developmentField(profile.getDevelopmentField())
               .developmentTool(profile.getDevelopmentTool())
               .build();
    }

    public static List<TechStackResponseDTO> toResponseDTOList(List<Profile> profiles) {
        return profiles.stream()
                .map(TechStackResponseDTO::toResponseDTO)
                .collect(java.util.stream.Collectors.toList());
    }
}