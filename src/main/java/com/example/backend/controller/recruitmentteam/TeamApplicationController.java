package com.example.backend.controller.recruitmentteam;

import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.recruitmentteam.TeamApplicationRequestDTO;
import com.example.backend.dto.recruitmentteam.TeamApplicationResponseDTO;
import com.example.backend.service.recruitmentteam.TeamApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recruitment/team-application")
public class TeamApplicationController {

    private final TeamApplicationService teamApplicationService;

    @PostMapping("/{recruitmentId}")
    public ResponseDTO<Object> createTeamApplication(Authentication authentication,
                                                     @RequestBody TeamApplicationRequestDTO teamApplicationRequestDTO,
                                                     @PathVariable Long recruitmentId) {
        TeamApplicationResponseDTO teamApplicationResponseDTO = teamApplicationService.createTeamApplication(authentication, teamApplicationRequestDTO, recruitmentId);

        return ResponseDTO.builder()
                .status(200)
                .data(teamApplicationResponseDTO)
                .build();
    }

    @PutMapping("/{teamApplicationId}")
    public ResponseDTO<Object> updateTeamApplication(Authentication authentication,
                                                     @RequestBody TeamApplicationRequestDTO teamApplicationRequestDTO,
                                                     @PathVariable Long teamApplicationId) {
        TeamApplicationResponseDTO teamApplicationResponseDTO = teamApplicationService.updateTeamApplication(authentication, teamApplicationRequestDTO, teamApplicationId);

        return ResponseDTO.builder()
                .status(200)
                .data(teamApplicationResponseDTO)
                .build();
    }

}
