package com.example.backend.dto.recruitmentteam;

import com.example.backend.model.entity.member.Member;
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
    private boolean applicationStatus;
    private Long recruitmentId;

    public TeamApplication toEntity(Member member, Recruitment recruitment) {
        return TeamApplication.builder()
                .id(null)
                .applicationContent(applicationContent)
                .applicationStatus(applicationStatus)
                .member(member)
                .recruitment(recruitment)
                .build();
    }
}
