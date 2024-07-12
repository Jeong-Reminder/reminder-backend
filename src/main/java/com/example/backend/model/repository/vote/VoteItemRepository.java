package com.example.backend.model.repository.vote;

import com.example.backend.model.entity.vote.Vote;
import com.example.backend.model.entity.vote.VoteItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteItemRepository extends JpaRepository<VoteItem, Long> {
    boolean existsByVoteAndContent(Vote vote, String content);

    List<VoteItem> findByVote(Vote vote);
}
