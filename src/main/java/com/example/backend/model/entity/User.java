package com.example.backend.model.entity;

import com.example.backend.dto.UserRole;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String student_Id;
    private String password;
    private String name;
    private Integer level;
    private String status;

    private UserRole role;

    // OAuth 로그인에 사용
    private String provider;
    private String providerId;
}
