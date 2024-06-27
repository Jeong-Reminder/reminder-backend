package com.example.backend.model.entity.member;

import com.example.backend.model.entity.recruitmentteam.TeamMemberRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member_experience")
@Entity
public class MemberExperience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String experienceName;

    @Enumerated(EnumType.STRING)
    @Column
    private TeamMemberRole experienceRole;

    @Column
    private String experienceContent;

    @Column
    private String experienceGithub;

    @Column
    private String experienceJob;

    @Column
    private String experienceDate;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
