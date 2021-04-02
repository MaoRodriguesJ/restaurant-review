package dev.restaurantreview.modules.restaurant.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.restaurantreview.modules.restaurant.model.Restaurant;
import dev.restaurantreview.modules.user.model.User;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
	List<Restaurant> findAllByOwner(User user);
}
