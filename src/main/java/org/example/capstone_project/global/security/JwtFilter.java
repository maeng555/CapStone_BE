package org.example.capstone_project.global.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.capstone_project.global.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor //jwtutil 생성자 주입
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override //Http 요청에 필터 작동 , 검사 및 인증 처리 수행
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
        throws ServletException, IOException{

        String authHeader = request.getHeader("Authorization"); // 헤더 추출
        //가져온 토큰 앞부분을 자르고 유효성검증 - 인증객체 생성
        if (authHeader != null && authHeader.startsWith("Bearer ")){
            authHeader = authHeader.substring(7);

            if (jwtUtil.validateToken(authHeader)){
                String nickname = jwtUtil.getNickname(authHeader);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(nickname, null, List.of());
                SecurityContextHolder.getContext().setAuthentication(authentication); //인증 정보 등록
            }
            filterChain.doFilter(request, response); //다음 필터로 넘김
        }

    }

}
