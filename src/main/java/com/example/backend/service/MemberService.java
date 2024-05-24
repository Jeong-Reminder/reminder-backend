package com.example.backend.service;

import com.example.backend.dto.TechStackDTO;
import com.example.backend.model.entity.Member;
import com.example.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public void techStack(String studentId, TechStackDTO techStackDTO) {
        Member member = memberRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        member.setGithubLink(techStackDTO.getGithubLink());
        member.setDevelopmentField(techStackDTO.getDevelopmentField());
        member.setDevelopmentTool(techStackDTO.getDevelopmentTool());

        memberRepository.save(member);
    }
}
