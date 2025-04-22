package org.example.capstone_project.global.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component // required를 쓰기위해서 autowired도 똑같음
//토큰 생성 및 유효성 검사
public class JwtUtil {
    private static final String SECRET = "your-secret-key-your-secret-key-123456";
    private static final long EXP = 1000L * 60 * 60 * 24 ; // 30일 (밀리초 단위)
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    //토큰 생성 메서드
    public String generateToken(String nickname) {
        long now = System.currentTimeMillis(); // 현재 시간
        Date issuedAt = new Date(now);
        Date expiration = new Date(now + EXP); // 30일 후 만료

        return Jwts.builder()
                .setSubject(nickname) // jwt 주체
                .setIssuedAt(new Date()) //발급 시간
                .setExpiration(expiration) //만료시간
                .signWith(key, SignatureAlgorithm.HS256) //서명
                .compact(); //발급
    }
    //토큰 추출
    public String getNickname(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key) //검증키
                .build()
                .parseClaimsJws(token) //토큰 분석
                .getBody()
                .getSubject(); // 다시 토큰에서 닉네임 추출
    }

    //토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true; // 예외 없으면 유효한 토큰
        } catch (JwtException e) {
            return false; // 서명 위조, 만료, 형식 오류 등 체크 처리
        }
    }
    private static final long REFRESH_EXP = 1000L * 60 * 60 * 24 * 14;

    // 리프레시 토큰 생성
    public String generateRefreshToken(String nickname) {
        return Jwts.builder()
                .setSubject(nickname)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXP))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


}
