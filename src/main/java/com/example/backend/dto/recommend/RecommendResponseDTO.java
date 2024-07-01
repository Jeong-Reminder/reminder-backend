package com.example.backend.dto.recommend;

import com.example.backend.model.entity.recommend.Recommend;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class RecommendResponseDTO {
    private Long recommendId;
    private Long announcementId;
    private Long memberId;
    private boolean status;

    public static RecommendResponseDTO toResponseDTO(Recommend recommend) {
        return RecommendResponseDTO.builder()
                .recommendId(recommend.getId())
                .announcementId(recommend.getAnnouncement().getId())
                .memberId(recommend.getMember().getId())
                .status(recommend.isStatus())
                .build();
    }
}
