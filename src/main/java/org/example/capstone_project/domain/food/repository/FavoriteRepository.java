package org.example.capstone_project.domain.food.repository;

import org.example.capstone_project.domain.food.entity.Favorite;
import org.example.capstone_project.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUser(User user);
    Optional<Favorite> findByUserAndPlaceName(User user, String placeName);
}
