package org.example.capstone_project.domain.map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoTransitService {
    private final String kakaoApiKey = "10cf24583b579386a79d8a7c039d40c7";
    private final RestTemplate restTemplate = new RestTemplate();

    public String findTransitRoute(double originX, double originY, double destX, double destY) {
        String url = "https://apis-navi.kakaomobility.com/v1/directions/transit" + "?origin=" + originX + "," + originY + "&destination =" + destX + "," + destY;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        return response.getBody(); // API 응답 그대로 리턴


    }

}
