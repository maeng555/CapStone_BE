package org.example.capstone_project.domain.food.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AiFoodResponse {
    private String recommendations;
}
