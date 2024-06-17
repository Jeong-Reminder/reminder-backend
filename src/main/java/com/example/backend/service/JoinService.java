package com.example.backend.service;

import com.example.backend.dto.JoinRequestDTO;
import com.example.backend.jwt.JWTUtil;
import com.example.backend.model.entity.Member;
import com.example.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTUtil jwtUtil;

    public void joinProcess(JoinRequestDTO joinRequestDTO) {

        String studentId = joinRequestDTO.getStudentId();
        Boolean isExist = memberRepository.existsByStudentId(studentId);

        if (isExist) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }

        Member member = joinRequestDTO.toMember(bCryptPasswordEncoder);
        memberRepository.save(member);
    }
    public String login(JoinRequestDTO requestDto) {
        Member member = memberRepository.findByStudentId(requestDto.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid studentId or password"));

        if (!bCryptPasswordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("Invalid studentId or password");
        }

        // JWT 토큰 생성
        return jwtUtil.createJwt(member.getStudentId(), member.getUserRole().name());
    }
}
