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


    @PostMapping("/create")
    public ResponseEntity<ResponseDTO<CommentResponseDTO>> createComment(Authentication authentication,
                                                                         @RequestBody CommentRequestDTO commentRequestDTO) {
        try {
            CommentResponseDTO responseDTO = commentService.createComment(authentication, commentRequestDTO);
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

    @PutMapping("/update/{commentId}")
    public ResponseEntity<ResponseDTO<CommentResponseDTO>> updateComment(Authentication authentication,
                                                                         @PathVariable Long commentId,
                                                                         @RequestBody CommentRequestDTO commentRequestDTO) {
        try {
            CommentResponseDTO responseDTO = commentService.updateComment(authentication, commentId, commentRequestDTO);
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

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<ResponseDTO<Void>> deleteComment(Authentication authentication,
                                                           @PathVariable Long commentId) {
        try {
            commentService.deleteComment(authentication, commentId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO.<Void>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .error(e.getMessage())
                    .build());
        }
    }
}
