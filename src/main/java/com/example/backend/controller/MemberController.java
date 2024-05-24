package com.example.backend.controller;

import com.example.backend.dto.TechStackDTO;
import com.example.backend.model.entity.Member;
import com.example.backend.repository.MemberRepository;
import com.example.backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @PutMapping("/update-tech-stack/{studentId}")
    public String updateTechStack(@PathVariable String studentId, @RequestBody TechStackDTO techStackDTO) {
        memberService.techStack(studentId, techStackDTO);
        return "Tech stack updated successfully";
    }

    @GetMapping("/{studentId}")
    public Member getMember(@PathVariable String studentId) {
        return memberRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
    }
}
