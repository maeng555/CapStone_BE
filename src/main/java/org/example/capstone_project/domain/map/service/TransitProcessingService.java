package org.example.capstone_project.domain.map.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.capstone_project.domain.map.dto.TransitCategoryResponse;
import org.example.capstone_project.domain.map.dto.TransitLegResponse;
import org.example.capstone_project.domain.map.dto.TransitPathResponse;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransitProcessingService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public TransitCategoryResponse process(String tmapResponseJson) {
        try {
            JsonNode root = objectMapper.readTree(tmapResponseJson);
            JsonNode itinerariesNode = root.path("metaData").path("plan").path("itineraries");

            if (itinerariesNode.isMissingNode() || !itinerariesNode.isArray() || itinerariesNode.isEmpty()) {
                throw new IllegalArgumentException("Tmap 응답에 경로 정보가 없습니다.");
            }

            // 수정: 여기서 빈 리스트 만들어
            List<TransitPathResponse> paths = new ArrayList<>();

            for (JsonNode itinerary : itinerariesNode) {
                List<TransitLegResponse> legs = new ArrayList<>();
                int totalWalkDistance = 0;
                int totalTime = 0;

                for (JsonNode leg : itinerary.path("legs")) {
                    String mode = leg.path("mode").asText();
                    String route = leg.path("route").isMissingNode() ? null : leg.path("route").asText();
                    int sectionTime = leg.path("sectionTime").asInt();
                    int distance = leg.path("distance").asInt();
                    String startName = leg.path("start").path("name").asText();
                    String endName = leg.path("end").path("name").asText();

                    List<String> stations = null;
                    Integer stationCount = null;
                    List<String> descriptions = null;

                    if (mode.equals("SUBWAY") || mode.equals("BUS")) {
                        JsonNode stationList = leg.path("passStopList").path("stationList");
                        if (stationList.isArray()) {
                            stations = new ArrayList<>();
                            for (JsonNode station : stationList) {
                                stations.add(station.path("stationName").asText());
                            }
                            stationCount = stations.size();
                        }
                    }

                    if (mode.equals("WALK")) {
                        JsonNode steps = leg.path("steps");
                        if (steps.isArray()) {
                            descriptions = new ArrayList<>();
                            for (JsonNode step : steps) {
                                descriptions.add(step.path("description").asText());
                            }
                        }
                        totalWalkDistance += distance;
                    }

                    totalTime += sectionTime;

                    legs.add(TransitLegResponse.builder()
                            .mode(mode)
                            .route(route)
                            .sectionTime(sectionTime)
                            .distance(distance)
                            .startName(startName)
                            .endName(endName)
                            .stations(stations)
                            .stationCount(stationCount)
                            .descriptions(descriptions)
                            .build());
                }

                paths.add(TransitPathResponse.builder()
                        .legs(legs)
                        .totalWalkDistance(totalWalkDistance)
                        .totalTime(totalTime)
                        .build());
            }

            return TransitCategoryResponse.builder()
                    .recommended(paths.stream().limit(3).collect(Collectors.toList()))
                    .subwayOnly(paths.stream().filter(this::isSubwayOnly).limit(3).collect(Collectors.toList()))
                    .busOnly(paths.stream().filter(this::isBusOnly).limit(3).collect(Collectors.toList()))
                    .minTransfer(paths.stream().min(Comparator.comparingInt(p -> p.getLegs().size())).orElse(null))
                    .minFare(paths.get(0)) // 가장 첫 번째가 요금 제일 적은 경로
                    .minTime(paths.stream().min(Comparator.comparingInt(TransitPathResponse::getTotalTime)).orElse(null))
                    .build();

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("길찾기 데이터 파싱 실패", e);
        }
    }

    private boolean isSubwayOnly(TransitPathResponse path) {
        return path.getLegs().stream()
                .allMatch(leg -> leg.getMode().equals("SUBWAY") || leg.getMode().equals("WALK"));
    }

    private boolean isBusOnly(TransitPathResponse path) {
        return path.getLegs().stream()
                .allMatch(leg -> leg.getMode().equals("BUS") || leg.getMode().equals("WALK"));
    }
}