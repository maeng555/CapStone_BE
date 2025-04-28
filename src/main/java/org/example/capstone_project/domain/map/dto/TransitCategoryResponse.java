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
    private List<TransitPathResponse> recommended;  // ✅ 수정
    private List<TransitPathResponse> subwayOnly;   // ✅ 수정
    private List<TransitPathResponse> busOnly;      // ✅ 수정
    private TransitPathResponse minTransfer;        // ✅ 수정
    private TransitPathResponse minFare;            // ✅ 수정
    private TransitPathResponse minTime;            // ✅ 수정
}