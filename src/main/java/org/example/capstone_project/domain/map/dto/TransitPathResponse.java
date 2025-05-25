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
public class TransitPathResponse {
    private List<TransitLegResponse> legs; // 교통구간 별 리스트
    private int totalWalkDistance; //총 걷기 거리
    private int totalTime; // 총시간

    private String departureTime; //시작 시간
    private String arrivalTime; //도착시간
}