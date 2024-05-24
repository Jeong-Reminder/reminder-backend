package com.example.backend.controller;

import com.example.backend.dto.TechStackDTO;
import com.example.backend.model.entity.Member;
import com.example.backend.repository.MemberRepository;
import com.example.backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @PutMapping("/update-tech-stack/{studentId}")
    public ResponseEntity<String> techStack(@PathVariable String studentId, @RequestBody TechStackDTO techStackDTO) {
        try {
            memberService.techStack(studentId, techStackDTO);
            return ResponseEntity.ok("Tech stack updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<Member> getMember(@PathVariable String studentId) {
        try {
            Member member = memberRepository.findByStudentId(studentId)
                    .orElseThrow(() -> new IllegalArgumentException("Member not found"));
            return ResponseEntity.ok(member);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
