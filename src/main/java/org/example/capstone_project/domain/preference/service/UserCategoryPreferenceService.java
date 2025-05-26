package org.example.capstone_project.domain.preference.service;

import lombok.RequiredArgsConstructor;
import org.example.capstone_project.domain.preference.entity.UserCategoryPreference;
import org.example.capstone_project.domain.preference.repository.UserCategoryPreferenceRepository;
import org.example.capstone_project.domain.user.entity.User;
import org.example.capstone_project.domain.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class UserCategoryPreferenceService {
    private final UserCategoryPreferenceRepository userCategoryPreferenceRepository;
    private final UserService userService;

    public void increaseClickCount(User user, String categoryName) {
        UserCategoryPreference preference = userCategoryPreferenceRepository
                .findByUserAndCategoryName(user,categoryName)
                .orElse(UserCategoryPreference.builder()
                        .user(user)
                        .categoryName(categoryName)
                        .clickCount(0)
                        .build()); //없으면 객체 생성
        preference.increaseClickCount();
        userCategoryPreferenceRepository.save(preference);
    }
    public String getAutoSelectedCategory(User user) {


        return userCategoryPreferenceRepository.findByUser(user).stream()
                .filter(p -> p.getClickCount() >= 3)
                .max(Comparator.comparingInt(UserCategoryPreference::getClickCount))
                .map(UserCategoryPreference::getCategoryName)
                .orElse(null); // 없으면 null 반환
    }

}
