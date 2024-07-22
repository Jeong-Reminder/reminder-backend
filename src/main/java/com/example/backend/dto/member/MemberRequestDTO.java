package com.example.backend.dto.member;

import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.member.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberRequestDTO {
    private String studentId;
    private String password;
    private String name;
    private int level;
    private String status;
    private UserRole userRole;

    public Member toEntity(String passwordEncoder) {
        return Member.builder()
                .studentId(studentId)
                .password(passwordEncoder)
                .name(name)
                .level(level)
                .status(status)
                .userRole(UserRole.ROLE_USER)
                .fcmToken(null)
                .build();
    }

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(studentId, password);
    }
}
