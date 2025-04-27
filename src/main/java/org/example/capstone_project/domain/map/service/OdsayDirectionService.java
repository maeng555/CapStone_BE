package org.example.capstone_project.domain.map.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class OdsayDirectionService {
    private static final String ODSAY_API_KEY = "6I50eWAeTRbH2G7cqwUu04TafL+q37mwp4YKqVyHSBE"; // 네 키
    private static final String ODSAY_BASE_URL = "https://api.odsay.com/v1/api/searchPubTransPathT";

    private final RestTemplate restTemplate = new RestTemplate();

    public String findTransitRoute(double startX, double startY, double goalX, double goalY) {
        String url = UriComponentsBuilder.fromHttpUrl(ODSAY_BASE_URL)
                .queryParam("SX", startX)
                .queryParam("SY", startY)
                .queryParam("EX", goalX)
                .queryParam("EY", goalY)
                .queryParam("apiKey", ODSAY_API_KEY) // 여기 꼭 query로 넣는다!!
                .toUriString();

        return restTemplate.getForObject(url, String.class);
    }
}