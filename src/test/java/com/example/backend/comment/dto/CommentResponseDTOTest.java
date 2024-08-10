package com.example.backend.comment.dto;


import com.example.backend.dto.comment.CommentResponseDTO;
import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.comment.Comment;
import com.example.backend.model.entity.member.Member;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommentResponseDTOTest {

    @Test
    public void testToResponseDTO() {
        // Given
        Long commentId = 1L;
        String content = "Test comment";
        Long announcementId = 2L;
        Long memberId = 3L;
        String memberName = "John Doe";

        Member member = new Member();
        member.setId(memberId);
        member.setName(memberName);

        Announcement announcement = new Announcement();
        announcement.setId(announcementId);

        Comment comment = Comment.builder()
                .id(commentId)
                .content(content)
                .announcement(announcement)
                .member(member)
                .build();

        CommentResponseDTO dto = CommentResponseDTO.toResponseDTO(comment);

        assertEquals(commentId, dto.getId());
        assertEquals(content, dto.getContent());
        assertEquals(announcementId, dto.getAnnouncementId());
        assertEquals(memberId, dto.getMemberId());
        assertEquals(memberName, dto.getMemberName());
    }
}