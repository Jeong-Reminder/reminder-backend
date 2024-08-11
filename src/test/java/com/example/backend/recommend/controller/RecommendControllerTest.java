package com.example.backend.recommend.controller;

import com.example.backend.controller.recommend.RecommendController;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.recommend.RecommendRequestDTO;
import com.example.backend.dto.recommend.RecommendResponseDTO;
import com.example.backend.service.recommend.RecommendService;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("댓글 생성 및 조회 통합")
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RecommendControllerTest {

    @Mock
    private RecommendService recommendService;

    @InjectMocks
    private RecommendController recommendController;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testToggleRecommend() {
        // given
        Long announcementId = 1L;
        RecommendRequestDTO requestDTO = new RecommendRequestDTO();
        RecommendResponseDTO responseDTO = new RecommendResponseDTO();

        when(recommendService.recommend(authentication, announcementId, requestDTO)).thenReturn(responseDTO);

        // when
        ResponseEntity<ResponseDTO<RecommendResponseDTO>> responseEntity = recommendController.toggleRecommend(authentication, announcementId, requestDTO);

        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK.value(), responseEntity.getBody().getStatus());
        assertEquals(responseDTO, responseEntity.getBody().getData());
    }

    @Test
    void testGetMyRecommends() {
        RecommendResponseDTO responseDTO = new RecommendResponseDTO();
        List<RecommendResponseDTO> responseDTOList = Collections.singletonList(responseDTO);

        when(recommendService.getMyRecommends(authentication)).thenReturn(responseDTOList);

        ResponseEntity<ResponseDTO<List<RecommendResponseDTO>>> responseEntity = recommendController.getMyRecommends(authentication);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK.value(), responseEntity.getBody().getStatus());
        assertEquals(responseDTOList, responseEntity.getBody().getData());
    }
}

