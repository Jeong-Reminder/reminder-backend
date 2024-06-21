package com.example.backend.service.member;

import com.example.backend.dto.member.JoinRequestDTO;
import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.member.UserRole;
import com.example.backend.model.repository.member.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JoinService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    public void joinProcess(JoinRequestDTO joinRequestDTO) {
        String memberId = joinRequestDTO.getStudentId();
        String password = joinRequestDTO.getPassword();
        UserRole userRole = joinRequestDTO.getUserRole();

        // 이미 존재하는 회원인지 확인
        if (memberRepository.existsByStudentId(memberId)) {
            throw new IllegalArgumentException("이미 가입된 학생 ID입니다: " + memberId);
        }

        Member data = new Member();
        data.setMemberId(memberId);
        data.setPassword(bCryptPasswordEncoder.encode(password));
        data.setName("Default Name"); // 기본 값 설정 또는 입력 받도록 변경
        data.setLevel(1);             // 기본 값 설정 또는 입력 받도록 변경
        data.setStatus("Enrolled");   // 기본 값 설정 또는 입력 받도록 변경
        data.setUserRole(userRole);   // 사용자 역할 설정

        memberRepository.save(data);
    }
}
