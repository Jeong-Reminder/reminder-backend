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
        data.setRole("ROLE_ADMIN");

        memberRepository.save(data);
    }
}
