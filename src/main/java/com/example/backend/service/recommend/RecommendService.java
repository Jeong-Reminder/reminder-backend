package com.example.backend.service.recommend;


import com.example.backend.dto.recommend.RecommendRequestDTO;
import com.example.backend.dto.recommend.RecommendResponseDTO;
import org.springframework.security.core.Authentication;

public interface RecommendService {
    RecommendResponseDTO recommend(Authentication authentication,RecommendRequestDTO requestDTO);
    void cancelRecommend(Authentication authentication,Long recommendId);
}
