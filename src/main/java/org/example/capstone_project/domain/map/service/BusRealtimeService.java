package org.example.capstone_project.domain.map.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BusRealtimeService {

    private static final String REALTIME_URL = "https://apis.openapi.sk.com/transit/bus/arrival";
    private static final String API_KEY = "DDbQqFb8vL4eKAVdr9EPq7j8yKdU3ot7ivMVuJ45";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getRealtimeArrival(String stationId, String routeId) {
        String url = REALTIME_URL + "?version=1&stationID=" + stationId + "&routeID=" + routeId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("appKey", API_KEY);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JsonNode root = objectMapper.readTree(response.getBody());

            JsonNode arrivalInfo = root.path("arrivalList").get(0);
            return arrivalInfo.path("predictTime1").asText() + "분 후 도착";

        } catch (Exception e) {
            return "실시간 정보 없음";
        }
    }
}
