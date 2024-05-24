package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
public class JoinRequest {

    @NotBlank(message = "로그인 아이디가 비어있습니다.")
    private String studentId;

    @NotBlank(message = "비밀번호가 비어있습니다.")
    private String password;

//    @NotBlank(message = "이름이 비어있습니다.")
//    private String name;

//    @NotBlank(message = "학년이 비어있습니다.")
//    private Integer level;
//
//    @NotBlank(message = "학적상태가 비어있습니다.")
//    private String status;

}