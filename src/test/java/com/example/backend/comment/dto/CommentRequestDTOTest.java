package com.example.backend.comment.dto;

import com.example.backend.dto.comment.CommentRequestDTO;
import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.comment.Comment;
import com.example.backend.model.entity.member.Member;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommentRequestDTOTest {

    @Test
    public void testToEntity() {
        String content = "Test comment";
        Member member = new Member();
        Announcement announcement = new Announcement();
        CommentRequestDTO dto = new CommentRequestDTO(content);

        Comment comment = dto.toEntity(member, announcement);

        assertEquals(content, comment.getContent());
        assertEquals(member, comment.getMember());
        assertEquals(announcement, comment.getAnnouncement());
    }
}
