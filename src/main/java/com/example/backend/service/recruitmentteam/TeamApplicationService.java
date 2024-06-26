package com.example.backend.service.recruitmentteam;

import com.example.backend.dto.recruitmentteam.TeamApplicationRequestDTO;
import com.example.backend.dto.recruitmentteam.TeamApplicationResponseDTO;
import org.springframework.security.core.Authentication;

public interface TeamApplicationService {
    TeamApplicationResponseDTO createTeamApplication(Authentication authentication, TeamApplicationRequestDTO teamApplicationRequestDTO, Long recruitmentId);

    TeamApplicationResponseDTO updateTeamApplication(Authentication authentication, TeamApplicationRequestDTO teamApplicationRequestDTO, Long teamApplicationId);

    void deleteTeamApplication(Authentication authentication, Long teamApplicationId);
}
