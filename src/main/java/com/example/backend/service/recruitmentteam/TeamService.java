package com.example.backend.service.recruitmentteam;

import com.example.backend.dto.recruitmentteam.TeamRequestDTO;
import com.example.backend.dto.recruitmentteam.TeamResponseDTO;
import org.springframework.security.core.Authentication;

public interface TeamService {
    TeamResponseDTO createTeam(Authentication authentication, TeamRequestDTO teamRequestDTO);
}
