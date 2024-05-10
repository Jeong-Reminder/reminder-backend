package com.example.backend.controller;

import com.example.backend.dto.JoinRequest;
import com.example.backend.service.JoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class JoinController {

    @Autowired
    private JoinService joinService;

    @GetMapping("/join")
    public String join() {

        return "join";
    }

    @PostMapping("/joinProc")
    public String joinProcess(JoinRequest joinRequest) {

        System.out.println(joinRequest.getName());

        joinService.joinProcess(joinRequest);

        return "redirect:/login";
    }

}