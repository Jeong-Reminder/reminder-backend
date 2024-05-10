package com.example.backend.service;


import com.example.backend.dto.JoinRequest;
import com.example.backend.dto.MemberRole;
import com.example.backend.model.entity.Member;
import com.example.backend.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {

    @Autowired
    private MemberRepository memberRepository;

    private MemberRole memberRole;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public void joinProcess(JoinRequest joinRequest) {

        Member data = new Member();

        data.setStudentId(joinRequest.getStudentId());
        data.setName(joinRequest.getName());
        data.setPassword(bCryptPasswordEncoder.encode(joinRequest.getPassword()));
        data.setRole(memberRole);

        memberRepository.save(data);

    }
}
