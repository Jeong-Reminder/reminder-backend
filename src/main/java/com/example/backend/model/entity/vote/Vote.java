package com.example.backend.model.entity.vote;

import com.example.backend.model.entity.TimeZone;
import com.example.backend.model.entity.announcement.Announcement;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDateTime;

    @Setter
    @Column(name = "vote_ended")
    private boolean voteEnded = false;

    private String voteItemIds;

    @OneToMany(mappedBy = "vote", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VoteStatus> voteStatuses;

    @Getter
    @OneToMany(mappedBy = "vote", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VoteItem> voteItems;

}
