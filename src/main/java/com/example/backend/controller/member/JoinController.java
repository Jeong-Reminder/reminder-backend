package com.example.backend.controller.member;

import com.example.backend.dto.member.JoinRequestDTO;
import com.example.backend.service.member.JoinService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class JoinController {

    private final JoinService joinService;

    public JoinController(JoinService joinService) {

        this.joinService = joinService;
    }


    @PostMapping("/join")
    public String joinProcess(JoinRequestDTO joinRequestDTO) {

        joinService.joinProcess(joinRequestDTO);

        return "ok";
    }
}