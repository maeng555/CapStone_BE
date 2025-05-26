package org.example.capstone_project.domain.map.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "map",description = "사용자 패턴 분석 길찾기 api")
@Validated //컨트롤에서 오류 거름
public class TmapDirectionController {

    private final TmapDirectionService tmapDirectionService;
    private final TransitProcessingService transitProcessingService;
    private final TransitFilterService transitFilterService;
    private final BusRealtimeService busRealtimeService;
    private final UserCategoryPreferenceService preferenceService;
    private final UserService userService;

    // tmap api 모든내용
    @Operation(summary = "전체 대중교통 경로 JSON 반환", description = "Tmap 대중교통 API를 호출하여 전체 경로 데이터를 JSON 문자열로 반환합니다.")
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
    @Operation(summary = "분류된 대중교통 경로 전체 다 보기", description = "Tmap API 호출 후, 걷기/버스/지하철/최소요금/최소환승/추천 등으로 분류된 경로를 반환합니다.")
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
    @Operation(summary = "카테고리 클릭 시 경로 및 클릭 횟수 증가 처리", description = "버튼 클릭 시 선호 카테고리 클릭 횟수를 증가시키고 해당 카테고리의 경로 정보를 반환합니다.")
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
    @Operation(summary = "사용자 선호 기반 자동 경로 반환", description = "선호도가 3 이상인 카테고리가 있을 경우, 해당 선호 경로를 자동으로 반환합니다.")
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