package org.example.capstone_project.domain.user.service;


import lombok.RequiredArgsConstructor;
import org.example.capstone_project.domain.user.dto.LoginRequest;
import org.example.capstone_project.domain.user.dto.LoginResponse;
import org.example.capstone_project.domain.user.dto.RegisterRequest;
import org.example.capstone_project.domain.user.entity.User;
import org.example.capstone_project.domain.user.repository.UserRepository;
import org.example.capstone_project.global.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public void register(RegisterRequest request) {
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }
        System.out.println("📌 register: DB 저장 시도 중"); // 로그
        User user = new User(null, request.getNickname(), request.getAge(), passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByNickname(request.getNickname())
                        .orElseThrow(()->new IllegalArgumentException("존재하지 않는 닉네임 입니다"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호 일치하지 않습니다");
        }
        String token = jwtUtil.generateToken(user.getNickname());
        return new LoginResponse(token);
    }
}
