package com.example.backend.dto.vote;

import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.vote.Vote;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoteRequestDTO {
    private String subjectTitle;
    private boolean repetition;
    private boolean additional;
    private Long announcementId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDateTime;
    private List<Long> voteItemIds;

    public Vote toEntity(Announcement announcement) {
        Vote vote = new Vote();
        vote.setSubjectTitle(this.subjectTitle);
        vote.setRepetition(this.repetition);
        vote.setAdditional(this.additional);
        vote.setAnnouncement(announcement);
        vote.setEndDateTime(endDateTime);
        if (this.voteItemIds != null && !this.voteItemIds.isEmpty()) {
            vote.setVoteItemIds(StringUtils.join(this.voteItemIds, ","));
        } else {
            vote.setVoteItemIds("");
        }
        return vote;
    }

}
