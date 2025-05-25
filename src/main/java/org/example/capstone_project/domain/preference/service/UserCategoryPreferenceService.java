package org.example.capstone_project.domain.preference.service;

import lombok.RequiredArgsConstructor;
import org.example.capstone_project.domain.preference.entity.UserCategoryPreference;
import org.example.capstone_project.domain.preference.repository.UserCategoryPreferenceRepository;
import org.example.capstone_project.domain.user.entity.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCategoryPreferenceService {
    private final UserCategoryPreferenceRepository userCategoryPreferenceRepository;

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

}
