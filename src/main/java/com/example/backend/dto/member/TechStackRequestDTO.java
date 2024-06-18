package com.example.backend.dto.member;

import com.example.backend.model.entity.member.Profile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TechStackRequestDTO {

    private String githubLink;
    private String developmentField; // 예: "Java, JavaScript, C, C++"
    private String developmentTool;     // 예: "IntelliJ, VSCode"

    public Profile toEntity() {
        return Profile.builder()
                .githubLink(githubLink)
                .developmentField(developmentField)
                .developmentTool(developmentTool)
                .build();
    }
}
