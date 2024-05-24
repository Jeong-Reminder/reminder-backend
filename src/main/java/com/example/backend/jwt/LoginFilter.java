package com.example.backend.jwt;

import com.example.backend.service.RefreshTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    // 로그인 시도 시 실행되는 메서드
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String studentId = request.getParameter("studentId");
        String password = request.getParameter("password");

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(studentId, password);
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        String studentId = authentication.getName();

        // 인증된 사용자의 권한을 가져옴
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // Access 및 Refresh 토큰 생성
        String access = jwtUtil.createJwt("access", studentId, role, 600000L);
        String refresh = jwtUtil.createJwt("refresh", studentId, role, 86400000L);

        // Refresh 토큰을 Redis에 저장
        refreshTokenService.saveRefreshToken(studentId, refresh, 86400000L);

        // Access 토큰을 응답 헤더에 설정
        response.setHeader("access", access);

        // Refresh 토큰을 쿠키에 설정
        response.addCookie(createCookie("refresh", refresh));
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("HTTP Status 401 - " + failed.getMessage());
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);
        return cookie;
    }
}
