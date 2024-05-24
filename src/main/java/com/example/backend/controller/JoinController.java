package com.example.backend.controller;

import com.example.backend.dto.JoinRequest;
import com.example.backend.service.JoinService;
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
    public String joinProcess(JoinRequest joinRequest) {

        joinService.joinProcess(joinRequest);

        return "ok";
    }

}