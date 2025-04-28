package org.example.capstone_project.domain.map.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TmapDirectionService {
    private static final String TMAP_API_KEY = "DDbQqFb8vL4eKAVdr9EPq7j8yKdU3ot7ivMVuJ45";
    private static final String TMAP_BASE_URL = "https://apis.openapi.sk.com/transit/routes";

    private final RestTemplate restTemplate = new RestTemplate();

    public String findTransitRoute(double startX, double startY, double endX, double endY) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("appKey", TMAP_API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON); // 이거 중요!!

        Map<String, Object> body = new HashMap<>();
        body.put("startX", String.valueOf(startX));
        body.put("startY", String.valueOf(startY));
        body.put("endX", String.valueOf(endX));
        body.put("endY", String.valueOf(endY));
        body.put("format", "json");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        return restTemplate.exchange(
                TMAP_BASE_URL,
                HttpMethod.POST,
                entity,
                String.class
        ).getBody();
    }
}