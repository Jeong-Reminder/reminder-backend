package com.example.backend.dto.recruitmentteam;

import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.recruitmentteam.AcceptMember;
import com.example.backend.model.entity.recruitmentteam.Recruitment;
import com.example.backend.model.entity.recruitmentteam.TeamMemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AcceptMemberRequestDTO {
    private Long memberId;
    private Long recruitmentId;

    public AcceptMember toEntity(Member member, Recruitment recruitment) {
        return AcceptMember.builder()
                .id(null)
                .memberRole(TeamMemberRole.MEMBER)
                .member(member)
                .recruitment(recruitment)
                .build();
    }
}
