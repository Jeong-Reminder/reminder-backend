package com.example.backend.service.vote;

import com.example.backend.dto.vote.VoteRequestDTO;
import com.example.backend.dto.vote.VoteResponseDTO;
import com.example.backend.dto.vote.VoteItemRequestDTO;
import com.example.backend.dto.vote.VoteItemResponseDTO;
import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.vote.Vote;
import org.springframework.security.core.Authentication;
import java.util.List;

public interface VoteService {

    Vote createVote(Authentication authentication, VoteRequestDTO voteRequestDTO, Announcement announcement);

    void vote(Authentication authentication, Long voteId, Long voteItemId);

    void vote(Authentication authentication, Long voteId, List<Long> voteItemIds);

    VoteResponseDTO updateVote(Authentication authentication, Long voteId, VoteRequestDTO voteRequestDTO);

    void deleteVote(Authentication authentication, Long voteId);

    List<VoteResponseDTO> getAllVotes(Authentication authentication);

    VoteResponseDTO getVoteById(Authentication authentication, Long voteId);

    VoteItemResponseDTO addVoteItem(Authentication authentication, Long voteId, VoteItemRequestDTO voteItemRequestDTO);

    void endVote(Authentication authentication, Long voteId);

    void deleteVoteItem(Authentication authentication, Long voteItemId);

    void recastVote(Authentication authentication, Long voteId, List<Long> voteItemIds);
}
