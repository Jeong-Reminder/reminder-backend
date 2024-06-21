package com.example.backend.dto.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDTO {
    private String grantType;
    private String accessToken;
    private Long tokenExpiresIn;
    private String refreshToken; // 리프레쉬 토큰 필드 추가
    private Long refreshTokenExpiresIn; // 리프레쉬 토큰의 만료 시간 필드 추가
}
