package com.example.backend.model.entity.member;

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

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Profile profile;  // 프로필 정보

    public Member(String studentId, String password, String name, Integer level, String status, UserRole userRole) {
        this.studentId = studentId;
        this.password = password;
        this.name = name;
        this.level = level;
        this.status = status;
        this.userRole = userRole;
    }

}
