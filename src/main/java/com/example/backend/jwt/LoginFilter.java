package com.example.backend.jwt;

import com.example.backend.dto.member.CustomMemberDetails;
import com.example.backend.dto.member.MemberResponseDTO;
import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.member.MemberExperience;
import com.example.backend.model.entity.member.MemberProfile;
import com.example.backend.model.entity.member.Refresh;
import com.example.backend.model.repository.member.MemberRepository;
import com.example.backend.model.repository.member.RefreshRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MemberRepository memberRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String studentId = request.getParameter("studentId");
        String password = request.getParameter("password");
        String fcmToken = request.getParameter("fcmToken");

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(studentId, password, null);

        Member member = memberRepository.findByStudentId(studentId);
        member.setFcmToken(fcmToken);
        memberRepository.save(member);

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication)
            throws IOException {

        CustomMemberDetails customMemberDetails = (CustomMemberDetails) authentication.getPrincipal();

        String studentId = customMemberDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String userRole = auth.getAuthority();

        Long memberId = customMemberDetails.getMemberId();

        Member member = memberRepository.findByStudentId(studentId);
        MemberProfile memberProfile = member.getMemberProfile();
        List<MemberExperience> memberExperiences = member.getMemberExperiences();

        MemberResponseDTO memberResponseDTO = MemberResponseDTO.toResponseDTO(member, memberProfile , memberExperiences);

        String access = jwtUtil.createJwt("access",studentId, userRole, 600000L, memberId);
        String refresh = jwtUtil.createJwt("refresh",studentId, userRole, 86400000L, memberId);

        addRefresh(refresh);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(memberResponseDTO));
        response.setHeader("access", access);
        response.addCookie(createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    private void addRefresh(String refresh) {

        Refresh refreshEntity = new Refresh();
        refreshEntity.setId(refresh);
        refreshEntity.setRefresh(refresh);

        refreshRepository.save(refreshEntity);
    }
}