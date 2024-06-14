package com.example.backend.dto.member;

import com.example.backend.model.entity.member.UserRole;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
public class JoinRequestDTO {

    @NotBlank(message = "로그인 아이디가 비어있습니다.")
    private String studentId;

    @NotBlank(message = "비밀번호가 비어있습니다.")
    private String password;

    private UserRole userRole;
    //private boolean isAdmin; // 관리자 여부 추가
}