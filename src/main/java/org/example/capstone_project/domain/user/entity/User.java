package org.example.capstone_project.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nickname;
    @Column(nullable = false)
    private int age;
    @Column(nullable = false)
    private String password;
    @Column(length = 500)
    private String refreshToken;
    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }




}
