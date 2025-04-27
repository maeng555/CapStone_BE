package org.example.capstone_project.domain.map.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NaverDirectionService {

    private final String clientId = "ydgdnjwuc8";
    private final String clientSecret = "F6mR1nNUh4Vn8BaW8FgdL77E8I2VbJxmhLE57kYa";

    private final RestTemplate restTemplate = new RestTemplate();

    public String findTransitRoute(double startX, double startY, double goalX, double goalY) {
        String url = "https://naveropenapi.apigw.ntruss.com/map-direction-15/v1/driving"
                + "?start=" + startX + "," + startY
                + "&goal=" + goalX + "," + goalY;
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-APIGW-API-KEY-ID", clientId);
        headers.set("X-NCP-APIGW-API-KEY", clientSecret);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        return response.getBody();
    }
}
