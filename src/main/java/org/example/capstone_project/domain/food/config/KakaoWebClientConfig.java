package org.example.capstone_project.domain.food.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class KakaoWebClientConfig {
    @Bean
    public WebClient kakaoWebClient() {
        return WebClient.builder()
                .baseUrl("https://dapi.kakao.com")
                .defaultHeader("Authorization", "KakaoAK f67936837301f598045cf8bb51986262")
                .build();
    }
}