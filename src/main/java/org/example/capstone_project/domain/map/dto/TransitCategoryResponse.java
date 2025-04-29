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
public class TransitCategoryResponse {
    private List<TransitPathResponse> recommended;
    private List<TransitPathResponse> subwayOnly;
    private List<TransitPathResponse> busOnly;
    private TransitPathResponse minTransfer;
    private TransitPathResponse minFare;
    private TransitPathResponse minTime;
}