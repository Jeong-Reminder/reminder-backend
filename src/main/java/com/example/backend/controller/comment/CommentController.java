package com.example.backend.controller.comment;

import com.example.backend.dto.comment.CommentRequestDTO;
import com.example.backend.dto.comment.CommentResponseDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{announcementId}/")
    public ResponseEntity<ResponseDTO<CommentResponseDTO>> createComment(Authentication authentication,
                                                                         @PathVariable Long announcementId,
                                                                         @RequestBody CommentRequestDTO commentRequestDTO) {
        try {
            CommentResponseDTO responseDTO = commentService.createComment(authentication, announcementId, commentRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDTO.<CommentResponseDTO>builder()
                    .status(HttpStatus.CREATED.value())
                    .data(responseDTO)
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO.<CommentResponseDTO>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .error(e.getMessage())
                    .build());
        }
    }

    @PutMapping("/{announcementId}/update/{commentId}")
    public ResponseEntity<ResponseDTO<CommentResponseDTO>> updateComment(Authentication authentication,
                                                                         @PathVariable Long announcementId,
                                                                         @PathVariable Long commentId,
                                                                         @RequestBody CommentRequestDTO commentRequestDTO) {
        try {
            CommentResponseDTO responseDTO = commentService.updateComment(authentication, announcementId, commentId, commentRequestDTO);
            return ResponseEntity.ok(ResponseDTO.<CommentResponseDTO>builder()
                    .status(HttpStatus.OK.value())
                    .data(responseDTO)
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO.<CommentResponseDTO>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .error(e.getMessage())
                    .build());
        }
    }

    @DeleteMapping("/{announcementId}/delete/{commentId}")
    public ResponseEntity<ResponseDTO<Void>> deleteComment(Authentication authentication,
                                                           @PathVariable Long announcementId,
                                                           @PathVariable Long commentId) {
        try {
            commentService.deleteComment(authentication, announcementId, commentId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO.<Void>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .error(e.getMessage())
                    .build());
        }
    }
}
