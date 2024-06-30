package com.example.backend.model.entity.member;

import com.example.backend.model.entity.recruitmentteam.TeamMember;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member")
@Builder
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String studentId; //로그인 아이디 = 학번
    @Column
    private String password; //비밀번호
    @Column
    private String name; //이름
    @Column
    private int level; //학년
    @Column
    private String status; //학적상태
    @Enumerated(EnumType.STRING)
    @Column
    private UserRole userRole;

    @OneToOne(mappedBy = "member", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private MemberProfile memberProfile;  // 프로필 정보

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<MemberExperience> memberExperiences; // 경험 리스트

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamMember> teamMembers; // 팀 멤버 리스트
}