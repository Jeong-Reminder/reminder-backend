package com.example.backend.dto.recruitmentteam;

import com.example.backend.model.entity.recruitmentteam.ApplicationStatus;
import com.example.backend.model.entity.recruitmentteam.Recruitment;
import com.example.backend.model.entity.recruitmentteam.TeamApplication;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TeamApplicationRequestDTO {
    private String applicationContent;

    public TeamApplication toEntity(Member member, Recruitment recruitment) {
        return TeamApplication.builder()
                .id(null)
                .applicationContent(applicationContent)
                .applicationStatus(ApplicationStatus.WAITING)
                .member(member)
                .recruitment(recruitment)
                .build();
    }
}
