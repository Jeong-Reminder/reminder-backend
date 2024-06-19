package com.example.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ProfileDTO {

    private String githubLink;
    private String developmentField;
    private String developmentTool;
}
