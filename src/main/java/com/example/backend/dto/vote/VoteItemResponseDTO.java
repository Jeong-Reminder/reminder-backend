package com.example.backend.dto.vote;

import com.example.backend.model.entity.vote.VoteItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoteItemResponseDTO {
    private Long id;
    private String content;
    private boolean hasVoted;
    private List<String> voters;
    public static VoteItemResponseDTO toResponseDTO(VoteItem voteItem, boolean hasVoted, List<String> voters) {
        return VoteItemResponseDTO.builder()
                .id(voteItem.getId())
                .content(voteItem.getContent())
                .hasVoted(hasVoted)
                .voters(voters)
                .build();
    }

    public static VoteItemResponseDTO toResponseDTO(VoteItem voteItem) {
        return VoteItemResponseDTO.builder()
                .id(voteItem.getId())
                .content(voteItem.getContent())
                .hasVoted(false)
                .voters(List.of())
                .build();
    }
}