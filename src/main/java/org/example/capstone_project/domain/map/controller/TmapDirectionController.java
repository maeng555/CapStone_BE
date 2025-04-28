package org.example.capstone_project.domain.map.controller;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.example.capstone_project.domain.map.dto.TransitCategoryResponse;
import org.example.capstone_project.domain.map.service.TmapDirectionService;
import org.example.capstone_project.domain.map.service.TransitProcessingService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tmap")
@Validated //컨트롤에서 오류 거름
public class TmapDirectionController {

    private final TmapDirectionService tmapDirectionService;
    private final TransitProcessingService transitProcessingService;

    @GetMapping("/transit")
    public String getTransitRoute(
            @RequestParam @NotNull @DecimalMin("0.0") double startX,
            @RequestParam @NotNull @DecimalMin("0.0") double startY,
            @RequestParam @NotNull @DecimalMin("0.0") double endX,
            @RequestParam @NotNull @DecimalMin("0.0") double endY
    ) {
        return tmapDirectionService.findTransitRoute(startX, startY, endX, endY);
    }

    @GetMapping("/transit/filter")
    public TransitCategoryResponse getFilteredTransitRoute(
            @RequestParam @NotNull @DecimalMin("0.0") double startX,
            @RequestParam @NotNull @DecimalMin("0.0") double startY,
            @RequestParam @NotNull @DecimalMin("0.0") double endX,
            @RequestParam @NotNull @DecimalMin("0.0") double endY
    ) {
        String tmapResponse = tmapDirectionService.findTransitRoute(startX, startY, endX, endY);
        TransitCategoryResponse result = transitProcessingService.process(tmapResponse);
        return ResponseEntity.ok(result).getBody();
    }
}