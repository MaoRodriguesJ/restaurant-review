package dev.restaurantreview.modules.review.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import dev.restaurantreview.modules.restaurant.model.Restaurant;
import dev.restaurantreview.modules.restaurant.service.RestaurantService;
import dev.restaurantreview.modules.review.model.Review;
import dev.restaurantreview.modules.review.model.ReviewInput;
import dev.restaurantreview.modules.user.model.User;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

	@Mock
	private RestaurantService restaurantService;

	@Mock
	private ReviewRepository reviewRepository;

	private ReviewService reviewService;

	@BeforeEach
	public void setup() {
		this.reviewService = new ReviewService(this.restaurantService, this.reviewRepository);
	}

	@Test
	public void verifyCallSave() {
		when(this.restaurantService.getRestaurant(any())).thenReturn(new Restaurant());
		this.reviewService.saveReview(new ReviewInput(), new User());
		Mockito.verify(this.reviewRepository, times(1)).save(any());
	}

	@Test
	public void ownerShouldReply() {
		Review review = new Review();
		Restaurant restaurant = new Restaurant();
		restaurant.setId(1l);
		review.setRestaurant(restaurant);
		User owner = new User();
		owner.setId(1l);
		when(this.restaurantService.isRestaurantOwner(any(), any())).thenReturn(true);
		when(this.reviewRepository.save(any())).thenReturn(review);
		Assertions.assertNotNull(this.reviewService.ownerReply(review, owner, "reply"));
	}

	@Test
	public void notOwnerShouldNotReply() {
		Review review = new Review();
		Restaurant restaurant = new Restaurant();
		restaurant.setId(1l);
		review.setRestaurant(restaurant);
		User owner = new User();
		owner.setId(1l);
		when(this.restaurantService.isRestaurantOwner(any(), any())).thenReturn(false);
		Review review2 = this.reviewService.ownerReply(review, owner, "reply");
		Assertions.assertNull(review2);
		Mockito.verify(this.reviewRepository, times(0)).save(any());
	}
}