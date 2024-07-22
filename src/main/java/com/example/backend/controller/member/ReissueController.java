package com.example.backend.controller.member;

import com.example.backend.dto.member.MemberResponseDTO;
import com.example.backend.jwt.JWTUtil;
import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.member.MemberExperience;
import com.example.backend.model.entity.member.MemberProfile;
import com.example.backend.model.entity.member.Refresh;
import com.example.backend.model.repository.member.MemberRepository;
import com.example.backend.model.repository.member.RefreshRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reissue")
public class ReissueController {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MemberRepository memberRepository;

    @PostMapping
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refresh")) {

                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {

            //response status code
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            //response status code
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {

            //response status code
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        boolean isExist = refreshRepository.existsById(refresh);
        if (!isExist) {

            //response body
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }


        String studentId = jwtUtil.getStudentId(refresh);
        String userRole = jwtUtil.getUserRole(refresh);
        Long memberId = jwtUtil.getMemberId(refresh);

        Member member = memberRepository.findByStudentId(studentId);
        MemberProfile memberProfile = member.getMemberProfile();
        List<MemberExperience> memberExperiences = member.getMemberExperiences();

        MemberResponseDTO memberResponseDTO = MemberResponseDTO.toResponseDTO(member, memberProfile , memberExperiences);


        //make new JWT
        String newAccess = jwtUtil.createJwt("access", studentId, userRole,  600000L, memberId);
        String newRefresh = jwtUtil.createJwt("refresh", studentId, userRole, 86400000L, memberId);

        // 기존 refresh 토큰을 삭제
        Optional<Refresh> refreshEntity = refreshRepository.findById(refresh);
        refreshEntity.ifPresent(refreshRepository::delete);

        addRefreshEntity(newRefresh);

        //response
        response.setHeader("access", newAccess);
        response.addCookie(createCookie("refresh", newRefresh));
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(memberResponseDTO));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    private void addRefreshEntity(String refresh) {

        Refresh refreshEntity = new Refresh();
        refreshEntity.setId(refresh);
        refreshEntity.setRefresh(refresh);

        refreshRepository.save(refreshEntity);
    }
}
