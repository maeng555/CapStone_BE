package org.example.capstone_project.domain.map.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private static final String TMAP_REVERSE_GEO_URL = "https://apis.openapi.sk.com/tmap/geo/reversegeocoding?version=1&coordType=WGS84GEO&addressType=A10";


    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    public String findTransitRoute(double startX, double startY, double endX, double endY) {
        String startName = getAddressName(startY, startX); // 위도, 경도 순서 주의
        String endName = getAddressName(endY, endX);

        HttpHeaders headers = new HttpHeaders();
        headers.set("appKey", TMAP_API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON); // 이거 중요!!

        Map<String, Object> body = new HashMap<>();
        body.put("startX", String.valueOf(startX));
        body.put("startY", String.valueOf(startY));
        body.put("endX", String.valueOf(endX));
        body.put("endY", String.valueOf(endY));
        body.put("startName", startName);
        body.put("endName", endName);
        body.put("format", "json");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        return restTemplate.exchange(
                TMAP_BASE_URL,
                HttpMethod.POST,
                entity,
                String.class
        ).getBody();
    }
    public String getAddressName(double lat, double lon) {
        String url = TMAP_REVERSE_GEO_URL + "&lat=" + lat + "&lon=" + lon;

        HttpHeaders headers = new HttpHeaders();
        headers.set("appKey", TMAP_API_KEY);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JsonNode root = objectMapper.readTree(response.getBody());
            return root.path("addressInfo").path("buildingName").asText();
        } catch (Exception e) {
            return "위치"; // fallback
        }
    }
}