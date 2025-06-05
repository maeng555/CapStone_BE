package org.example.capstone_project.domain.food.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.capstone_project.domain.food.PythonRun;
import org.example.capstone_project.domain.food.dto.KakaoPlaceResponse;
import org.example.capstone_project.domain.food.service.KakaoPlaceService;
import org.example.capstone_project.domain.food.service.UserSearchKeywordService;
import org.example.capstone_project.domain.user.entity.User;
import org.example.capstone_project.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommend")
public class AiRecommendController {

    private final KakaoPlaceService kakaoPlaceService;
    private final UserSearchKeywordService keywordService;
    private final UserService userService;

    // ✅ 자동 분류 기준 키워드 목록
    private static final Set<String> CAFE_KEYWORDS = Set.of(
            "카페", "커피", "아메리카노", "스타벅스", "이디야", "투썸", "메가커피", "빽다방", "할리스", "폴바셋"
    );

    // ✅ 키워드를 보고 category_code 결정
    private String determineCategoryCode(String keyword) {
        String lower = keyword.toLowerCase();
        for (String cafe : CAFE_KEYWORDS) {
            if (lower.contains(cafe)) return "CE7"; // 카페
        }
        return "FD6"; // 기본: 음식점
    }

    @Operation(summary = "키워드 기반 맛집 검색 (자동 카테고리)", description = "사용자가 검색한 키워드와 위치를 기반으로 카카오 API에서 맛집을 검색하고, 키워드를 저장합니다.")
    @PostMapping("/search")
    public ResponseEntity<List<KakaoPlaceResponse>> searchPlacesByKeyword(
            @RequestHeader("Authorization") String token,
            @RequestParam String keyword,
            @RequestParam double x,
            @RequestParam double y
    ) {
        String pureToken = token.replace("Bearer ", "");
        User user = userService.getUserFromToken(pureToken);

        keywordService.saveKeyword(user, keyword);

        String categoryCode = determineCategoryCode(keyword);
        List<KakaoPlaceResponse> places = kakaoPlaceService.searchPlaces(keyword, categoryCode, x, y);

        if (places.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(places);
    }

    @GetMapping("/auto")
    @Operation(summary = "사용자 임베딩 기반 자동 추천", description = "사용자가 2번 이상 검색한 키워드 기반으로 Python 유사도 계산 후 추천 장소 반환")
    public ResponseEntity<List<KakaoPlaceResponse>> recommendAuto(
            @RequestHeader("Authorization") String token,
            @RequestParam double x,
            @RequestParam double y
    ) {
        String pureToken = token.replace("Bearer ", "");
        User user = userService.getUserFromToken(pureToken);

        List<Object[]> frequentKeywords = keywordService.getFrequentKeywords(user);
        if (frequentKeywords.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<KakaoPlaceResponse> finalResults = new ArrayList<>();

        for (Object[] entry : frequentKeywords) {
            String baseKeyword = (String) entry[0];

            String similarCsv = PythonRun.runPythonScript(baseKeyword);
            if (similarCsv == null || similarCsv.isBlank()) {
                keywordService.deleteKeyword(user, baseKeyword);  // 삭제
                continue;
            }

            String[] similarKeywords = similarCsv.split(",");

            for (String word : similarKeywords) {
                word = word.trim();
                if (word.isEmpty()) continue;

                String categoryCode = determineCategoryCode(word);  // 자동 분류
                List<KakaoPlaceResponse> places = kakaoPlaceService.searchPlaces(word, categoryCode, x, y);
                if (places != null && !places.isEmpty()) {
                    finalResults.addAll(places);
                }
            }
        }

        if (finalResults.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(finalResults);
    }
}