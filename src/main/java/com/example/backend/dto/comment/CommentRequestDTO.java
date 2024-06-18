package com.example.backend.dto.comment;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDTO {
    private String content;
    private Long announcementId;
    private Long memberId;
}
