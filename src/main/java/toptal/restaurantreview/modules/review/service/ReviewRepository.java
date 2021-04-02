package dev.restaurantreview.modules.review.service;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.restaurantreview.modules.restaurant.model.Restaurant;
import dev.restaurantreview.modules.review.model.Review;
import dev.restaurantreview.modules.user.model.User;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	Review findTopByRestaurantOrderByRateDescVisitDateDesc(Restaurant restaurant);

	Review findTopByRestaurantOrderByRateAscVisitDateDesc(Restaurant restaurant);

	void deleteByRestaurant(Restaurant restaurant);
	
	void deleteByAuthor(User user);
}
