package com.example.backend.model.entity.announcement;

import com.example.backend.dto.announcement.AnnouncementCategory;
import com.example.backend.dto.announcement.AnnouncementRequestDTO;
import com.example.backend.model.entity.TimeZone;
import com.example.backend.model.entity.comment.Comment;
import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.vote.Vote;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
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
    private Member manager;

    @Column(name = "good")
    private int good;

    @OneToMany(mappedBy = "announcement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "announcement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vote> votes;

    public void update(AnnouncementRequestDTO dto, List<String> imgPaths, List<String> filePaths, Vote vote) {
        this.announcementTitle = dto.getAnnouncementTitle();
        this.announcementContent = dto.getAnnouncementContent();
        this.announcementCategory = dto.getAnnouncementCategory();
        this.announcementImportant = dto.getAnnouncementImportant();
        this.announcementLevel = dto.getAnnouncementLevel();
        this.img = String.join(",", imgPaths);
        this.file = String.join(",", filePaths);
        this.visible = dto.isVisible();
        this.good = dto.getGood();
        this.votes = vote != null ? List.of(vote) : this.votes;
    }

}
