package com.example.backend.service.recruitmentteam;

import com.example.backend.dto.recruitmentteam.TeamApplicationRequestDTO;
import com.example.backend.dto.recruitmentteam.TeamApplicationResponseDTO;
import org.springframework.security.core.Authentication;

public interface TeamApplicationService {
    TeamApplicationResponseDTO createTeamApplication(Authentication authentication, TeamApplicationRequestDTO teamApplicationRequestDTO, Long recruitmentId);
}
