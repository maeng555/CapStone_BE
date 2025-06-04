package org.example.capstone_project.domain.food.controller;

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


    @GetMapping("/restaurant")
    public List<KakaoPlaceResponse> getRestaurants(@RequestParam double x, @RequestParam double y) {
        return kakaoPlaceService.searchPlaces("음식점", "FD6", x, y);
    }

    @GetMapping("/bar")
    public List<KakaoPlaceResponse> getBars(@RequestParam double x, @RequestParam double y) {
        return kakaoPlaceService.searchPlaces("술집", "FD6", x, y);
    }

    @GetMapping("/cafe")
    public List<KakaoPlaceResponse> getCafes(@RequestParam double x, @RequestParam double y) {
        return kakaoPlaceService.searchPlaces("카페", "CE7", x, y);
    }
    @GetMapping("/all")
    public List<KakaoPlaceResponse> getAllPlaces(@RequestParam double x, @RequestParam double y) {
        List<KakaoPlaceResponse> restaurants = kakaoPlaceService.searchPlaces("음식점", "FD6", x, y);
        List<KakaoPlaceResponse> bars = kakaoPlaceService.searchPlaces("술집", "FD6", x, y);
        List<KakaoPlaceResponse> cafes = kakaoPlaceService.searchPlaces("카페", "CE7", x, y);

        restaurants.addAll(bars);
        restaurants.addAll(cafes);
        return restaurants;
    }
}
