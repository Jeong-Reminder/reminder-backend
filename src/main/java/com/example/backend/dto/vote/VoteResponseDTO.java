package com.example.backend.dto.vote;

import com.example.backend.model.entity.vote.Vote;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoteResponseDTO {
    private Long id;
    private String subjectTitle;
    private boolean repetition;
    private boolean additional;
    private Long announcementId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endDateTime;
    private List<Long> voteItemIds;
    private List<VoteItemResponseDTO> voteItems;
    public VoteResponseDTO(Vote vote) {
        this.id = vote.getId();
        this.subjectTitle = vote.getSubjectTitle();
        this.repetition = vote.isRepetition();
        this.additional = vote.isAdditional();
        this.announcementId = getAnnouncementId();
        this.endDateTime=getEndDateTime();
    }

    public static VoteResponseDTO toResponseDTO(Vote vote) {
        return VoteResponseDTO.builder()
                .id(vote.getId())
                .subjectTitle(vote.getSubjectTitle())
                .repetition(vote.isRepetition())
                .additional(vote.isAdditional())
                .announcementId(vote.getAnnouncement().getId())
                .endDateTime(vote.getEndDateTime())
                .voteItemIds(parseVoteItemIds(vote.getVoteItemIds()))
                .voteItems(vote.getVoteItems() != null ? vote.getVoteItems().stream()
                        .map(VoteItemResponseDTO::toResponseDTO)
                        .collect(Collectors.toList()) : List.of())
                .build();
    }


    private static List<Long> parseVoteItemIds(String voteItemIds) {
        if (voteItemIds == null || voteItemIds.isEmpty()) {
            return List.of();
        }
        return Arrays.stream(voteItemIds.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}
