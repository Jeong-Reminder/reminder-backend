package com.example.backend.dto.member;

import com.example.backend.model.entity.member.Profile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberProfileRequestDTO {

    private String hopeJob;
    private String githubLink;
    private String developmentField; // 예: "Java, JavaScript, C, C++"
    private String developmentTool;     // 예: "IntelliJ, VSCode"

    public Profile toEntity() {
        return Profile.builder()
                .hopeJob(hopeJob)
                .githubLink(githubLink)
                .developmentField(developmentField)
                .developmentTool(developmentTool)
                .build();
    }
}
