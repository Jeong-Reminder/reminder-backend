package com.example.backend.service.comment;

import com.example.backend.dto.comment.CommentRequestDTO;
import com.example.backend.dto.comment.CommentResponseDTO;
import org.springframework.security.core.Authentication;

public interface CommentService {
    CommentResponseDTO createComment(Authentication authentication,CommentRequestDTO commentRequestDTO);
    CommentResponseDTO updateComment(Authentication authentication,Long commentId, CommentRequestDTO commentRequestDTO);
    void deleteComment(Authentication authentication,Long commentId);
}
