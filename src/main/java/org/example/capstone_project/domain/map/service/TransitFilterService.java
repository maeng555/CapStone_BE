package org.example.capstone_project.domain.map.service;

import lombok.RequiredArgsConstructor;
import org.example.capstone_project.domain.map.dto.TransitCategoryResponse;
import org.example.capstone_project.domain.map.dto.TransitPathResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransitFilterService {
    public List<TransitPathResponse> filterByPreference (TransitCategoryResponse response, String preference) {
        return switch (preference.toUpperCase()){
            case "BUS" -> response.getBusOnly();
            case "SUBWAY" -> response.getSubwayOnly();
            case "MIN_TIME" -> List.of(response.getMinTime());
            case "MIN_FARE" -> List.of(response.getMinFare());
            case "MIN_TRANSFER" -> List.of(response.getMinTransfer());
            default -> response.getRecommended();
        };
    }
}
