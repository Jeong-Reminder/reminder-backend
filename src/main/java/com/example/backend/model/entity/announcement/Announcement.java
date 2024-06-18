package com.example.backend.model.entity.announcement;

import com.example.backend.dto.announcement.AnnouncementCategory;
import com.example.backend.model.entity.TimeZone;
import com.example.backend.model.entity.member.Member;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "announcement")
public class Announcement extends TimeZone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "announcement_title")
    private String announcementTitle;

    @Column(columnDefinition = "TEXT", name = "announcement_content")
    private String announcementContent;

    @Enumerated(EnumType.STRING)
    @Column(name = "announcement_category")
    private AnnouncementCategory announcementCategory;

    @Column(name = "announcement_important")
    private Boolean announcementImportant;

    @Column(name = "announcement_level")
    private int announcementLevel;

    @Builder.Default
    @Column(name = "img")
    private String img = "";

    @Builder.Default
    @Column(name = "file")
    private String file = "";

    @Builder.Default
    @Column(name = "visible")
    private boolean visible = true;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Member managerId;

    @Column(name = "good")
    private int good;
}
