package com.example.backend.dto.vote;

import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.vote.Vote;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.thymeleaf.util.StringUtils;

import java.time.LocalTime;
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
    private LocalTime endTime;
    private List<Long> voteItemIds;

    public Vote toEntity(Announcement announcement) {
        Vote vote = new Vote();
        vote.setSubjectTitle(this.subjectTitle);
        vote.setRepetition(this.repetition);
        vote.setAdditional(this.additional);
        vote.setAnnouncement(announcement);
        vote.setEndTime(this.endTime);
        if (this.voteItemIds != null && !this.voteItemIds.isEmpty()) {
            vote.setVoteItemIds(StringUtils.join(this.voteItemIds, ","));
        }
        return vote;
    }
}
