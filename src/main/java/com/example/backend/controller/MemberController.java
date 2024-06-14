package com.example.backend.controller;

import com.example.backend.dto.TechStackDTO;
import com.example.backend.model.entity.Member;
import com.example.backend.repository.MemberRepository;
import com.example.backend.service.MemberService;
import com.example.backend.jwt.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
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
    private final JWTUtil jwtUtil;

    @PutMapping("/update-tech-stack")
    public ResponseEntity<String> techStack(HttpServletRequest request, @RequestBody TechStackDTO techStackDTO) {
        try {
            String token = resolveToken(request);
            String studentId = jwtUtil.getStudentIdFromToken(token);
            memberService.techStack(studentId, techStackDTO);
            return ResponseEntity.ok("Tech stack updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Member> getMember(HttpServletRequest request) {
        try {
            String token = resolveToken(request);
            String studentId = jwtUtil.getStudentIdFromToken(token);
            Member member = memberRepository.findByStudentId(studentId)
                    .orElseThrow(() -> new IllegalArgumentException("Member not found"));
            return ResponseEntity.ok(member);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
