package com.bitcamp.todo.security;

import com.bitcamp.todo.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
public class TokenProvider {
    private static final String SECRET_KEY =
            "ldawldlaLDLADL231213ldwaloWdasdawdwadWDADAW31daw231232";
    Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    /**
     * 사용자 정보를 받아서 JWT 토큰 생성
     */
    public String create(UserEntity userEntity) {
        // 유효 기한 설정(현재부터 1일)
        Date expiryDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));

        // JWT 토큰 생성
        return Jwts.builder()
                // 헤더에 들어갈 내용 및 서명을 하기 위한 SECRET_KEY
                .signWith(key, SignatureAlgorithm.HS256)
                // 페이로드(payload)에 들어갈 내용
                .setSubject(userEntity.getId()) // sub
                .setIssuer("todo app")
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .compact();
    }

    /**
     * 사용자로부터 토큰을 받아와 그 토큰을 가진 사용자 id 추출
     * 토큰을 디코딩 및 파싱하여 토큰의 위조 여부 확인하는 작업
     */
    public String validateAndGetUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}
