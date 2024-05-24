package com.example.backend.controller;

import com.example.backend.dto.TokenDTO;
import com.example.backend.jwt.JWTUtil;
import com.example.backend.jwt.TokenProvider;
import com.example.backend.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class RefreshTokenController {

    private final TokenProvider tokenProvider;
    private final JWTUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissueToken(@RequestBody TokenDTO.TokenRequest tokenRequest) {
        String refreshToken = tokenRequest.getRefreshToken();
        if (!jwtUtil.isExpired(refreshToken)) {
            String studentId = jwtUtil.getStudentId(refreshToken);
            String savedRefreshToken = refreshTokenService.getRefreshToken(studentId);
            if (refreshToken.equals(savedRefreshToken)) {
                String role = jwtUtil.getRole(refreshToken);
                String newAccessToken = jwtUtil.createJwt("access", studentId, role, 600000L);
                return ResponseEntity.ok(new TokenDTO.TokenResponse(newAccessToken, refreshToken));
            }
        }
        return ResponseEntity.status(401).body("Invalid or expired refresh token");
    }
}
