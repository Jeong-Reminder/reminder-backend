package com.example.backend.jwt;

import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Component;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;

@Component
public class JWTUtil {

    private SecretKey secretKey;

    public JWTUtil(@Value("${jwt.secret}")String secret) {


        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public Long getMemberId(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("memberId", Long.class);
    }

    public String getStudentId(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("studentId", String.class);
    }

    public String getUserRole(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("userRole", String.class);
    }

    public Boolean isExpired(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public String createJwt(String category, String studentId, String userRole, Long expiredMs, Long memberId) {

        return Jwts.builder()
                .claim("category", category)
                .claim("memberId", memberId)
                .claim("studentId", studentId)
                .claim("userRole", userRole)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs*1000L))
                .signWith(secretKey)
                .compact();
    }

    public String getCategory(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
    }
}