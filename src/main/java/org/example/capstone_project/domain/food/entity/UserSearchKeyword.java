package org.example.capstone_project.domain.food.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.capstone_project.domain.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_search_keyword")
public class UserSearchKeyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String keyword;

    @Column(nullable = false)
    private LocalDateTime searchedAt = LocalDateTime.now();

    @PrePersist
    protected void onCreate() {
        this.searchedAt = LocalDateTime.now();
    }
}
