package org.example.capstone_project.domain.map.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransitCategoryResponse {
    private List<Map<String, Object>> recommended; // 추천순
    private List<Map<String, Object>> subwayOnly; //지하철만
    private List<Map<String, Object>> busOnly; //버스만
    private Map<String, Object> minTransfer; //최소 환승
    private Map<String, Object> minFare; //최소 요금
    private Map<String, Object> minTime; // 최소 시간 추가
}