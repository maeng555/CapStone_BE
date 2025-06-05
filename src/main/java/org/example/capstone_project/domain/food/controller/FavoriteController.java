package org.example.capstone_project.domain.food.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.capstone_project.domain.food.dto.FavoriteResponse;
import org.example.capstone_project.domain.food.dto.KakaoPlaceResponse;
import org.example.capstone_project.domain.food.entity.Favorite;
import org.example.capstone_project.domain.food.service.FavoriteService;
import org.example.capstone_project.domain.user.entity.User;
import org.example.capstone_project.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final UserService userService;

    @PostMapping("/toggle")
    @Operation(summary = "즐겨찾기 토글", description = "좋아요 누르면 저장, 다시 누르면 삭제됩니다.")
    public ResponseEntity<Void> toggleFavorite(
            @RequestHeader("Authorization") String token,
            @RequestBody KakaoPlaceResponse place
    ) {
        String pureToken = token.replace("Bearer ", "");
        User user = userService.getUserFromToken(pureToken);
        favoriteService.toggleFavorite(user, place);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "즐겨찾기 목록 조회", description = "사용자의 저장된 즐겨찾기 목록을 조회합니다.")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(
            @RequestHeader("Authorization") String token
    ) {
        String pureToken = token.replace("Bearer ", "");
        User user = userService.getUserFromToken(pureToken);
        List<FavoriteResponse> responses = favoriteService.getFavorites(user).stream()
                .map(FavoriteResponse::from)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @DeleteMapping
    @Operation(summary = "즐겨찾기 개별 삭제", description = "장소 이름 즉 placeName 으로 즐겨찾기 삭제")
    public ResponseEntity<Void> deleteFavorite(
            @RequestHeader("Authorization") String token,
            @RequestParam String placeName
    ) {
        String pureToken = token.replace("Bearer ", "");
        User user = userService.getUserFromToken(pureToken);
        favoriteService.deleteFavorite(user, placeName);
        return ResponseEntity.noContent().build();
    }
}