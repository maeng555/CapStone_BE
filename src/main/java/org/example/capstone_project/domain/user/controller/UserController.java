package org.example.capstone_project.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capstone_project.domain.user.dto.LoginRequest;
import org.example.capstone_project.domain.user.dto.LoginResponse;
import org.example.capstone_project.domain.user.dto.RegisterRequest;
import org.example.capstone_project.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest registerRequest) {
        userService.register(registerRequest);
        return ResponseEntity.ok("회원가입 성공");
    }

    @GetMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        userService.login(loginRequest);
        return ResponseEntity.ok(userService.login(loginRequest));
    }
}
