package com.example.backend.dto.comment;

import com.example.backend.model.entity.comment.Comment;
import lombok.*;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDTO {
    private Long id;
    private String content;
    private Long announcementId;
    private Long memberId;
    private String memberName;

    public static CommentResponseDTO toResponseDTO(Comment comment) {
        return CommentResponseDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .announcementId(comment.getAnnouncement().getId())
                .memberId(comment.getMember().getId())
                .memberName(comment.getMember().getName())
                .build();
    }

    public static CommentResponseDTO fromEntity(Comment comment) {
        return CommentResponseDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .announcementId(comment.getAnnouncement().getId())
                .memberId(comment.getMember().getId())
                .memberName(comment.getMember().getName())
                .build();
    }
}
