package com.example.backend.controller;

import ch.qos.logback.core.model.Model;
import com.example.backend.dto.JoinRequest;
import com.example.backend.dto.LoginRequest;
import com.example.backend.dto.UserRole;
import com.example.backend.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.example.backend.model.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    @GetMapping(value = {"", "/"})
    public String home(Model model, @SessionAttribute(name = "student_Id", required = false) Long student_Id) {

        User loginUser = loginService.getLoginUserById(student_Id);

        if(loginUser != null) {

        }

        return "home";
    }

    @GetMapping("/join")
    public String joinPage(Model model) {
        return "join";
    }

    @PostMapping("/join")
    public String join(@Valid @ModelAttribute JoinRequest joinRequest, BindingResult bindingResult) {

        // loginId 중복 체크
        if(loginService.checkLoginIdDuplicate(joinRequest.getLoginId())) {
            bindingResult.addError(new FieldError("joinRequest", "loginId", "로그인 아이디가 중복됩니다."));
        }

        // password와 passwordCheck가 같은지 체크
        if(!joinRequest.getPassword().equals(joinRequest.getPasswordCheck())) {
            bindingResult.addError(new FieldError("joinRequest", "passwordCheck", "바밀번호가 일치하지 않습니다."));
        }

        if(bindingResult.hasErrors()) {
            return "join";
        }


        return "redirect:/session-login";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {

        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest loginRequest, BindingResult bindingResult,
                        HttpServletRequest httpServletRequest) {
        User user = loginService.login(loginRequest);

        // 로그인 아이디나 비밀번호가 틀린 경우 global error return
        if(user == null) {
            bindingResult.reject("loginFail", "로그인 아이디 또는 비밀번호가 틀렸습니다.");
        }

        if(bindingResult.hasErrors()) {
            return "login";
        }

        // 로그인 성공 => 세션 생성

        // 세션을 생성하기 전에 기존의 세션 파기
        httpServletRequest.getSession().invalidate();
        HttpSession session = httpServletRequest.getSession(true);  // Session이 없으면 생성
        // 세션에 userId를 넣어줌
        session.setAttribute("userId", user.getId());
        session.setMaxInactiveInterval(1800); // Session이 30분동안 유지

        sessionList.put(session.getId(), session);

        return "redirect:/session-login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);  // Session이 없으면 null return
        if(session != null) {
            sessionList.remove(session.getId());
            session.invalidate();
        }
        return "redirect:/session-login";
    }

    @GetMapping("/info")
    public String userInfo(@SessionAttribute(name = "userId", required = false) Long userId, Model model) {

        User loginUser = loginService.getLoginUserById(userId);

        if(loginUser == null) {
            return "redirect:/session-login/login";
        }


        return "info";
    }

    @GetMapping("/admin")
    public String adminPage(@SessionAttribute(name = "userId", required = false) Long userId) {
        User loginUser = loginService.getLoginUserById(userId);

        if(loginUser == null) {
            return "redirect:/session-login/login";
        }

        if(!loginUser.getRole().equals(UserRole.ADMIN)) {
            return "redirect:/session-login";
        }

        return "admin";
    }

    // 세션 리스트 확인용 코드
    public static Hashtable sessionList = new Hashtable();

    @GetMapping("/session-list")
    @ResponseBody
    public Map<String, String> sessionList() {
        Enumeration elements = sessionList.elements();
        Map<String, String> lists = new HashMap<>();
        while(elements.hasMoreElements()) {
            HttpSession session = (HttpSession)elements.nextElement();
            lists.put(session.getId(), String.valueOf(session.getAttribute("userId")));
        }
        return lists;
    }
}