package com.example.backend.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(unique = true)
    private String studentId; //로그인 아이디 = 학번
    private String password; // 비밀번호
    private String name; //이름
    private Integer level;
    private String status;

    private String role;

}
