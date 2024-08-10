package com.example.backend.comment.service;

import com.example.backend.dto.comment.CommentRequestDTO;
import com.example.backend.dto.comment.CommentResponseDTO;
import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.comment.Comment;
import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.member.UserRole;
import com.example.backend.model.repository.announcement.AnnouncementRepository;
import com.example.backend.model.repository.comment.CommentRepository;
import com.example.backend.model.repository.member.MemberRepository;
import com.example.backend.service.comment.impl.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private AnnouncementRepository announcementRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateComment() {
        String studentId = "testStudentId";
        Long announcementId = 1L;
        CommentRequestDTO requestDTO = new CommentRequestDTO();
        requestDTO.setContent("This is a test comment");

        Member member = new Member();
        member.setId(1L);
        when(authentication.getName()).thenReturn(studentId);
        when(memberRepository.findByStudentId(studentId)).thenReturn(member);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        Announcement announcement = new Announcement();
        announcement.setId(announcementId);
        when(announcementRepository.findById(announcementId)).thenReturn(Optional.of(announcement));

        when(commentRepository.save(any(Comment.class))).thenReturn(new Comment());

        CommentResponseDTO responseDTO = commentService.createComment(authentication, announcementId, requestDTO);

        assertNotNull(responseDTO);
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void testUpdateComment() {
        String studentId = "testStudentId";
        Long announcementId = 1L;
        Long commentId = 1L;
        CommentRequestDTO requestDTO = new CommentRequestDTO();
        requestDTO.setContent("Updated comment");

        Member member = new Member();
        member.setId(1L);
        when(authentication.getName()).thenReturn(studentId);
        when(memberRepository.findByStudentId(studentId)).thenReturn(member);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setMember(member);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentResponseDTO responseDTO = commentService.updateComment(authentication, announcementId, commentId, requestDTO);

        assertNotNull(responseDTO);
        assertEquals("Updated comment", responseDTO.getContent());
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void testDeleteComment() {
        String studentId = "testStudentId";
        Long announcementId = 1L;
        Long commentId = 1L;

        Member member = new Member();
        member.setId(1L);
        member.setUserRole(UserRole.ROLE_USER);
        when(authentication.getName()).thenReturn(studentId);
        when(memberRepository.findByStudentId(studentId)).thenReturn(member);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setMember(member);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        commentService.deleteComment(authentication, announcementId, commentId);
        verify(commentRepository).delete(comment);
    }

    @Test
    void testDeleteComment_AsAdmin() {
        String studentId = "testAdminId";
        Long announcementId = 1L;
        Long commentId = 1L;

        Member member = new Member();
        member.setId(1L);
        member.setUserRole(UserRole.ROLE_ADMIN);
        when(authentication.getName()).thenReturn(studentId);
        when(memberRepository.findByStudentId(studentId)).thenReturn(member);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setMember(new Member()); // 다른 사용자의 댓글
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        commentService.deleteComment(authentication, announcementId, commentId);

        verify(commentRepository).delete(comment);
    }
}

