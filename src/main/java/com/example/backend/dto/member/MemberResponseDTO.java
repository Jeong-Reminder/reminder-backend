package com.example.backend.dto.member;

import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.member.Profile;
import com.example.backend.model.entity.member.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberResponseDTO {
    private String name;
    private int level;
    private String status;
    private UserRole userRole;
    private TechStackResponseDTO techStack;


    public static MemberResponseDTO toResponseDTO(Member Member, Profile profile){
        return MemberResponseDTO.builder()
                .name(Member.getName())
                .level(Member.getLevel())
                .status(Member.getStatus())
                .userRole(Member.getUserRole())
                .techStack(TechStackResponseDTO.toResponseDTO(profile))
                .build();
    }
}
