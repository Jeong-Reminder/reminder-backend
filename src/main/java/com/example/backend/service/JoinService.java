package com.example.backend.service;

import com.example.backend.dto.JoinRequest;
import com.example.backend.model.entity.Member;
import com.example.backend.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void joinProcess(JoinRequest joinRequest) {

        String studentId = joinRequest.getStudentId();
        String password = joinRequest.getPassword();

        Boolean isExist = memberRepository.existsByStudentId(studentId);

        if (isExist) {
            return;
        }

        // 미리 저장된 회원 정보를 조회
        Member preSavedMember = memberRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Pre-saved member not found"));

        preSavedMember.setPassword(bCryptPasswordEncoder.encode(password));
        preSavedMember.setRole(joinRequest.isAdmin() ? "ROLE_ADMIN" : "ROLE_USER"); // 역할 설정
        memberRepository.save(preSavedMember);
    }
}
