package com.example.backend.service.recruitmentteam;

import com.example.backend.dto.recruitmentteam.AcceptMemberRequestDTO;
import com.example.backend.dto.recruitmentteam.AcceptMemberResponseDTO;
import org.springframework.security.core.Authentication;

public interface AcceptMemberService {
    AcceptMemberResponseDTO acceptMember(Authentication authentication, boolean accept, AcceptMemberRequestDTO acceptMemberRequestDTO);
}
