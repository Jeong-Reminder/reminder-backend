package com.example.backend.model.entity.vote;

import com.example.backend.model.entity.TimeZone;
import com.example.backend.model.entity.announcement.Announcement;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "vote")
public class Vote extends TimeZone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subject_title")
    private String subjectTitle;

    @Column(name = "repetition")
    private boolean repetition;

    @Column(name = "additional")
    private boolean additional;

    @ManyToOne
    @JoinColumn(name = "announcement_id")
    private Announcement announcement;

    @Column(name="endTime")
    private LocalDateTime endTime;
    private String voteItemIds;

    @OneToMany(mappedBy = "vote")
    private List<VoteStatus> voteStatuses;
}

