package com.example.backend.controller.recruitmentteam;

import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.ResponseListDTO;
import com.example.backend.dto.recruitmentteam.RecruitmentRequestDTO;
import com.example.backend.dto.recruitmentteam.RecruitmentResponseDTO;
import com.example.backend.service.recruitmentteam.RecruitmentService;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recruitment")
public class RecruitmentController {

    private final RecruitmentService recruitmentService;

    @PostMapping
    public ResponseDTO<Object> createRecruitment(Authentication authentication, @RequestBody RecruitmentRequestDTO recruitmentRequestDTO) {
        RecruitmentResponseDTO recruitmentResponseDTO = recruitmentService.createRecruitment(authentication, recruitmentRequestDTO);

        return ResponseDTO.builder()
                .status(201)
                .data(recruitmentResponseDTO)
                .build();
    }

    @PutMapping("/{recruitmentId}")
    public ResponseDTO<Object> updateRecruitment(Authentication authentication, @RequestBody RecruitmentRequestDTO recruitmentRequestDTO,
                                                                 @PathVariable Long recruitmentId) {
        RecruitmentResponseDTO recruitmentResponseDTO = recruitmentService.updateRecruitment(authentication, recruitmentRequestDTO, recruitmentId);

        return ResponseDTO.builder()
                .status(200)
                .data(recruitmentResponseDTO)
                .build();
    }

    @GetMapping("/{recruitmentId}")
    public ResponseDTO<Object> getRecruitment(@PathVariable Long recruitmentId) {
        RecruitmentResponseDTO recruitmentResponseDTO = recruitmentService.getRecruitment(recruitmentId);

        return ResponseDTO.builder()
                .status(200)
                .data(recruitmentResponseDTO)
                .build();
    }

    @GetMapping("/{announcementId}")
    public ResponseListDTO<Object> getRecruitmentByAnnouncementId(@PathVariable Long announcementId) {
        List<RecruitmentResponseDTO> recruitmentResponseDTOList = recruitmentService.getRecruitmentByAnnouncementId(announcementId);

        return ResponseListDTO.builder()
                .status(200)
                .data(Collections.singletonList(recruitmentResponseDTOList))
                .build();
    }

    @GetMapping("/my")
    public ResponseListDTO<Object> getMyRecruitment(Authentication authentication) {
        List<RecruitmentResponseDTO> recruitmentResponseDTOList = recruitmentService.getMyRecruitment(authentication);

        return ResponseListDTO.builder()
                .status(200)
                .data(Collections.singletonList(recruitmentResponseDTOList))
                .build();
    }

    @GetMapping("/active")
    public ResponseListDTO<Object> getActiveRecruitment() {
        List<RecruitmentResponseDTO> recruitmentResponseDTOList = recruitmentService.getActiveRecruitment();

        return ResponseListDTO.builder()
                .status(200)
                .data(Collections.singletonList(recruitmentResponseDTOList))
                .build();
    }

    @GetMapping
    public ResponseListDTO<Object> getAllRecruitment() {
        List<RecruitmentResponseDTO> recruitmentResponseDTOList = recruitmentService.getAllRecruitment();

        return ResponseListDTO.builder()
                .status(200)
                .data(Collections.singletonList(recruitmentResponseDTOList))
                .build();
    }

    @DeleteMapping("/{recruitmentId}")
    public ResponseDTO<Object> deleteRecruitment(Authentication authentication, @PathVariable Long recruitmentId) {
        recruitmentService.deleteRecruitment(authentication, recruitmentId);

        return ResponseDTO.builder()
                .status(200)
                .data(null)
                .build();
    }
}
