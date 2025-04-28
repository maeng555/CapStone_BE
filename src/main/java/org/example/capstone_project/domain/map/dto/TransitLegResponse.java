package org.example.capstone_project.domain.map.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransitLegResponse {
    private String mode;
    private String route; //노선
    private int sectionTime;
    private int distance;
    private String startName;
    private String endName;
    private List<String> stations; // SUBWAY, BUS용
    private Integer stationCount; // SUBWAY, BUS용
    private List<String> descriptions; // WALK용
}