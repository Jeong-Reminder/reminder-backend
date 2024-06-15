package com.example.backend.service.recruitmentteam;

import com.example.backend.dto.recruitmentteam.RecruitmentRequestDTO;
import com.example.backend.dto.recruitmentteam.RecruitmentResponseDTO;
import java.util.List;
import org.springframework.security.core.Authentication;

public interface RecruitmentService {
    RecruitmentResponseDTO createRecruitment(Authentication authentication, RecruitmentRequestDTO recruitmentRequestDTO);

    RecruitmentResponseDTO updateRecruitment(Authentication authentication, RecruitmentRequestDTO recruitmentRequestDTO, Long recruitmentId);

    RecruitmentResponseDTO getRecruitment(Long recruitmentId);

    List<RecruitmentResponseDTO> getRecruitmentByAnnouncementId(Long announcementId);
}
