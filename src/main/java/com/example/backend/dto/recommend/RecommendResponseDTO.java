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
    private Long id;
    private Long announcementId;
    private Long memberId;
    private boolean status;

    public static RecommendResponseDTO toResponseDTO(Recommend recommend) {
        RecommendResponseDTO responseDTO = new RecommendResponseDTO();
        responseDTO.setId(recommend.getId());
        responseDTO.setAnnouncementId(recommend.getAnnouncement().getId());
        responseDTO.setMemberId(recommend.getMember().getId());
        responseDTO.setStatus(recommend.isStatus());
        return responseDTO;
    }
}
