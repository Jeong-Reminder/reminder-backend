package com.example.backend.model.repository.vote;

import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.vote.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Vote findByAnnouncement(Announcement announcement);

    List<Vote> findByVoteEndedFalseAndEndDateTimeBefore(LocalDateTime now);
}


