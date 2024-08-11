package com.example.backend.comment.model.repository;

import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.comment.Comment;
import com.example.backend.model.entity.member.Member;
import com.example.backend.model.repository.comment.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

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
    public void testFindByAnnouncementId() {
        Comment comment = Comment.builder()
                .content("Test comment")
                .announcement(announcement)
                .member(member)
                .build();
        entityManager.persistAndFlush(comment);

        List<Comment> comments = commentRepository.findByAnnouncementId(announcement.getId());

        assertNotNull(comments);
        assertEquals(1, comments.size());
        assertEquals("Test comment", comments.get(0).getContent());
    }
}
