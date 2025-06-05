package org.example.capstone_project.domain.food.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.capstone_project.domain.food.dto.KakaoPlaceResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KakaoPlaceService {

    private final WebClient kakaoWebClient;

    public List<KakaoPlaceResponse> searchPlaces(String keyword,String categoryCode, double x, double y) {
        if (keyword == null || keyword.isBlank()) {
            keyword = "음식점";
        }

        String finalKeyword = keyword;
        return kakaoWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/local/search/keyword.json")
                        .queryParam("query", finalKeyword)
                        .queryParam("category_group_code", categoryCode)
                        .queryParam("x", x)
                        .queryParam("y", y)
                        //.queryParam("radius", 2000)
                        .queryParam("sort", "accuracy")
                        .build())
                .retrieve()
                .bodyToMono(KakaoRawResponse.class)
                .blockOptional()
                .map(KakaoRawResponse::getDocuments)
                .orElse(List.of())
                .stream()
                .map(doc -> {
                    KakaoPlaceResponse response = new KakaoPlaceResponse();
                    response.setPlaceName(doc.getPlaceName());
                    response.setPlaceUrl(doc.getPlaceUrl());
                    response.setX(doc.getX());
                    response.setY(doc.getY());
                    response.setAddressName(doc.getAddressName());
                    return response;
                })
                .collect(Collectors.toList());
    }


    @Data
    private static class KakaoRawResponse {
        private List<Document> documents;

        @Data
        static class Document {
            @JsonProperty("place_name")
            private String placeName;

            @JsonProperty("place_url")
            private String placeUrl;

            @JsonProperty("x")
            private String x;

            @JsonProperty("y")
            private String y;

            @JsonProperty("address_name")
            private String addressName;
        }
    }
}