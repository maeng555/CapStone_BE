package org.example.capstone_project.domain.food.service;

import lombok.RequiredArgsConstructor;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiRecommendationService {

    private final ChatClient chatClient;

    public String getNearbyRestaurantRecommendation(String location) {
        String prompt = String.format("""
            당신은 맛집 추천 AI입니다.
            사용자의 현재 위치는 "%s"입니다.
            근처에서 인기 있는 음식점 3곳을 추천해 주세요.
            - 한 줄 설명 포함
            - 메뉴 예시 간단히 포함
        """, location);

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content(); // 또는 .result().getOutput().getContent()
    }
}