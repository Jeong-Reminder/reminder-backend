package com.example.backend.dto.vote;


import com.example.backend.model.entity.vote.Vote;
import com.example.backend.model.entity.vote.VoteItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoteItemRequestDTO {
    private String content;

    public VoteItem toEntity(Vote vote) {
        VoteItem voteItem = new VoteItem();
        voteItem.setContent(this.content);
        voteItem.setVote(vote);
        return voteItem;
    }
}
