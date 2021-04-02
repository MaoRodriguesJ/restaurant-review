package dev.restaurantreview.modules.review.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import dev.restaurantreview.modules.restaurant.model.Restaurant;
import dev.restaurantreview.modules.review.model.Review;

public interface ReviewListRepository extends PagingAndSortingRepository<Review, Long> {
	Page<Review> findAllByRestaurantAndReplyIsNull(Restaurant restaurant, Pageable pageable);

	Page<Review> findAllByRestaurantOrderByVisitDateDesc(Restaurant restaurant, Pageable pageable);
}
