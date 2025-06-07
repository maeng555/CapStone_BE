package org.example.capstone_project.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.capstone_project.domain.food.entity.Favorite;
import org.example.capstone_project.domain.food.entity.UserSearchKeyword;
import org.example.capstone_project.domain.preference.entity.UserCategoryPreference;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserCategoryPreference> preferences = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorite> favorites = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserSearchKeyword> keywords = new ArrayList<>();

}
