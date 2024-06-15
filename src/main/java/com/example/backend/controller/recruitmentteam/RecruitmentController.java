package com.example.backend.controller.recruitmentteam;

import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.recruitmentteam.RecruitmentRequestDTO;
import com.example.backend.dto.recruitmentteam.RecruitmentResponseDTO;
import com.example.backend.service.recruitmentteam.RecruitmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/recruitment")
public class RecruitmentController {

    private final RecruitmentService recruitmentService;

    @PostMapping
    public ResponseDTO<RecruitmentResponseDTO> createRecruitment(Authentication authentication, @RequestBody RecruitmentRequestDTO recruitmentRequestDTO) {
        RecruitmentResponseDTO recruitmentResponseDTO = recruitmentService.createRecruitment(authentication, recruitmentRequestDTO);

        return new ResponseDTO<>(200, recruitmentResponseDTO);
    }

}
