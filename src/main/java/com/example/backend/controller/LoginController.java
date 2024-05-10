package com.example.backend.controller;

import com.example.backend.dto.LoginRequest;
import com.example.backend.dto.MemberRole;
import com.example.backend.model.entity.Member;
import com.example.backend.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // 로그인 페이지로 이동한다.
    }


    @PostMapping("/login")
    public String loginBySession(@RequestBody LoginRequest loginRequest, HttpServletRequest request){

        Member loginMember=loginService.login(loginRequest);
        log.info("login? {}",loginMember);
        if(loginMember==null){
            return "오류 발생";
        }

        HttpSession session=request.getSession();//세션이 있으면 있는 세션 반환, 없으면 신규 생성 default가 true이고 true일 떄
        //request.getSession(false):세션이 없으면 새로운 세션 생성 X, 세션이 있으면 반환
        session.setAttribute("memberId",loginMember.getId());//하나의 세션에 여러 값을 저장할 수 있다.,session에 loginMember라는 속성 추가
        return "로그인 성공";
    }

    @PostMapping("/logout")
    public String logoutBySession(HttpServletRequest request){
        HttpSession session=request.getSession();
        if(session!=null){
            session.invalidate();//세션을 제거한다.
        }
        return "로그아웃 완료";
    }

    @GetMapping("/admin")
    public String adminPage(@SessionAttribute(name = "memberId", required = false) Long memberId) {
//        model.addAttribute("loginType", "session-login");
//        model.addAttribute("pageName", "세션 로그인");

        Member loginMember = loginService.getLoginMemberById(memberId);

        if(loginMember == null) {
            return "redirect:/login";
        }

//        if(!loginMember.getRole().equals(MemberRole.ADMIN)) {
//            return "main";
//        }

        return "admin";
    }
}