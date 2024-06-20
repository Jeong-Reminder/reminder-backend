package com.example.backend.model.repository.vote;

import com.example.backend.model.entity.vote.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
}


