package org.example.capstone_project.domain.food.service;

import lombok.RequiredArgsConstructor;
import org.example.capstone_project.domain.food.entity.UserSearchKeyword;
import org.example.capstone_project.domain.food.repository.UserSearchKeywordRepository;
import org.example.capstone_project.domain.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSearchKeywordService {

    private final UserSearchKeywordRepository keywordRepository;

    // ✅ 키워드 저장
    public void saveKeyword(User user, String keyword) {
        UserSearchKeyword entry = new UserSearchKeyword();
        entry.setUser(user);
        entry.setKeyword(keyword);
        keywordRepository.save(entry);
    }

    // ✅ 해당 키워드를 2번 이상 검색했는지 여부
    public boolean isFrequentKeyword(User user, String keyword) {
        return keywordRepository.findByUserAndKeyword(user, keyword).size() >= 2;
    }

    // ✅ 유저가 2번 이상 검색한 키워드 목록
    public List<Object[]> getFrequentKeywords(User user) {
        return keywordRepository.findFrequentKeywordsByUser(user);
    }
    public void deleteKeyword(User user, String keyword) {
        List<UserSearchKeyword> entries = keywordRepository.findByUserAndKeyword(user, keyword);
        keywordRepository.deleteAll(entries);
    }
}