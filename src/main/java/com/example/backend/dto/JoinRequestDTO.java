package com.example.backend.dto;

import com.example.backend.model.entity.Member;
import com.example.backend.model.entity.UserRole;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JoinRequestDTO {

    @NotBlank(message = "로그인 아이디가 비어있습니다.")
    private String studentId;

    @NotBlank(message = "비밀번호가 비어있습니다.")
    private String password;

    @NotBlank(message = "이름이 비어있습니다.")
    private String name;

    private Integer level;
    private String status;
    private UserRole userRole;

    public Member toEntity(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .studentId(studentId)
                .password(passwordEncoder.encode(password))
                .name(name)
                .level(level)
                .status(status)
                .userRole(userRole != null ? userRole : UserRole.ROLE_USER)
                .build();
    }

    public static JoinRequestDTO fromEntity(Member member) {
        return JoinRequestDTO.builder()
                .studentId(member.getStudentId())
                .name(member.getName())
                .level(member.getLevel())
                .status(member.getStatus())
                .userRole(member.getUserRole())
                .build();
    }

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(studentId, password);
    }
}