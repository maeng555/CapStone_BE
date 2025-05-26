package org.example.capstone_project.domain.user.service;


import lombok.RequiredArgsConstructor;
import org.example.capstone_project.domain.preference.repository.UserCategoryPreferenceRepository;
import org.example.capstone_project.domain.user.dto.LoginRequest;
import org.example.capstone_project.domain.user.dto.LoginResponse;
import org.example.capstone_project.domain.user.dto.RegisterRequest;
import org.example.capstone_project.domain.user.entity.User;
import org.example.capstone_project.domain.user.repository.UserRepository;
import org.example.capstone_project.global.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserCategoryPreferenceRepository userCategoryPreferenceRepository;

    public User getUserFromToken(String token) {
        String nickname = jwtUtil.getNickname(token);
        return userRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    }

    public void register(RegisterRequest request) {
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }
        System.out.println("📌 register: DB 저장 시도 중"); // 로그
        User user = new User(null, request.getNickname(), request.getAge(), passwordEncoder.encode(request.getPassword()),null, new ArrayList<>());
        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByNickname(request.getNickname())
                        .orElseThrow(()->new IllegalArgumentException("존재하지 않는 닉네임 입니다"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호 일치하지 않습니다");
        }

        String accessToken = jwtUtil.generateToken(user.getNickname());
        String refreshToken = jwtUtil.generateRefreshToken(user.getNickname());

        user.updateRefreshToken(refreshToken); // ← setter 또는 별도 메서드로
        userRepository.save(user);


        return new LoginResponse(accessToken, refreshToken);
    }

    public LoginResponse reissue(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다.");
        }

        String nickname = jwtUtil.getNickname(refreshToken);
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!refreshToken.equals(user.getRefreshToken())) {
            throw new IllegalArgumentException("토큰이 일치하지 않습니다.");
        }

        String newAccessToken = jwtUtil.generateToken(nickname);
        String newRefreshToken = jwtUtil.generateRefreshToken(nickname);

        user.updateRefreshToken(newRefreshToken);
        userRepository.save(user);

        return new LoginResponse(newAccessToken, newRefreshToken);
    }
}
