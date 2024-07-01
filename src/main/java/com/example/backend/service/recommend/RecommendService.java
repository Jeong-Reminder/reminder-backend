package com.example.backend.service.recommend;

import com.example.backend.dto.recommend.RecommendRequestDTO;
import com.example.backend.dto.recommend.RecommendResponseDTO;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface RecommendService {
    RecommendResponseDTO recommend(Authentication authentication, Long announcementId, RecommendRequestDTO requestDTO);
    void cancelRecommend(Authentication authentication, Long recommendId);
    List<RecommendResponseDTO> getMyRecommends(Authentication authentication);
}
