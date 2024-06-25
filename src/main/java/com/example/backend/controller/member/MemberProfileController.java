package com.example.backend.controller.member;

import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.member.MemberProfileRequestDTO;
import com.example.backend.dto.member.MemberProfileResponseDTO;
import com.example.backend.service.member.MemberProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member-profile")
public class MemberProfileController {

    private final MemberProfileService memberProfileService;

    @PostMapping
    public ResponseDTO<Object> createProfile(Authentication authentication, @RequestBody MemberProfileRequestDTO memberProfileRequestDTO) {

        MemberProfileResponseDTO memberProfileResponseDTO = memberProfileService.createProfile(authentication, memberProfileRequestDTO);

        return ResponseDTO.builder()
                .status(200)
                .data(memberProfileResponseDTO)
                .build();
    }

    @PutMapping
    public ResponseDTO<Object> updateProfile(Authentication authentication, @RequestBody MemberProfileRequestDTO memberProfileRequestDTO) {

        MemberProfileResponseDTO memberProfileResponseDTO = memberProfileService.updateProfile(authentication, memberProfileRequestDTO);

        return ResponseDTO.builder()
                .status(200)
                .data(memberProfileResponseDTO)
                .build();
    }
}
