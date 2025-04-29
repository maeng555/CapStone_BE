package org.example.capstone_project.domain.map.controller;

import lombok.RequiredArgsConstructor;
import org.example.capstone_project.domain.map.service.OdsayDirectionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/odsay")
public class OdsayDirectionController {

    private final OdsayDirectionService odsayDirectionService;

    @GetMapping("/transit")
    public String getTransitRoute(
            @RequestParam double startX,//dkssuds
            @RequestParam double startY,
            @RequestParam double goalX,
            @RequestParam double goalY
    ) {
        return odsayDirectionService.findTransitRoute(startX, startY, goalX, goalY);
    }
}
