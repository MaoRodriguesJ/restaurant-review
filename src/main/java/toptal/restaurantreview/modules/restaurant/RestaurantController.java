package dev.restaurantreview.modules.restaurant;

import static dev.restaurantreview.config.accesspolicy.AccessPolicy.ADMIN;
import static dev.restaurantreview.config.accesspolicy.AccessPolicy.OWNER;
import static dev.restaurantreview.config.accesspolicy.AccessPolicy.USER;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import dev.restaurantreview.config.accesspolicy.AccessPolicy.HasAnyRole;
import dev.restaurantreview.config.accesspolicy.RoleEnum;
import dev.restaurantreview.modules.restaurant.model.Restaurant;
import dev.restaurantreview.modules.restaurant.model.RestaurantInput;
import dev.restaurantreview.modules.restaurant.model.RestaurantView;
import dev.restaurantreview.modules.restaurant.service.RestaurantService;
import dev.restaurantreview.modules.review.model.Review;
import dev.restaurantreview.modules.user.model.User;
import dev.restaurantreview.modules.user.service.UserContext;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/restaurants")
public class RestaurantController {

	private final UserContext userContext;
	private final RestaurantService restaurantService;

	@GetMapping
	@HasAnyRole({ ADMIN, USER, OWNER })
	public ResponseEntity index(Double rate, Pageable pageParams) {
		User currentUser = this.userContext.get();
		if (currentUser.getRoles().stream().anyMatch(t -> t.getId().equals(RoleEnum.ROLE_OWNER.getId()))) {
			return new ResponseEntity(this.restaurantService.getOwnedRestaurants(currentUser.getId(), rate, pageParams), HttpStatus.OK);
		}
		return new ResponseEntity(this.restaurantService.getRestaurants(rate, pageParams), HttpStatus.OK);
	}

	@PostMapping
	@HasAnyRole({ OWNER })
	public ResponseEntity store(@Valid @RequestBody RestaurantInput restaurantInput) {
		return new ResponseEntity<>(this.restaurantService.createRestaurant(restaurantInput, this.userContext.get()).getId(), HttpStatus.OK);
	}

	@GetMapping({ "/{id}" })
	@HasAnyRole({ ADMIN, USER, OWNER })
	public ResponseEntity show(@PathVariable Long id) {
		RestaurantView restaurant = this.restaurantService.getRestaurantView(id);
		User currentUser = this.userContext.get();
		if (currentUser.getRoles().stream().anyMatch(t -> t.getId().equals(RoleEnum.ROLE_OWNER.getId()))
				&& !currentUser.getId().equals(restaurant.getRestaurantOwnerId())) {
			Map<String, String> errors = new HashMap<>();
			errors.put("id", "you must be the owner of the restaurant");
			return ResponseEntity.badRequest().body(errors);
		}
		return new ResponseEntity<>(restaurant, HttpStatus.OK);
	}

	@PutMapping({ "/{id}" })
	@HasAnyRole({ ADMIN })
	public ResponseEntity update(@PathVariable Long id, @Valid @RequestBody RestaurantInput restaurantInput) {
		return new ResponseEntity<>(this.restaurantService.updateRestaurant(id, restaurantInput).getId(), HttpStatus.OK);
	}

	@GetMapping({ "/{id}/reviews" })
	@HasAnyRole({ ADMIN, USER, OWNER })
	public ResponseEntity showReviews(@PathVariable Long id, Pageable pageParams) {
		Restaurant restaurant = this.restaurantService.getRestaurant(id);
		User currentUser = this.userContext.get();
		if (currentUser.getRoles().stream().anyMatch(t -> t.getId().equals(RoleEnum.ROLE_OWNER.getId()))
				&& !currentUser.getId().equals(restaurant.getOwner().getId())) {
			Map<String, String> errors = new HashMap<>();
			errors.put("id", "you must be the owner of the restaurant");
			return ResponseEntity.badRequest().body(errors);
		}
		return new ResponseEntity<>(this.restaurantService.getReviews(restaurant, pageParams), HttpStatus.OK);
	}

	@GetMapping({ "/{id}/highestReview" })
	@HasAnyRole({ ADMIN, USER, OWNER })
	public ResponseEntity showHighestReview(@PathVariable Long id) {
		Restaurant restaurant = this.restaurantService.getRestaurant(id);
		User currentUser = this.userContext.get();
		if (currentUser.getRoles().stream().anyMatch(t -> t.getId().equals(RoleEnum.ROLE_OWNER.getId()))
				&& !currentUser.getId().equals(restaurant.getOwner().getId())) {
			Map<String, String> errors = new HashMap<>();
			errors.put("id", "you must be the owner of the restaurant");
			return ResponseEntity.badRequest().body(errors);
		}
		return new ResponseEntity<>(this.restaurantService.getHighestReview(restaurant), HttpStatus.OK);
	}

	@GetMapping({ "/{id}/lowestReview" })
	@HasAnyRole({ ADMIN, USER, OWNER })
	public ResponseEntity showLowestReview(@PathVariable Long id) {
		Restaurant restaurant = this.restaurantService.getRestaurant(id);
		User currentUser = this.userContext.get();
		if (currentUser.getRoles().stream().anyMatch(t -> t.getId().equals(RoleEnum.ROLE_OWNER.getId()))
				&& !currentUser.getId().equals(restaurant.getOwner().getId())) {
			Map<String, String> errors = new HashMap<>();
			errors.put("id", "you must be the owner of the restaurant");
			return ResponseEntity.badRequest().body(errors);
		}
		return new ResponseEntity<>(this.restaurantService.getLowestReview(restaurant), HttpStatus.OK);
	}

	@GetMapping({ "/{id}/pendingReplies" })
	@HasAnyRole({ OWNER })
	public ResponseEntity pendingReplies(@PathVariable Long id, Pageable pageParams) {
		Page<Review> reviews = this.restaurantService.getPendingReplies(id, this.userContext.get(), pageParams);
		if (reviews == null) {
			Map<String, String> errors = new HashMap<>();
			errors.put("id", "you must be the owner of the restaurant of the review");
			return ResponseEntity.badRequest().body(errors);
		}
		return new ResponseEntity<>(reviews, HttpStatus.OK);
	}

	@DeleteMapping({ "/{id}" })
	@HasAnyRole({ ADMIN })
	public ResponseEntity delete(@PathVariable Long id) {
		this.restaurantService.deleteRestaurant(id);
		return new ResponseEntity<>(true, HttpStatus.OK);
	}
}
