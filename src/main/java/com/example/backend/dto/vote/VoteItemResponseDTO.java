package com.example.backend.dto.vote;


import com.example.backend.model.entity.vote.VoteItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoteItemResponseDTO {
    private Long id;
    private String content;

    public static VoteItemResponseDTO toResponseDTO(VoteItem voteItem) {
        return VoteItemResponseDTO.builder()
                .id(voteItem.getId())
                .content(voteItem.getContent())
                .build();
    }

}