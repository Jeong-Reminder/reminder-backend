package com.example.backend.model.entity.recruitmentteam;

import com.example.backend.model.entity.TimeZone;
import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.member.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "recruitment")
@Builder
@Entity
public class Recruitment extends TimeZone {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "recruitment_category", nullable = false, length = 50)
    private String recruitmentCategory;

    @Column(name = "recruitment_title", nullable = false, length = 50)
    private String recruitmentTitle;

    @Column(name = "recruitment_content", nullable = false, columnDefinition = "TEXT")
    private String recruitmentContent;

    @Column(name = "student_count", nullable = false)
    private int studentCount;

    @Column(name = "hope_field", nullable = false, length = 500)
    private String hopeField;

    @Column(name = "kakao_url", nullable = false)
    private String kakaoUrl;

    @Column(name = "recruitment_status", nullable = false)
    private boolean recruitmentStatus;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "member_id") // 외래 키 컬럼 명시
    private Member member;

    @ManyToOne
    @JoinColumn(name = "announcement_id")
    private Announcement announcement;

    @OneToMany(mappedBy = "recruitment", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TeamApplication> teamApplications; // 필드 초기화

    @OneToMany(mappedBy = "recruitment", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AcceptMember> acceptMembers; // 필드 초기화
}
