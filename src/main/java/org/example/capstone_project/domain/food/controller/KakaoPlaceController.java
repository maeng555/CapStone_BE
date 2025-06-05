package org.example.capstone_project.domain.food.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.example.capstone_project.domain.food.dto.KakaoPlaceResponse;
import org.example.capstone_project.domain.food.service.KakaoPlaceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/places")
public class KakaoPlaceController {

    private final KakaoPlaceService kakaoPlaceService;

    @Operation(summary = "주변 음식점 조회", description = "현재 위치(x, y)를 기준으로 주변 음식점(FD6)을 조회합니다.")
    @GetMapping("/restaurant")
    public List<KakaoPlaceResponse> getRestaurants(
            @Parameter(description = "경도 (longitude)", example = "127.12345") @RequestParam double x,
            @Parameter(description = "위도 (latitude)", example = "37.54321") @RequestParam double y
    ) {
        return kakaoPlaceService.searchPlaces("음식점", "FD6", x, y);
    }

    @Operation(summary = "주변 술집 조회", description = "현재 위치(x, y)를 기준으로 주변 술집(FD6)을 조회합니다.")
    @GetMapping("/bar")
    public List<KakaoPlaceResponse> getBars(
            @Parameter(description = "경도 (longitude)", example = "127.12345") @RequestParam double x,
            @Parameter(description = "위도 (latitude)", example = "37.54321") @RequestParam double y
    ) {
        return kakaoPlaceService.searchPlaces("호프", "FD6", x, y);
    }

    @Operation(summary = "주변 카페 조회", description = "현재 위치(x, y)를 기준으로 주변 카페(CE7)를 조회합니다.")
    @GetMapping("/cafe")
    public List<KakaoPlaceResponse> getCafes(
            @Parameter(description = "경도 (longitude)", example = "127.12345") @RequestParam double x,
            @Parameter(description = "위도 (latitude)", example = "37.54321") @RequestParam double y
    ) {
        return kakaoPlaceService.searchPlaces("카페", "CE7", x, y);
    }

    @Operation(summary = "주변 전체 장소 조회", description = "음식점(FD6), 술집(FD6), 카페(CE7)를 모두 조회합니다.")
    @GetMapping("/all")
    public List<KakaoPlaceResponse> getAllPlaces(
            @Parameter(description = "경도 (longitude)", example = "127.12345") @RequestParam double x,
            @Parameter(description = "위도 (latitude)", example = "37.54321") @RequestParam double y
    ) {
        List<KakaoPlaceResponse> restaurants = kakaoPlaceService.searchPlaces("음식점", "FD6", x, y);
        List<KakaoPlaceResponse> bars = kakaoPlaceService.searchPlaces("술집", "FD6", x, y);
        List<KakaoPlaceResponse> cafes = kakaoPlaceService.searchPlaces("카페", "CE7", x, y);

        restaurants.addAll(bars);
        restaurants.addAll(cafes);
        return restaurants;
    }
}