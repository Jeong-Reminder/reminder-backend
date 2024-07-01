package com.example.backend.service.comment.impl;

import com.example.backend.dto.comment.CommentRequestDTO;
import com.example.backend.dto.comment.CommentResponseDTO;
import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.comment.Comment;
import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.member.UserRole;
import com.example.backend.model.repository.announcement.AnnouncementRepository;
import com.example.backend.model.repository.comment.CommentRepository;
import com.example.backend.model.repository.member.MemberRepository;
import com.example.backend.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentImplService implements CommentService {

    private final CommentRepository commentRepository;
    private final AnnouncementRepository announcementRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public CommentResponseDTO createComment(Authentication authentication, Long announcementId, CommentRequestDTO commentRequestDTO) {
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);
        Long managerId = member.getId();
        Member manager = memberRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다: " + managerId));

        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 아이디를 찾지 못했습니다"));

        if (announcement.getVotes() == null || announcement.getVotes().isEmpty()) {
            throw new IllegalArgumentException("댓글을 달 수 없습니다. 투표가 없는 공지사항입니다.");
        }

        Comment comment = Comment.builder()
                .content(commentRequestDTO.getContent())
                .announcement(announcement)
                .member(member)
                .build();

        Comment savedComment = commentRepository.save(comment);
        return CommentResponseDTO.toResponseDTO(savedComment);
    }

    @Override
    @Transactional
    public CommentResponseDTO updateComment(Authentication authentication, Long announcementId, Long commentId, CommentRequestDTO commentRequestDTO) {
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);
        Long managerId = member.getId();
        Member manager = memberRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다: " + managerId));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글 아이디를 찾지못했습니다"));

        if (!comment.getMember().getId().equals(managerId)) {
            throw new IllegalArgumentException("댓글을 수정할 권한이 없습니다.");
        }

        comment.setContent(commentRequestDTO.getContent());
        Comment updatedComment = commentRepository.save(comment);
        return CommentResponseDTO.toResponseDTO(updatedComment);
    }

    @Override
    @Transactional
    public void deleteComment(Authentication authentication, Long announcementId, Long commentId) {
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);
        Long managerId = member.getId();
        Member manager = memberRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다: " + managerId));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글 아이디를 찾지 못 했습니다."));

        if (!comment.getMember().getId().equals(managerId) && member.getUserRole() != UserRole.ROLE_ADMIN) {
            throw new IllegalArgumentException("댓글을 삭제할 권한이 없습니다.");
        }
        commentRepository.delete(comment);
    }
}
