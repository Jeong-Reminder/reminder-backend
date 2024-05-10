package com.example.backend.model.entity;

import jakarta.persistence.*;
import lombok.*;
import com.example.backend.dto.MemberRole;

@Entity
@Setter
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(unique = true)
    private String studentId; //로그인 아이디 = 학번
    private String password; // 비밀번호
    private String name; //이름
    private Integer level;
    private String status;

    private MemberRole role;
    //매개변수를 받는 생성자
//    public Member(String student_Id,String password, String name,Integer level, String status, String role) {
//        this.student_Id=student_Id;
//        this.password=password;
//        this.name=name;
//        this.level=level;
//        this.status=status;
//        this.role=role;
//
//    }

}
