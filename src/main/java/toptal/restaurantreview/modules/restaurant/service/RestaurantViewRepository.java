package dev.restaurantreview.modules.restaurant.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import dev.restaurantreview.modules.restaurant.model.RestaurantView;

public interface RestaurantViewRepository extends PagingAndSortingRepository<RestaurantView, Long> {
	Page<RestaurantView> findAllByRestaurantOwnerId(Long restaurantOwnerId, Pageable pageable);

	Page<RestaurantView> findAllByRestaurantOwnerIdAndAndAvgRateGreaterThanEqual(Long restaurantOwnerId, Double rate, Pageable pageable);

	Page<RestaurantView> findAllByAvgRateGreaterThanEqual(Double rate, Pageable pageable);

	RestaurantView findFirstById(Long id);
}
