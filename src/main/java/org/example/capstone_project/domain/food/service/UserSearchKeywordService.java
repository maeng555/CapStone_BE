package org.example.capstone_project.domain.food.service;

import lombok.RequiredArgsConstructor;
import org.example.capstone_project.domain.food.entity.UserSearchKeyword;
import org.example.capstone_project.domain.food.repository.UserSearchKeywordRepository;
import org.example.capstone_project.domain.user.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSearchKeywordService {

    private final UserSearchKeywordRepository keywordRepository;


    public void saveKeyword(User user, String keyword) {
        UserSearchKeyword entry = new UserSearchKeyword();
        entry.setUser(user);
        entry.setKeyword(keyword);
        entry.setSearchedAt(LocalDateTime.now());
        keywordRepository.save(entry);
    }


    public List<Object[]> getFrequentKeywords(User user) {
        return keywordRepository.findFrequentKeywordsByUser(user);
    }
    public void deleteKeyword(User user, String keyword) {
        List<UserSearchKeyword> entries = keywordRepository.findByUserAndKeyword(user, keyword);
        keywordRepository.deleteAll(entries);
    }
}