package com.example.backend.model.entity.member;

import com.example.backend.model.entity.recommend.Recommend;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String memberId; //로그인 아이디 = 학번
    private String password; //비밀번호
    private String name; //이름
    private Integer level; //학년
    private String status; //학적상태

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Profile profile;  // 프로필 정보

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recommend> recommends;
    public Member(String memberId, String password, String name, Integer level, String status, UserRole userRole) {
        this.memberId = memberId;
        this.password = password;
        this.name = name;
        this.level = level;
        this.status = status;
        this.userRole = userRole;
    }

}
