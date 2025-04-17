package org.example.capstone_project.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor //this token = token
public class LoginResponse {
    private String token;
    private String refreshToken;
}
