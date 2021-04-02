package dev.restaurantreview.modules.restaurant.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dev.restaurantreview.config.domainObject.GetEnumByAttribute;
import dev.restaurantreview.modules.restaurant.model.Restaurant;
import dev.restaurantreview.modules.restaurant.model.RestaurantInput;
import dev.restaurantreview.modules.restaurant.model.RestaurantView;
import dev.restaurantreview.modules.restaurant.model.TypeOfFood;
import dev.restaurantreview.modules.restaurant.model.TypeOfFoodEnum;
import dev.restaurantreview.modules.review.model.Review;
import dev.restaurantreview.modules.review.service.ReviewListRepository;
import dev.restaurantreview.modules.review.service.ReviewRepository;
import dev.restaurantreview.modules.user.model.User;

@Service
@RequiredArgsConstructor
public class RestaurantService {

	private final RestaurantRepository restaurantRepository;
	private final RestaurantViewRepository restaurantViewRepository;
	private final ReviewListRepository reviewListRepository;
	private final ReviewRepository reviewRepository;
	private final EntityManager em;

	@Transactional
	public Restaurant createRestaurant(RestaurantInput restaurantInput, User owner) {
		Restaurant restaurant = new Restaurant();
		restaurant.setName(restaurantInput.getName());
		restaurant.setOwner(owner);
		restaurant.setDescription(restaurantInput.getDescription());
		restaurant.setTypeOfFood(this.em.getReference(TypeOfFood.class, GetEnumByAttribute.getById(restaurantInput.getTypeOfFood().getId(), TypeOfFoodEnum.class).getId()));
		restaurant.setAddress(restaurantInput.getAddress());
		restaurant.setLatitude(restaurantInput.getLatitude());
		restaurant.setLongitude(restaurantInput.getLongitude());
		return this.restaurantRepository.save(restaurant);
	}

	@Transactional
	public Restaurant updateRestaurant(Long id, RestaurantInput restaurantInput) {
		Restaurant restaurant = this.restaurantRepository.getOne(id);
		restaurant.setName(restaurantInput.getName());
		restaurant.setDescription(restaurantInput.getDescription());
		restaurant.setTypeOfFood(this.em.getReference(TypeOfFood.class, GetEnumByAttribute.getById(restaurantInput.getTypeOfFood().getId(), TypeOfFoodEnum.class).getId()));
		restaurant.setAddress(restaurantInput.getAddress());
		return this.restaurantRepository.save(restaurant);
	}

	public Page<RestaurantView> getOwnedRestaurants(Long ownerId, Double rate, Pageable pageable) {
		if (rate != null) {
			return this.restaurantViewRepository.findAllByRestaurantOwnerIdAndAndAvgRateGreaterThanEqual(ownerId, rate, pageable);
		}
		return this.restaurantViewRepository.findAllByRestaurantOwnerId(ownerId, pageable);
	}

	public Page<RestaurantView> getRestaurants(Double rate, Pageable pageable) {
		if (rate != null) {
			return this.restaurantViewRepository.findAllByAvgRateGreaterThanEqual(rate, pageable);
		}
		return this.restaurantViewRepository.findAll(pageable);
	}

	public Restaurant getRestaurant(Long restaurantId) {
		return this.restaurantRepository.getOne(restaurantId);
	}

	public RestaurantView getRestaurantView(Long restaurantId) {
		return this.restaurantViewRepository.findFirstById(restaurantId);
	}

	public boolean isRestaurantOwner(Long restaurantId, Long userId) {
		Restaurant restaurant = this.restaurantRepository.getOne(restaurantId);
		return userId.equals(restaurant.getOwner().getId());
	}

	public Review getHighestReview(Restaurant restaurant) {
		return this.reviewRepository.findTopByRestaurantOrderByRateDescVisitDateDesc(restaurant);
	}

	public Review getLowestReview(Restaurant restaurant) {
		return this.reviewRepository.findTopByRestaurantOrderByRateAscVisitDateDesc(restaurant);
	}

	public Page<Review> getReviews(Restaurant restaurant, Pageable pageable) {
		return this.reviewListRepository.findAllByRestaurantOrderByVisitDateDesc(restaurant, pageable);
	}

	public Page<Review> getPendingReplies(Long restaurantId, User owner, Pageable pageable) {
		if (this.isRestaurantOwner(restaurantId, owner.getId())) {
			return this.reviewListRepository.findAllByRestaurantAndReplyIsNull(this.getRestaurant(restaurantId), pageable);
		}
		return null;
	}

	@Transactional
	public void deleteRestaurant(Long restaurantId) {
		Restaurant restaurant = this.restaurantRepository.getOne(restaurantId);
		this.reviewRepository.deleteByRestaurant(restaurant);
		this.restaurantRepository.delete(restaurant);
	}

	@Transactional
	public void deleteRestaurantByOwner(User user) {
		List<Restaurant> restaurants = this.restaurantRepository.findAllByOwner(user);
		restaurants.forEach(r -> this.deleteRestaurant(r.getId()));
	}
}
