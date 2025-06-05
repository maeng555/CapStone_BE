package org.example.capstone_project.domain.food.service;

import lombok.RequiredArgsConstructor;
import org.example.capstone_project.domain.food.dto.KakaoPlaceResponse;
import org.example.capstone_project.domain.food.entity.Favorite;
import org.example.capstone_project.domain.food.repository.FavoriteRepository;
import org.example.capstone_project.domain.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;

    public void toggleFavorite(User user, KakaoPlaceResponse place) {
        Optional<Favorite> existing = favoriteRepository.findByUserAndPlaceName(user, place.getPlaceName());

        if (existing.isPresent()) {
            favoriteRepository.delete(existing.get());  // 이미 있으면 삭제
        } else {
            Favorite favorite = new Favorite();
            favorite.setUser(user);
            favorite.setPlaceName(place.getPlaceName());
            favorite.setAddress(place.getAddressName());
            favorite.setPlaceUrl(place.getPlaceUrl());
            favorite.setX(Double.parseDouble(place.getX()));
            favorite.setY(Double.parseDouble(place.getY()));


            favoriteRepository.save(favorite);  // 없으면 저장
        }
    }

    public List<Favorite> getFavorites(User user) {
        return favoriteRepository.findByUser(user);
    }

    public void deleteFavorite(User user, String placeName) {
        Optional<Favorite> favorite = favoriteRepository.findByUserAndPlaceName(user, placeName);
        favorite.ifPresent(favoriteRepository::delete);
    }
}
