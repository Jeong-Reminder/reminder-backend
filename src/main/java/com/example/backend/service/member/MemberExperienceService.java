package com.example.backend.service.member;

import com.example.backend.dto.member.MemberExperienceRequestDTO;
import com.example.backend.dto.member.MemberExperienceResponseDTO;
import java.util.List;
import org.springframework.security.core.Authentication;

public interface MemberExperienceService {
    List<MemberExperienceResponseDTO> createMemberExperience(Authentication authentication, MemberExperienceRequestDTO memberExperienceRequestDTO);

    List<MemberExperienceResponseDTO> createMemberExperienceList(Authentication authentication, List<MemberExperienceRequestDTO> memberExperienceRequestDTOList);

    List<MemberExperienceResponseDTO> updateMemberExperience(Authentication authentication, MemberExperienceRequestDTO memberExperienceRequestDTO, Long memberExperienceId);
}
