package com.example.backend.service.member;

import com.example.backend.dto.member.ChangePasswordRequestDTO;
import com.example.backend.dto.member.MemberMyPageResponseDTO;
import com.example.backend.dto.member.MemberRequestDTO;
import com.example.backend.dto.member.MemberResponseDTO;
import org.springframework.security.core.Authentication;

public interface MemberService {
    MemberResponseDTO signup(MemberRequestDTO requestDTO);

    MemberResponseDTO changePassword(Authentication authentication, ChangePasswordRequestDTO changePasswordRequestDTO);

    MemberMyPageResponseDTO getMemberInfo(Authentication authentication);
}
