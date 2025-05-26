package org.example.capstone_project.domain.preference.repository;

import org.example.capstone_project.domain.user.entity.User;
import org.example.capstone_project.domain.preference.entity.UserCategoryPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCategoryPreferenceRepository extends JpaRepository<UserCategoryPreference, Long> {
    Optional<UserCategoryPreference> findByUserAndCategoryName(User user, String categoryName);
    List<UserCategoryPreference> findByUser(User user); // 3번 이상클릭한것중 젤 많이 클릭한걸로 보여주기 위한 메서드


}
