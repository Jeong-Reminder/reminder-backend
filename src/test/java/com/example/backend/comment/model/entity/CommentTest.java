package com.example.backend.comment.model.entity;

import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.comment.Comment;
import com.example.backend.model.entity.member.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class CommentTest {

    @Autowired
    private TestEntityManager entityManager;

    private Member member;
    private Announcement announcement;

    @BeforeEach
    public void setUp() {
        member = new Member();
        member.setName("John Doe");
        member = entityManager.persistAndFlush(member);

        announcement = new Announcement();
        announcement.setTitle("Test Announcement");
        announcement = entityManager.persistAndFlush(announcement);
    }

    @Test
    public void testCommentEntityMapping() {

        Comment comment = Comment.builder()
                .content("Test comment")
                .announcement(announcement)
                .member(member)
                .build();

        Comment savedComment = entityManager.persistAndFlush(comment);

        assertNotNull(savedComment.getId());
        assertEquals("Test comment", savedComment.getContent());
        assertEquals(announcement.getId(), savedComment.getAnnouncement().getId());
        assertEquals(member.getId(), savedComment.getMember().getId());
    }

    @Test
    public void testCommentUpdate() {
        Comment comment = Comment.builder()
                .content("Initial comment")
                .announcement(announcement)
                .member(member)
                .build();
        Comment savedComment = entityManager.persistAndFlush(comment);

        savedComment.setContent("Updated comment");
        Comment updatedComment = entityManager.persistAndFlush(savedComment);

        assertNotNull(updatedComment.getId());
        assertEquals("Updated comment", updatedComment.getContent());
    }

    @Test
    public void testCommentDeletion() {
        Comment comment = Comment.builder()
                .content("Test comment")
                .announcement(announcement)
                .member(member)
                .build();
        Comment savedComment = entityManager.persistAndFlush(comment);
        Long commentId = savedComment.getId();

        entityManager.remove(savedComment);
        entityManager.flush();

        Comment deletedComment = entityManager.find(Comment.class, commentId);
        assertEquals(null, deletedComment);
    }
}
