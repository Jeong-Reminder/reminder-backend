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


        Member data = new Member();
        data.setStudentId(studentId);
        data.setPassword(bCryptPasswordEncoder.encode(password));
        data.setName("Default Name"); // 기본 값 설정 또는 입력 받도록 변경
        data.setLevel(1);             // 기본 값 설정 또는 입력 받도록 변경
        data.setStatus("Enrolled");   // 기본 값 설정 또는 입력 받도록 변경
        data.setRole("ROLE_USER");    // 기본 사용자 역할 설정

        memberRepository.save(data);

//        // 미리 저장된 회원 정보를 조회
//        Member preSavedMember = memberRepository.findByStudentId(studentId)
//                .orElseThrow(() -> new IllegalArgumentException("Pre-saved member not found"));
//
//        preSavedMember.setPassword(bCryptPasswordEncoder.encode(password));
//        preSavedMember.setRole(joinRequest.isAdmin() ? "ROLE_ADMIN" : "ROLE_USER"); // 역할 설정
//        memberRepository.save(preSavedMember);
    }
}
