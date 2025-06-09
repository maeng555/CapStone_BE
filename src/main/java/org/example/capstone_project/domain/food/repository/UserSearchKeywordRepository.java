package org.example.capstone_project.domain.food.repository;

import org.example.capstone_project.domain.food.entity.UserSearchKeyword;
import org.example.capstone_project.domain.user.entity.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserSearchKeywordRepository extends JpaRepository<UserSearchKeyword, Long> {

    // 유저가 특정 키워드를 검색한 전체 로그
    List<UserSearchKeyword> findByUserAndKeyword(User user, String keyword);

    // 유저가 검색한 키워드 중 2회 이상 검색된 것 목록
    @Query("SELECT k.keyword, COUNT(k) as cnt " +
            "FROM UserSearchKeyword k " +
            "WHERE k.user = :user " +
            "GROUP BY k.keyword " +
            "HAVING COUNT(k) >= 2 " +
            "ORDER BY MAX(k.searchedAt) DESC")
    List<Object[]> findFrequentKeywordsByUser(@Param("user") User user);
}
