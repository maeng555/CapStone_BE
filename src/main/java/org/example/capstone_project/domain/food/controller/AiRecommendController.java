package org.example.capstone_project.domain.food.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.capstone_project.domain.food.PythonRun;
import org.example.capstone_project.domain.food.dto.KakaoPlaceResponse;
import org.example.capstone_project.domain.food.dto.UserSearchKeywordRequest;
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


    private static final Set<String> CAFE_KEYWORDS = Set.of(
            "카페", "커피", "아메리카노", "스타벅스", "이디야", "투썸", "메가커피", "빽다방", "할리스", "폴바셋"
    );


    private String determineCategoryCode(String keyword) {
        String lower = keyword.toLowerCase();
        for (String cafe : CAFE_KEYWORDS) {
            if (lower.contains(cafe)) return "CE7";
        }
        return "FD6";
    }

    @PostMapping("/search")
    public ResponseEntity<List<KakaoPlaceResponse>> searchPlacesByKeyword(
            @RequestHeader("Authorization") String token,
            @RequestBody UserSearchKeywordRequest keywordRequest,
            @RequestParam double x,
            @RequestParam double y
    ) {
        String pureToken = token.replace("Bearer ", "");
        User user = userService.getUserFromToken(pureToken);


        keywordService.saveKeyword(user, keywordRequest.getKeyword());

        String categoryCode = determineCategoryCode(keywordRequest.getKeyword());
        List<KakaoPlaceResponse> places = kakaoPlaceService.searchPlaces(keywordRequest.getKeyword(), categoryCode, x, y);

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
    ) throws IOException {
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