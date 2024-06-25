package com.example.backend.service.member;

import com.example.backend.dto.member.MemberProfileRequestDTO;
import com.example.backend.dto.member.MemberProfileResponseDTO;
import org.springframework.security.core.Authentication;

public interface MemberProfileService {
    MemberProfileResponseDTO createProfile(Authentication authentication, MemberProfileRequestDTO memberProfileRequestDTO);

    MemberProfileResponseDTO updateProfile(Authentication authentication, MemberProfileRequestDTO memberProfileRequestDTO);

    MemberProfileResponseDTO getMemberProfile(Authentication authentication);

    MemberProfileResponseDTO getMemberProfileByMemberId(Long memberId);
}
