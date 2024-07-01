package com.example.backend.dto.admin;

import com.example.backend.dto.member.MemberProfileResponseDTO;
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
public class MemberAdminResponseDTO {
    private String studentId;
    private String name;
    private int level;
    private String status;
    private UserRole userRole;


    public static MemberAdminResponseDTO toResponseDTO(Member Member){
        return MemberAdminResponseDTO.builder()
                .studentId(Member.getStudentId())
                .name(Member.getName())
                .level(Member.getLevel())
                .status(Member.getStatus())
                .userRole(Member.getUserRole())
                .build();
    }

    public static List<MemberAdminResponseDTO> toResponseDTOList(List<Member> members){
        return members.stream().map(MemberAdminResponseDTO::toResponseDTO).toList();
    }
}
