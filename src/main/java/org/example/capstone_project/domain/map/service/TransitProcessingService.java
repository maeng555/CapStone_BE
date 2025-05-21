package org.example.capstone_project.domain.map.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private final BusRealtimeService busRealtimeService;

    public TransitCategoryResponse process(String tmapResponseJson, String startNameOverride, String endNameOverride) {
        try {
            JsonNode root = objectMapper.readTree(tmapResponseJson);
            JsonNode itinerariesNode = root.path("metaData").path("plan").path("itineraries");

            if (!itinerariesNode.isArray() || itinerariesNode.isEmpty()) {
                throw new IllegalArgumentException("Tmap 응답에 경로 정보가 없습니다.");
            }

            List<TransitPathResponse> paths = new ArrayList<>();

            for (JsonNode itinerary : itinerariesNode) {
                List<TransitLegResponse> legs = new ArrayList<>();
                int totalWalkDistance = 0;
                int totalTime = 0;

                for (JsonNode leg : itinerary.path("legs")) {
                    String mode = leg.path("mode").asText();
                    String route = leg.path("route").asText(null);
                    int sectionTime = leg.path("sectionTime").asInt();
                    int distance = leg.path("distance").asInt();
                    String startName = leg.path("start").path("name").asText();
                    String endName = leg.path("end").path("name").asText();

                    List<String> stations = null;
                    Integer stationCount = null;
                    List<String> descriptions = null;

                    String stationId = null;
                    String routeId = null;
                    String predictTime = null;

                    if (mode.equals("SUBWAY") || mode.equals("BUS")) {
                        JsonNode stationList = leg.path("passStopList").path("stationList");
                        if (stationList.isArray()) {
                            stations = new ArrayList<>();
                            for (JsonNode station : stationList) {
                                stations.add(station.path("stationName").asText());
                            }
                            stationCount = stations.size();

                            // ✅ 수정: BUS의 실시간 정보용 ID 추출 (stationList[0].stationID)
                            if (mode.equals("BUS") && stationList.size() > 0) {
                                stationId = stationList.get(0).path("stationID").asText(null);
                            }
                        }

                        // ✅ 수정: routeId는 leg.path("routeId")로 추출
                        if (mode.equals("BUS")) {
                            routeId = leg.path("routeId").asText(null);

                            if (stationId != null && routeId != null) {
                                predictTime = busRealtimeService.getRealtimeArrival(stationId, routeId);
                            }
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
                            .stationId(stationId)
                            .routeId(routeId)
                            .predictTime(predictTime)
                            .build());
                }

                LocalDateTime now = LocalDateTime.now();
                LocalDateTime arrival = now.plusSeconds(totalTime);

                if (!legs.isEmpty()) {
                    TransitLegResponse first = legs.get(0).toBuilder().startName(startNameOverride).build();
                    TransitLegResponse last = legs.get(legs.size() - 1).toBuilder().endName(endNameOverride).build();
                    legs.set(0, first);
                    legs.set(legs.size() - 1, last);
                }

                paths.add(TransitPathResponse.builder()
                        .legs(legs)
                        .totalWalkDistance(totalWalkDistance)
                        .totalTime(totalTime)
                        .departureTime(now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                        .arrivalTime(arrival.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                        .build());
            }

            return TransitCategoryResponse.builder()
                    .recommended(paths.stream().limit(3).collect(Collectors.toList()))
                    .subwayOnly(paths.stream().filter(this::isSubwayOnly).limit(3).collect(Collectors.toList()))
                    .busOnly(paths.stream().filter(this::isBusOnly).limit(3).collect(Collectors.toList()))
                    .minTransfer(paths.stream().min(Comparator.comparingInt(p -> p.getLegs().size())).orElse(null))
                    .minFare(paths.get(0))
                    .minTime(paths.stream().min(Comparator.comparingInt(TransitPathResponse::getTotalTime)).orElse(null))
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("길찾기 데이터 파싱 실패", e);
        }
    }

    private boolean isSubwayOnly(TransitPathResponse path) {
        return path.getLegs().stream().allMatch(leg -> leg.getMode().equals("SUBWAY") || leg.getMode().equals("WALK"));
    }

    private boolean isBusOnly(TransitPathResponse path) {
        return path.getLegs().stream().allMatch(leg -> leg.getMode().equals("BUS") || leg.getMode().equals("WALK"));
    }
}