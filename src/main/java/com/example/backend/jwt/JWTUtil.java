package com.example.backend.jwt;

import com.example.backend.dto.CustomUserDetails;
import com.example.backend.model.entity.Member;
import com.example.backend.model.entity.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;

@Component
public class JWTUtil {

    private final SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String getStudentIdFromToken(String token) {
        return getClaims(token).getSubject();
    }

    public String getStudentId(String token) {
        return getClaims(token).getSubject();
    }

    public UserRole getRole(String token) {
        String role = getClaims(token).get("role", String.class);
        return UserRole.valueOf(role);
    }

    public boolean isExpired(String token) {
        try {
            return getClaims(token).getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        String studentId = claims.getSubject();
        UserRole role = getRole(token);
        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.name());

        Member member = new Member();
        member.setStudentId(studentId);
        member.setUserRole(role);

        CustomUserDetails customUserDetails = new CustomUserDetails(member);
        return new UsernamePasswordAuthenticationToken(customUserDetails, token, Collections.singleton(grantedAuthority));
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String createJwt(String category, String studentId, String role, Long expiredMs) {
        return Jwts.builder()
                .setSubject(studentId)
                .claim("category", category)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }
}
