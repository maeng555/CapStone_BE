package org.example.capstone_project.domain.map.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TransitLegResponse {
    private String mode; // walk, bus, subway
    private String route; //노선
    private int sectionTime;  // 노선 시간
    private int distance; //거리
    private String startName; //입력지
    private String endName; //도착지
    private List<String> stations; // SUBWAY, BUS용
    private Integer stationCount; // SUBWAY, BUS용
    private List<String> descriptions; // WALK용

    private String stationId;   // 시작 정류장 ID
    private String routeId;     // 노선 ID
    private String predictTime; // 몇 분 후 도착
}