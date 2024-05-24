package com.example.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String testGet() {
        return "GET 요청에 성공했습니다!";
    }

    @PostMapping("/test")
    public String testPost() {
        return "POST 요청에 성공했습니다!";
    }

    @PutMapping("/test")
    public String testPut() {
        return "PUT 요청에 성공했습니다!";
    }

    @DeleteMapping("/test")
    public String testDelete() {
        return "DELETE 요청에 성공했습니다!";
    }
}
