package com.example.backend.dto.member;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequestDTO {

    private String memberId;
    private String password;

}
