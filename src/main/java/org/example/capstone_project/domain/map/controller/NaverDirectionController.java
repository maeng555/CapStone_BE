package org.example.capstone_project.domain.map.controller;

import lombok.RequiredArgsConstructor;
import org.example.capstone_project.domain.map.service.NaverDirectionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/naver")
public class NaverDirectionController {

   private final NaverDirectionService naverDirectionService;

    @GetMapping("/transit")
    public String getTransitRoute(
            @RequestParam double startX,
            @RequestParam double startY,
            @RequestParam double goalX,
            @RequestParam double goalY
    ) {
        return naverDirectionService.findTransitRoute(startX, startY, goalX, goalY);
    }
}
