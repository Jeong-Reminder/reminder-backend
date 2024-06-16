package com.example.backend.controller.recruitmentteam;

import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.ResponseListDTO;
import com.example.backend.dto.recruitmentteam.RecruitmentRequestDTO;
import com.example.backend.dto.recruitmentteam.RecruitmentResponseDTO;
import com.example.backend.service.recruitmentteam.RecruitmentService;
import java.util.List;
import lombok.Getter;
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
    public ResponseDTO<RecruitmentResponseDTO> createRecruitment(Authentication authentication, @RequestBody RecruitmentRequestDTO recruitmentRequestDTO) {
        RecruitmentResponseDTO recruitmentResponseDTO = recruitmentService.createRecruitment(authentication, recruitmentRequestDTO);

        return new ResponseDTO<>(200, recruitmentResponseDTO);
    }

    @PutMapping("/{recruitmentId}")
    public ResponseDTO<RecruitmentResponseDTO> updateRecruitment(Authentication authentication, @RequestBody RecruitmentRequestDTO recruitmentRequestDTO,
                                                                 @PathVariable Long recruitmentId) {
        RecruitmentResponseDTO recruitmentResponseDTO = recruitmentService.updateRecruitment(authentication, recruitmentRequestDTO, recruitmentId);

        return new ResponseDTO<>(200, recruitmentResponseDTO);
    }

    @GetMapping("/{recruitmentId}")
    public ResponseDTO<RecruitmentResponseDTO> getRecruitment(@PathVariable Long recruitmentId) {
        RecruitmentResponseDTO recruitmentResponseDTO = recruitmentService.getRecruitment(recruitmentId);

        return new ResponseDTO<>(200, recruitmentResponseDTO);
    }

    @GetMapping("/{announcementId}")
    public ResponseListDTO<List<RecruitmentResponseDTO>> getRecruitmentByAnnouncementId(@PathVariable Long announcementId) {
        List<RecruitmentResponseDTO> recruitmentResponseDTOList = recruitmentService.getRecruitmentByAnnouncementId(announcementId);

        return new ResponseListDTO<>(200, recruitmentResponseDTOList);
    }

    @GetMapping("/my")
    public ResponseListDTO<List<RecruitmentResponseDTO>> getMyRecruitment(Authentication authentication) {
        List<RecruitmentResponseDTO> recruitmentResponseDTOList = recruitmentService.getMyRecruitment(authentication);

        return new ResponseListDTO<>(200, recruitmentResponseDTOList);
    }

    @GetMapping("/active")
    public ResponseListDTO<List<RecruitmentResponseDTO>> getActiveRecruitment() {
        List<RecruitmentResponseDTO> recruitmentResponseDTOList = recruitmentService.getActiveRecruitment();

        return new ResponseListDTO<>(200, recruitmentResponseDTOList);
    }

    @GetMapping
    public ResponseListDTO<List<RecruitmentResponseDTO>> getAllRecruitment() {
        List<RecruitmentResponseDTO> recruitmentResponseDTOList = recruitmentService.getAllRecruitment();

        return new ResponseListDTO<>(200, recruitmentResponseDTOList);
    }

    @DeleteMapping("/{recruitmentId}")
    public ResponseDTO<String> deleteRecruitment(Authentication authentication, @PathVariable Long recruitmentId) {
        recruitmentService.deleteRecruitment(authentication, recruitmentId);

        return new ResponseDTO<>(200, "삭제되었습니다.");
    }
}
