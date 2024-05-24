package com.example.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class TechStackDTO {

    private String githubLink;
    private String developmentField; // 예: "Java, JavaScript, C, C++"
    private String developmentTool;     // 예: "IntelliJ, VSCode"
}
