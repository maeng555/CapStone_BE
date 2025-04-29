package org.example.capstone_project.domain.food.controller;

import lombok.RequiredArgsConstructor;
import org.example.capstone_project.domain.food.dto.AiFoodResponse;
import org.example.capstone_project.domain.food.service.AiRecommendationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class AiRecommendController {
    private final AiRecommendationService aiRecommendationService;

    @GetMapping("/recommend")
    public AiFoodResponse getRecommendations(String location) {
        String recommendations = aiRecommendationService.getNearbyRestaurantRecommendation(location);
        return AiFoodResponse.builder()
                .recommendations(recommendations)
                .build();
    }
}
