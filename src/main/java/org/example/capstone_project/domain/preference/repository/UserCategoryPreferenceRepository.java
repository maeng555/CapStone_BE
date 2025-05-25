package org.example.capstone_project.domain.preference.repository;

import org.example.capstone_project.domain.user.entity.User;
import org.example.capstone_project.domain.preference.entity.UserCategoryPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCategoryPreferenceRepository extends JpaRepository<UserCategoryPreference, Long> {
    Optional<UserCategoryPreference> findByUserAndCategoryName(User user, String categoryName);

}
