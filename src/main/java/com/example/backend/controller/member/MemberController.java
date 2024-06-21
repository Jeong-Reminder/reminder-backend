package com.example.backend.controller.member;

import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.member.ChangePasswordRequestDTO;
import com.example.backend.dto.member.MemberRequestDTO;
import com.example.backend.dto.member.MemberResponseDTO;
import com.example.backend.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseDTO<Object> signup(@RequestBody MemberRequestDTO requestDTO) {
        MemberResponseDTO memberResponseDTO = memberService.signup(requestDTO);

        return ResponseDTO.builder()
                .status(200)
                .data(memberResponseDTO)
                .build();
    }

    @PutMapping("/changePassword")
    public ResponseDTO<Object> changePassword(Authentication authentication, @RequestBody ChangePasswordRequestDTO changePasswordRequestDTO) {
        MemberResponseDTO memberResponseDTO = memberService.changePassword(authentication, changePasswordRequestDTO);

        return ResponseDTO.builder()
                .status(200)
                .data(memberResponseDTO)
                .build();
    }
}
