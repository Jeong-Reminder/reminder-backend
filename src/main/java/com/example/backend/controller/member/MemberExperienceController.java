package com.example.backend.controller.member;

import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.member.MemberExperienceRequestDTO;
import com.example.backend.dto.member.MemberExperienceResponseDTO;
import com.example.backend.service.member.MemberExperienceService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member-experience")
public class MemberExperienceController {

    private final MemberExperienceService memberExperienceService;

    @PostMapping
    public ResponseDTO<Object> createMemberExperience(Authentication authentication,
                                                      @RequestBody MemberExperienceRequestDTO memberExperienceRequestDTO) {
        List<MemberExperienceResponseDTO> memberExperienceResponseDTOList = memberExperienceService.createMemberExperience(authentication, memberExperienceRequestDTO);

        return ResponseDTO.builder()
                .status(200)
                .data(memberExperienceResponseDTOList)
                .build();
    }

    @PostMapping("/list")
    public ResponseDTO<Object> createMemberExperienceList(Authentication authentication,
                                                          @RequestBody List<MemberExperienceRequestDTO> memberExperienceRequestDTOList) {
        List<MemberExperienceResponseDTO> memberExperienceResponseDTOList = memberExperienceService.createMemberExperienceList(authentication, memberExperienceRequestDTOList);

        return ResponseDTO.builder()
                .status(200)
                .data(memberExperienceResponseDTOList)
                .build();
    }

    @PutMapping("/{memberExperienceId}")
    public ResponseDTO<Object> updateMemberExperience(Authentication authentication,
                                                      @RequestBody MemberExperienceRequestDTO memberExperienceRequestDTO,
                                                      @PathVariable Long memberExperienceId) {
        List<MemberExperienceResponseDTO> memberExperienceResponseDTOList = memberExperienceService.updateMemberExperience(authentication, memberExperienceRequestDTO, memberExperienceId);

        return ResponseDTO.builder()
                .status(200)
                .data(memberExperienceResponseDTOList)
                .build();
    }

    @GetMapping
    public ResponseDTO<Object> getMemberExperience(Authentication authentication) {
        List<MemberExperienceResponseDTO> memberExperienceResponseDTOList = memberExperienceService.getMemberExperience(authentication);

        return ResponseDTO.builder()
                .status(200)
                .data(memberExperienceResponseDTOList)
                .build();
    }

    @GetMapping("/{memberId}")
    public ResponseDTO<Object> getMemberExperienceByMemberId(@PathVariable Long memberId) {
        List<MemberExperienceResponseDTO> memberExperienceResponseDTOList = memberExperienceService.getMemberExperienceByMemberId(memberId);

        return ResponseDTO.builder()
                .status(200)
                .data(memberExperienceResponseDTOList)
                .build();
    }
}
