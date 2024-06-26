package com.example.backend.dto.member;

import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.member.MemberExperience;
import com.example.backend.model.entity.member.MemberProfile;
import com.example.backend.model.entity.member.UserRole;
import java.util.List;
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
    private MemberProfileResponseDTO techStack;
    private List<MemberExperience> memberExperiences;


    public static MemberResponseDTO toResponseDTO(Member Member, MemberProfile memberProfile, List<MemberExperience> memberExperiences){
        return MemberResponseDTO.builder()
                .name(Member.getName())
                .level(Member.getLevel())
                .status(Member.getStatus())
                .userRole(Member.getUserRole())
                .techStack(MemberProfileResponseDTO.toResponseDTO(memberProfile))
                .memberExperiences(memberExperiences)
                .build();
    }
}
