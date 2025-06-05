package org.example.capstone_project.domain.food.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.capstone_project.domain.food.entity.Favorite;

@Data
@AllArgsConstructor
public class FavoriteResponse {
    private String placeName;
    private String address;
    private String placeUrl;
    private double x;
    private double y;

    public static FavoriteResponse from(Favorite favorite) {
        return new FavoriteResponse(
                favorite.getPlaceName(),
                favorite.getAddress(),
                favorite.getPlaceUrl(),
                favorite.getX(),
                favorite.getY()
        );
    }
}
