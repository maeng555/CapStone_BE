package org.example.capstone_project.domain.preference.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.capstone_project.domain.user.entity.User;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCategoryPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String categoryName;

    @Column(nullable = false)
    private int clickCount;

    public void increaseClickCount() {
        this.clickCount += 1;
    }

}