package com.example.backend.dto.comment;

import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.comment.Comment;
import com.example.backend.model.entity.member.Member;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDTO {
    private String content;
    private Long announcementId;
    private Long memberId;

    public Comment toEntity(Member member, Announcement announcement) {
        return Comment.builder()
                .content(this.content)
                .announcement(announcement)
                .member(member)
                .build();
    }
}
