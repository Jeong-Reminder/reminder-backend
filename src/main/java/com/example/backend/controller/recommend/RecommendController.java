package com.example.backend.controller.recommend;

import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.recommend.RecommendRequestDTO;
import com.example.backend.dto.recommend.RecommendResponseDTO;
import com.example.backend.service.recommend.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recommend")
@RequiredArgsConstructor
public class RecommendController {

    private final RecommendService recommendService;

    @PostMapping("/")
    public ResponseDTO<RecommendResponseDTO> recommend(Authentication authentication, @RequestBody RecommendRequestDTO requestDTO) {
        try {
            RecommendResponseDTO responseDTO = recommendService.recommend(authentication, requestDTO);
            return ResponseDTO.<RecommendResponseDTO>builder()
                    .status(200)
                    .data(responseDTO)
                    .build();
        } catch (Exception e) {
            return ResponseDTO.<RecommendResponseDTO>builder()
                    .status(500)
                    .error(e.getMessage())
                    .build();
        }
    }

    @DeleteMapping("/cancelRecommend")
    public ResponseDTO<Void> cancelRecommend(Authentication authentication, @RequestParam Long announcementId) {
        try {
            recommendService.cancelRecommend(authentication, announcementId);
            return ResponseDTO.<Void>builder()
                    .status(200)
                    .build();
        } catch (Exception e) {
            return ResponseDTO.<Void>builder()
                    .status(500)
                    .error(e.getMessage())
                    .build();
        }
    }
}
