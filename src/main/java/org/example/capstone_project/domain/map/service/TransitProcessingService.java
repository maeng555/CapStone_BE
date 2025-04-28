package org.example.capstone_project.domain.map.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.capstone_project.domain.map.dto.TransitCategoryResponse;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransitProcessingService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public TransitCategoryResponse process(String tmapResponseJson) {
        try {
            // JSON 파싱
            JsonNode root = objectMapper.readTree(tmapResponseJson);
            JsonNode itinerariesNode = root
                    .path("metaData")
                    .path("plan")
                    .path("itineraries");

            // itineraries가 아예 없거나 비어있을 때 방어
            if (itinerariesNode.isMissingNode() || !itinerariesNode.isArray() || itinerariesNode.isEmpty()) {
                throw new IllegalArgumentException("Tmap 응답에 경로 정보(itineraries)가 없습니다.");
            }

            List<Map<String, Object>> itineraryList = objectMapper.convertValue(
                    itinerariesNode,
                    List.class
            );

            // 추천 (전체)
            List<Map<String, Object>> recommended = itineraryList.stream()
                    .limit(3)
                    .collect(Collectors.toList());
            // 지하철만
            List<Map<String, Object>> subwayOnly = itineraryList.stream()
                    .filter(this::isSubwayOnly)
                    .limit(3)
                    .collect(Collectors.toList());

            // 버스만
            List<Map<String, Object>> busOnly = itineraryList.stream()
                    .filter(this::isBusOnly)
                    .limit(3)
                    .collect(Collectors.toList());

            // 최소 환승
            Map<String, Object> minTransfer = itineraryList.stream()
                    .filter(itinerary -> itinerary.get("transferCount") != null)
                    .min(Comparator.comparingInt(itinerary -> (int) itinerary.get("transferCount")))
                    .orElse(null);

            // 최소 요금
            Map<String, Object> minFare = itineraryList.stream()
                    .filter(itinerary -> getTotalFare(itinerary) != null)
                    .min(Comparator.comparingInt(this::getTotalFare))
                    .orElse(null);

            // 최소 소요시간
            Map<String, Object> minTime = itineraryList.stream()
                    .filter(itinerary -> itinerary.get("totalTime") != null)
                    .min(Comparator.comparingInt(itinerary -> (int) itinerary.get("totalTime")))
                    .orElse(null);

            return TransitCategoryResponse.builder()
                    .recommended(recommended)
                    .subwayOnly(subwayOnly)
                    .busOnly(busOnly)
                    .minTransfer(minTransfer)
                    .minFare(minFare)
                    .minTime(minTime)
                    .build();

        } catch (IllegalArgumentException e) {
            throw e; // 직접 던진 것은 그대로
        } catch (Exception e) {
            throw new RuntimeException("길찾기 데이터 파싱 실패", e);
        }
    }

    // ✨ "totalFare" 꺼내는 보조 함수
    private Integer getTotalFare(Map<String, Object> itinerary) {
        try {
            Map<String, Object> fare = (Map<String, Object>) itinerary.get("fare");
            Map<String, Object> regular = (Map<String, Object>) fare.get("regular");
            return (Integer) regular.get("totalFare");
        } catch (Exception e) {
            return null;
        }
    }

    // 🚀 수정한 부분: "WALK"은 허용
    private boolean isSubwayOnly(Map<String, Object> itinerary) {
        List<Map<String, Object>> legs = (List<Map<String, Object>>) itinerary.get("legs");
        return legs.stream().allMatch(leg ->
                "SUBWAY".equals(leg.get("mode")) || "WALK".equals(leg.get("mode"))
        );
    }

    private boolean isBusOnly(Map<String, Object> itinerary) {
        List<Map<String, Object>> legs = (List<Map<String, Object>>) itinerary.get("legs");
        return legs.stream().allMatch(leg ->
                "BUS".equals(leg.get("mode")) || "WALK".equals(leg.get("mode"))
        );
    }
}