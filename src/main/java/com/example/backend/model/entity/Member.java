package com.example.backend.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String studentId; //로그인 아이디 = 학번
    private String password; //비밀번호
    private String name; //이름
    private Integer level; //학년
    private String status; //학적상태

    private String role;

    // 새로 추가된 필드
    private String githubLink;
    private String developmentField;
    private String developmentTool;

    public Member(String studentId, String password, String name, Integer level, String status, String role) {
        this.studentId = studentId;
        this.password = password;
        this.name = name;
        this.level = level;
        this.status = status;
        this.role = role;
    }

}
