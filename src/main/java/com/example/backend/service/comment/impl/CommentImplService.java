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
    public CommentResponseDTO createComment(Authentication authentication, CommentRequestDTO commentRequestDTO) {
        Member member = memberRepository.findById(Long.valueOf(authentication.getName()))
                .orElseThrow(() -> new IllegalArgumentException("ID을 찾지 못했습니다."));

        Announcement announcement = announcementRepository.findById(commentRequestDTO.getAnnouncementId())
                .orElseThrow(() -> new IllegalArgumentException("게시글 아이디를 찾지못했습니다"));

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
    public CommentResponseDTO updateComment(Authentication authentication, Long commentId, CommentRequestDTO commentRequestDTO) {
        Member member = memberRepository.findById(Long.valueOf(authentication.getName()))
                .orElseThrow(() -> new IllegalArgumentException("ID을 찾지 못했습니다."));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글 아이디를 찾지못했습니다"));

        if (!comment.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("댓글을 수정할 권한이 없습니다.");
        }

        comment.setContent(commentRequestDTO.getContent());
        Comment updatedComment = commentRepository.save(comment);
        return CommentResponseDTO.toResponseDTO(updatedComment);
    }

    @Override
    @Transactional
    public void deleteComment(Authentication authentication, Long commentId) {
        Member member = memberRepository.findById(Long.valueOf(authentication.getName()))
                .orElseThrow(() -> new IllegalArgumentException("ID을 찾지 못했습니다."));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글 아이디를 찾지 못 했습니다."));

        if (!comment.getMember().getId().equals(member.getId()) && member.getUserRole() != UserRole.ROLE_ADMIN) {
            throw new IllegalArgumentException("댓글을 삭제할 권한이 없습니다.");
        }
        commentRepository.delete(comment);
    }
}
