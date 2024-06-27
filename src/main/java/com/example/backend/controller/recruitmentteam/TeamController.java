package com.example.backend.controller.recruitmentteam;

import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.recruitmentteam.TeamRequestDTO;
import com.example.backend.dto.recruitmentteam.TeamResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.backend.service.recruitmentteam.TeamService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/team")
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseDTO<Object> createTeam(Authentication authentication, @RequestBody TeamRequestDTO teamRequestDTO) {
        TeamResponseDTO teamResponseDTO = teamService.createTeam(authentication, teamRequestDTO);

        return ResponseDTO.builder()
                .status(200)
                .data(teamResponseDTO)
                .build();
    }

    @GetMapping("{teamId}")
    public ResponseDTO<Object> getTeam(Authentication authentication, @PathVariable Long teamId) {
        TeamResponseDTO teamResponseDTO = teamService.getTeam(authentication, teamId);

        return ResponseDTO.builder()
                .status(200)
                .data(teamResponseDTO)
                .build();
    }
}
