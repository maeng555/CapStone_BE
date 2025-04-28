package org.example.capstone_project.domain.map.controller;

import lombok.RequiredArgsConstructor;
import org.example.capstone_project.domain.map.service.TmapDirectionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tmap")
public class TmapDirectionController {

    private final TmapDirectionService tmapDirectionService;

    @GetMapping("/transit")
    public String getTransitRoute(
            @RequestParam double startX,
            @RequestParam double startY,
            @RequestParam double endX,
            @RequestParam double endY
    ) {
        return tmapDirectionService.findTransitRoute(startX, startY, endX, endY);
    }
}