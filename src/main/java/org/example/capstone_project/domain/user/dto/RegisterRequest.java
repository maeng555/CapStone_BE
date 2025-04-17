package org.example.capstone_project.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RegisterRequest {
    @NotBlank // 공백 및 null x
    private String nickname;
    @NotNull
    private Integer age;
    @NotBlank
    private String password;
}
