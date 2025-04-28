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
public class TransitPathResponse {
    private List<TransitLegResponse> legs;
    private int totalWalkDistance;
    private int totalTime;
}