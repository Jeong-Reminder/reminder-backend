package com.example.backend.controller.recommend;

import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.recommend.RecommendRequestDTO;
import com.example.backend.dto.recommend.RecommendResponseDTO;
import com.example.backend.service.recommend.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recommend")
@RequiredArgsConstructor
public class RecommendController {

    private final RecommendService recommendService;

    @PostMapping("/{announcementId}/toggle")
    public ResponseEntity<ResponseDTO<RecommendResponseDTO>> toggleRecommend(Authentication authentication, @PathVariable Long announcementId, @RequestBody RecommendRequestDTO recommendRequestDTO) {
        try {
            RecommendResponseDTO responseDTO = recommendService.recommend(authentication, announcementId, recommendRequestDTO);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ResponseDTO.<RecommendResponseDTO>builder()
                            .status(HttpStatus.OK.value())
                            .data(responseDTO)
                            .build()
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ResponseDTO.<RecommendResponseDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .error(e.getMessage())
                            .build()
            );
        }
    }

    @GetMapping("/my-recommends")
    public ResponseEntity<ResponseDTO<List<RecommendResponseDTO>>> getMyRecommends(Authentication authentication) {
        List<RecommendResponseDTO> responseDTOs = recommendService.getMyRecommends(authentication);
        return ResponseEntity.ok(
                ResponseDTO.<List<RecommendResponseDTO>>builder()
                        .status(HttpStatus.OK.value())
                        .data(responseDTOs)
                        .build()
        );
    }
}
