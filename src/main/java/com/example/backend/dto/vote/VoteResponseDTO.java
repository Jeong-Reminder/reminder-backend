package com.example.backend.dto.vote;

import com.example.backend.model.entity.vote.Vote;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    private LocalDateTime endTime;
    private List<Long> voteItemIds;

    public static VoteResponseDTO toResponseDTO(Vote vote) {
        return VoteResponseDTO.builder()
                .id(vote.getId())
                .subjectTitle(vote.getSubjectTitle())
                .repetition(vote.isRepetition())
                .additional(vote.isAdditional())
                .announcementId(vote.getAnnouncement().getId())
                .endTime(vote.getEndTime())
                .voteItemIds(parseVoteItemIds(vote.getVoteItemIds()))
                .build();
    }

    public static VoteResponseDTO fromEntity(Vote vote) {
        return VoteResponseDTO.builder()
                .id(vote.getId())
                .subjectTitle(vote.getSubjectTitle())
                .repetition(vote.isRepetition())
                .additional(vote.isAdditional())
                .announcementId(vote.getAnnouncement().getId())
                .endTime(vote.getEndTime())
                .voteItemIds(parseVoteItemIds(vote.getVoteItemIds()))
                .build();
    }

    private static List<Long> parseVoteItemIds(String voteItemIds) {
        if (voteItemIds == null || voteItemIds.isEmpty()) {
            return List.of(); // 빈 리스트 반환
        }
        return Arrays.stream(voteItemIds.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}
