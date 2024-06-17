package com.example.backend.controller;

import com.example.backend.dto.JoinRequestDTO;
import com.example.backend.service.JoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;

    @PostMapping("/join")
    public ResponseEntity<String> joinProcess(@RequestBody JoinRequestDTO joinRequestDTO) {
        joinService.joinProcess(joinRequestDTO);
        return ResponseEntity.ok("회원가입 성공");
    }
}