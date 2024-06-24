package com.example.backend.model.entity.member;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "profile")
@Builder
@Entity
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String githubLink;   // 깃허브 링크
    private String developmentField; // 개발 분야
    private String developmentTool;  // 개발 도구

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
