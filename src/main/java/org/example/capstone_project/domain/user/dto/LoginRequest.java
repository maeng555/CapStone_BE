package org.example.capstone_project.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequest {
    @NotBlank
    private String nickname;
    @NotBlank
    private String password;
}
