package dev.restaurantreview.modules.restaurant.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

import java.util.Collections;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import dev.restaurantreview.modules.restaurant.model.Restaurant;
import dev.restaurantreview.modules.restaurant.model.RestaurantInput;
import dev.restaurantreview.modules.restaurant.model.TypeOfFoodEnum;
import dev.restaurantreview.modules.review.model.Review;
import dev.restaurantreview.modules.review.service.ReviewListRepository;
import dev.restaurantreview.modules.review.service.ReviewRepository;
import dev.restaurantreview.modules.user.model.User;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class RestaurantServiceTest {

	@Mock
	private RestaurantRepository restaurantRepository;

	@Mock
	private RestaurantViewRepository restaurantViewRepository;

	@Mock
	private ReviewListRepository reviewListRepository;

	@Mock
	private ReviewRepository reviewRepository;

	@Mock
	private EntityManager em;

	private RestaurantService restaurantService;
	private final Pageable pageable = PageRequest.of(0, 5);

	@BeforeEach
	public void setup() {
		this.restaurantService = new RestaurantService(this.restaurantRepository,
				this.restaurantViewRepository,
				this.reviewListRepository,
				this.reviewRepository,
				this.em);
	}

	@Test
	public void verifyCallSave() {
		this.restaurantService.createRestaurant(new RestaurantInput("name", "description", TypeOfFoodEnum.JAPANESE, "address", 1.0, 1.0), new User());
		Mockito.verify(this.restaurantRepository, times(1)).save(any());
	}

	@Test
	public void ifRateCallFilteredRestaurants() {
		this.restaurantService.getRestaurants(2.0, this.pageable);
		Mockito.verify(this.restaurantViewRepository, times(1)).findAllByAvgRateGreaterThanEqual(any(), any());
		Mockito.verify(this.restaurantViewRepository, times(0)).findAll(any(Pageable.class));
	}

	@Test
	public void ifNotRateCallAllRestaurants() {
		this.restaurantService.getRestaurants(null, this.pageable);
		Mockito.verify(this.restaurantViewRepository, times(0)).findAllByAvgRateGreaterThanEqual(any(), any());
		Mockito.verify(this.restaurantViewRepository, times(1)).findAll(any(Pageable.class));
	}

	@Test
	public void ifRateCallFilteredOwnedRestaurants() {
		this.restaurantService.getOwnedRestaurants(1L, 2.0, this.pageable);
		Mockito.verify(this.restaurantViewRepository, times(1)).findAllByRestaurantOwnerIdAndAndAvgRateGreaterThanEqual(any(), any(), any());
		Mockito.verify(this.restaurantViewRepository, times(0)).findAllByRestaurantOwnerId(any(), any(Pageable.class));
	}

	@Test
	public void ifNotRateCallAllOwnedRestaurants() {
		this.restaurantService.getOwnedRestaurants(1L, null, this.pageable);
		Mockito.verify(this.restaurantViewRepository, times(0)).findAllByRestaurantOwnerIdAndAndAvgRateGreaterThanEqual(any(), any(), any());
		Mockito.verify(this.restaurantViewRepository, times(1)).findAllByRestaurantOwnerId(any(), any());
	}

	@Test
	public void getRestaurantCall() {
		this.restaurantService.getRestaurant(1L);
		Mockito.verify(this.restaurantRepository, times(1)).getOne(any());
	}

	@Test
	public void getRestaurantViewCall() {
		this.restaurantService.getRestaurantView(1L);
		Mockito.verify(this.restaurantViewRepository, times(1)).findFirstById(any());
	}

	@Test
	public void getRestaurantHighestReview() {
		this.restaurantService.getHighestReview(new Restaurant());
		Mockito.verify(this.reviewRepository, times(1)).findTopByRestaurantOrderByRateDescVisitDateDesc(any());
	}

	@Test
	public void getRestaurantLowestReview() {
		this.restaurantService.getLowestReview(new Restaurant());
		Mockito.verify(this.reviewRepository, times(1)).findTopByRestaurantOrderByRateAscVisitDateDesc(any());
	}

	@Test
	public void getRestaurantReviewList() {
		this.restaurantService.getReviews(new Restaurant(), this.pageable);
		Mockito.verify(this.reviewListRepository, times(1)).findAllByRestaurantOrderByVisitDateDesc(any(), any(Pageable.class));
	}

	@Test
	public void ownerOfRestaurantShouldTrue() {
		Restaurant restaurant = new Restaurant();
		User owner = new User();
		owner.setId(1l);
		restaurant.setOwner(owner);
		Mockito.when(this.restaurantRepository.getOne(any())).thenReturn(restaurant);
		Assertions.assertEquals(true, this.restaurantService.isRestaurantOwner(1l, 1l));
	}

	@Test
	public void notOwnerOfRestaurantShouldFalse() {
		Restaurant restaurant = new Restaurant();
		User owner = new User();
		owner.setId(2l);
		restaurant.setOwner(owner);
		Mockito.when(this.restaurantRepository.getOne(any())).thenReturn(restaurant);
		Assertions.assertEquals(false, this.restaurantService.isRestaurantOwner(1l, 1l));
	}

	@Test
	public void ownerPendingReplies() {
		Restaurant restaurant = new Restaurant();
		User owner = new User();
		owner.setId(1l);
		restaurant.setOwner(owner);
		Mockito.when(this.restaurantRepository.getOne(any())).thenReturn(restaurant);
		Mockito.when(this.reviewListRepository.findAllByRestaurantAndReplyIsNull(any(), any(Pageable.class))).thenReturn(new PageImpl<Review>(Collections.emptyList()));
		Assertions.assertNotNull(this.restaurantService.getPendingReplies(1l, owner, this.pageable));
	}

	@Test
	public void notOwnerNotReturnPendingReplies() {
		Restaurant restaurant = new Restaurant();
		User owner = new User();
		owner.setId(1l);
		restaurant.setOwner(owner);
		User notOwner = new User();
		notOwner.setId(2l);
		Mockito.when(this.restaurantRepository.getOne(any())).thenReturn(restaurant);
		Page<Review> reviews = this.restaurantService.getPendingReplies(1l, notOwner, this.pageable);
		Assertions.assertNull(reviews);
		Mockito.verify(this.reviewListRepository, times(0)).findAllByRestaurantAndReplyIsNull(any(), any(Pageable.class));
	}

}