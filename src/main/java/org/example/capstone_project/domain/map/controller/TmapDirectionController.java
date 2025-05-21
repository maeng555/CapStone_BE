package org.example.capstone_project.domain.map.controller;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.capstone_project.domain.map.dto.TransitCategoryResponse;
import org.example.capstone_project.domain.map.dto.TransitPathResponse;
import org.example.capstone_project.domain.map.service.BusRealtimeService;
import org.example.capstone_project.domain.map.service.TmapDirectionService;
import org.example.capstone_project.domain.map.service.TransitFilterService;
import org.example.capstone_project.domain.map.service.TransitProcessingService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tmap")
@Validated //컨트롤에서 오류 거름
public class TmapDirectionController {

    private final TmapDirectionService tmapDirectionService;
    private final TransitProcessingService transitProcessingService;
    private final TransitFilterService transitFilterService;
    private final BusRealtimeService busRealtimeService;

    // tmap api 모든내용
    @GetMapping("/transit")
    public String getTransitRoute(
            @RequestParam @NotNull @DecimalMin("0.0") double startX,
            @RequestParam @NotNull @DecimalMin("0.0") double startY,
            @RequestParam @NotNull @DecimalMin("0.0") double endX,
            @RequestParam @NotNull @DecimalMin("0.0") double endY
    ) {
        return tmapDirectionService.findTransitRoute(startX, startY, endX, endY);
    }
    //전체 분류 보여주기
    @GetMapping("/transit/filter")
    public TransitCategoryResponse getFilteredTransitRoute(
            @RequestParam @NotNull @DecimalMin("0.0") double startX,
            @RequestParam @NotNull @DecimalMin("0.0") double startY,
            @RequestParam @NotNull @DecimalMin("0.0") double endX,
            @RequestParam @NotNull @DecimalMin("0.0") double endY
    ) {
        String tmapResponse = tmapDirectionService.findTransitRoute(startX, startY, endX, endY);
        String startName = tmapDirectionService.getAddressName(startY, startX);
        String endName = tmapDirectionService.getAddressName(endY, endX);

        TransitCategoryResponse result = transitProcessingService.process(tmapResponse, startName, endName);
        return ResponseEntity.ok(result).getBody();
    }

    @GetMapping("/transit/preferred")
    public ResponseEntity<List<TransitPathResponse>> getPreferredTransitRoute(
            @RequestParam @NotNull @DecimalMin("0.0") double startX,
            @RequestParam @NotNull @DecimalMin("0.0") double startY,
            @RequestParam @NotNull @DecimalMin("0.0") double endX,
            @RequestParam @NotNull @DecimalMin("0.0") double endY,
            @RequestParam @NotNull String preferred)
    {
        String tmapResponse = tmapDirectionService.findTransitRoute(startX, startY, endX, endY);
        String startName = tmapDirectionService.getAddressName(startY, startX);
        String endName = tmapDirectionService.getAddressName(endY, endX);

        TransitCategoryResponse categoryResult = transitProcessingService.process(tmapResponse, startName, endName);
        List<TransitPathResponse> filtered = transitFilterService.filterByPreference(categoryResult, preferred);
        return ResponseEntity.ok(filtered);
    }




}