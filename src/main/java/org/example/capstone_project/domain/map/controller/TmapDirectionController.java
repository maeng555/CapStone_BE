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
import org.example.capstone_project.domain.preference.service.UserCategoryPreferenceService;
import org.example.capstone_project.domain.user.entity.User;
import org.example.capstone_project.domain.user.service.UserService;
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
    private final UserCategoryPreferenceService preferenceService;
    private final UserService userService;

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
    //선호도 화면에서 클릭 횟수 증가 및 선호도 선택한 경로 보여주기
    @PostMapping("/transit/preferred/click")
    public ResponseEntity<List<TransitPathResponse>> handleCategoryClickAndGetRoute(
            @RequestHeader("Authorization") String token,
            @RequestParam @NotNull @DecimalMin("0.0") double startX,
            @RequestParam @NotNull @DecimalMin("0.0") double startY,
            @RequestParam @NotNull @DecimalMin("0.0") double endX,
            @RequestParam @NotNull @DecimalMin("0.0") double endY,
            @RequestParam @NotNull String preferred
    ) {
        String pureToken = token.replace("Bearer ", "");
        User user = userService.getUserFromToken(pureToken);
        preferenceService.increaseClickCount(user, preferred);

        String tmapResponse = tmapDirectionService.findTransitRoute(startX, startY, endX, endY);
        String startName = tmapDirectionService.getAddressName(startY, startX);
        String endName = tmapDirectionService.getAddressName(endY, endX);
        TransitCategoryResponse categoryResult = transitProcessingService.process(tmapResponse, startName, endName);
        List<TransitPathResponse> filtered = transitFilterService.filterByPreference(categoryResult, preferred);

        return ResponseEntity.ok(filtered);
    }
    //자동으로 3회햇을경우 목적지입력했을때 바로 자동화 경로화면 렌더링
    @GetMapping("/transit/auto")
    public ResponseEntity<List<TransitPathResponse>> getAutoPreferredRoute(
            @RequestHeader("Authorization") String token,
            @RequestParam @NotNull @DecimalMin("0.0") double startX,
            @RequestParam @NotNull @DecimalMin("0.0") double startY,
            @RequestParam @NotNull @DecimalMin("0.0") double endX,
            @RequestParam @NotNull @DecimalMin("0.0") double endY
    ) {
        String pureToken = token.replace("Bearer ", "");
        User user = userService.getUserFromToken(pureToken);

        String preferred = preferenceService.getAutoSelectedCategory(user);
        if (preferred == null) {
            return ResponseEntity.noContent().build();
        }

        String tmapResponse = tmapDirectionService.findTransitRoute(startX, startY, endX, endY);
        String startName = tmapDirectionService.getAddressName(startY, startX);
        String endName = tmapDirectionService.getAddressName(endY, endX);
        TransitCategoryResponse categoryResult = transitProcessingService.process(tmapResponse, startName, endName);
        List<TransitPathResponse> filtered = transitFilterService.filterByPreference(categoryResult, preferred);

        return ResponseEntity.ok(filtered);
    }


}