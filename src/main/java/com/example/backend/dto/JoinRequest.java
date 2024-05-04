package com.example.backend.dto;

import com.example.backend.model.entity.Member;
import com.example.backend.dto.UserRole;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class JoinRequest {

    private String name;
    private String password;

    @NotBlank(message = "로그인 아이디가 비어있습니다.")
    private String loginId;

//    @NotBlank(message = "비밀번호가 비어있습니다.")
//    private String password;
//    private String passwordCheck;



}