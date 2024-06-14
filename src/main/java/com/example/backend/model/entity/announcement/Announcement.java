package com.example.backend.model.entity.announcement;

import com.example.backend.dto.announcement.AnnouncementCategory;
import com.example.backend.dto.announcement.AnnouncementDTO;
import com.example.backend.dto.announcement.AnnouncementRequestDTO;
import com.example.backend.dto.announcement.AnnouncementResponseDTO;
import com.example.backend.model.entity.TimeZone;
import com.example.backend.model.entity.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "announcement")
public class Announcement extends TimeZone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "announcement_title")
    private String announcementTitle;

    @Column(columnDefinition = "TEXT", name = "content")
    private String announcementContent;

    @Column(name = "announcement_category")
    private AnnouncementCategory announcementCategory;

    @Column(name = "announcement_important")
    private Boolean announcementImportant;

    @Column(name = "announcement_level")
    private int announcementLevel;

    @Column(name = "img")
    private String img;

    @Column(name = "file")
    private String file = ""; // 초기값 설정

    @Column(name = "visible")
    private boolean visible = true; // 초기값 설정

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Member managerId;

    @Column(name = "good")
    private int good;

    @Builder
    public Announcement(String announcementTitle, AnnouncementCategory category, String announcementContent, Boolean announcementImportant, int announcementLevel, String img, String file, boolean visible, Member managerId, int good) {
        setAnnouncementTitle(announcementTitle);
        setCategory(category);
        this.announcementContent = announcementContent;
        this.announcementImportant = announcementImportant;
        this.announcementLevel = announcementLevel;
        this.img = img;
        this.file = file;
        this.visible = visible;
        this.managerId = managerId;
        this.good = good;
    }

    private void setAnnouncementTitle(String announcementTitle) {
        if (Objects.isNull(announcementTitle)) {
            throw new IllegalArgumentException("게시글의 제목은 null일 수 없습니다.");
        } else if (announcementTitle.isBlank()) {
            throw new IllegalArgumentException("게시글의 제목은 빈 문자열일 수 없습니다.");
        } else if (announcementTitle.length() > 20) {
            throw new IllegalArgumentException("게시글의 제목은 20글자를 넘을 수 없습니다.");
        }
        this.announcementTitle = announcementTitle;
    }

    private void setCategory(AnnouncementCategory category) {
        if (Objects.isNull(category)) {
            throw new IllegalArgumentException("게시글의 category는 null일 수 없습니다.");
        }
        this.announcementCategory = category;
    }

    public AnnouncementDTO toDTO() {
        return AnnouncementDTO.builder()
                .id(this.id)
                .announcementTitle(this.announcementTitle)
                .announcementContent(this.announcementContent)
                .announcementCategory(this.announcementCategory)
                .announcementImportant(this.announcementImportant)
                .announcementLevel(this.announcementLevel)
                .img(this.img)
                .file(this.file)
                .visible(this.visible)
                .managerId(this.managerId != null ? this.managerId.getId() : null)
                .good(this.good)
                .build();
    }

    public AnnouncementResponseDTO toResponseDTO() {
        return AnnouncementResponseDTO.builder()
                .id(this.id)
                .announcementTitle(this.announcementTitle)
                .announcementContent(this.announcementContent)
                .announcementCategory(this.announcementCategory)
                .announcementImportant(this.announcementImportant)
                .announcementLevel(this.announcementLevel)
                .img(this.img)
                .file(this.file)
                .visible(this.visible)
                .managerId(this.managerId != null ? this.managerId.getId() : null)
                .good(this.good)
                .build();
    }

    public static Announcement fromRequestDTO(AnnouncementRequestDTO requestDTO, Member manager) {
        return Announcement.builder()
                .announcementTitle(requestDTO.getAnnouncementTitle())
                .category(requestDTO.getAnnouncementCategory())
                .announcementContent(requestDTO.getAnnouncementContent())
                .announcementImportant(requestDTO.getAnnouncementImportant())
                .announcementLevel(requestDTO.getAnnouncementLevel())
                .img(requestDTO.getImg())
                .file(requestDTO.getFile())
                .visible(requestDTO.isVisible())
                .managerId(manager)
                .good(requestDTO.getGood())
                .build();
    }
}
