package com.example.backend.model.repository.vote;


import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.vote.Vote;
import com.example.backend.model.entity.vote.VoteItem;
import com.example.backend.model.entity.vote.VoteStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteStatusRepository extends JpaRepository<VoteStatus, Long> {
    boolean existsByVoteAndMember(Vote vote, Member member);
    List<VoteStatus> findByVoteAndMember(Vote vote, Member member);

    List<VoteStatus> findByVoteItem(VoteItem voteItem);

    List<VoteStatus> findByVote(Vote vote);

    boolean existsByVoteAndMemberAndVoteItem(Vote vote, Member member, VoteItem voteItem);
}