package org.example.capstone_project.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capstone_project.domain.user.dto.LoginRequest;
import org.example.capstone_project.domain.user.dto.LoginResponse;
import org.example.capstone_project.domain.user.dto.RegisterRequest;
import org.example.capstone_project.domain.user.entity.User;
import org.example.capstone_project.domain.user.service.UserService;
import org.example.capstone_project.global.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor

@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "사용자 인증 API")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "회원가입", description = "닉네임, 나이, 비밀번호를 입력받아 사용자 계정을 생성합니다.")
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest registerRequest) {
        userService.register(registerRequest);
        return ResponseEntity.ok("회원가입 성공");
    }

    @Operation(summary = "로그인", description = "닉네임과 비밀번호로 로그인하고 액세스 및 리프레시 토큰을 발급받습니다.")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        userService.login(loginRequest);
        return ResponseEntity.ok(userService.login(loginRequest));
    }

    @Operation(summary = "토큰 재발급", description = "리프레시 토큰을 이용하여 새로운 액세스 및 리프레시 토큰을 발급받습니다.")
    @PostMapping("/reissue")
    public ResponseEntity<LoginResponse> reissue(@RequestHeader("Refresh-Token") String refreshToken) {
        return ResponseEntity.ok(userService.reissue(refreshToken));
    }
}
