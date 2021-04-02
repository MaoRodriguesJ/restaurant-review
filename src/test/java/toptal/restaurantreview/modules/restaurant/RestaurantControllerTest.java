package dev.restaurantreview.modules.restaurant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.Collections;

import javax.persistence.EntityManager;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.google.gson.Gson;

import dev.restaurantreview.config.security.SecurityTestUtils;
import dev.restaurantreview.modules.restaurant.model.Restaurant;
import dev.restaurantreview.modules.restaurant.model.RestaurantInput;
import dev.restaurantreview.modules.restaurant.model.RestaurantView;
import dev.restaurantreview.modules.restaurant.model.TypeOfFood;
import dev.restaurantreview.modules.restaurant.model.TypeOfFoodEnum;
import dev.restaurantreview.modules.restaurant.service.RestaurantService;
import dev.restaurantreview.modules.review.model.Review;
import dev.restaurantreview.modules.user.model.User;
import dev.restaurantreview.modules.user.service.UserContext;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RestaurantControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserContext userContext;

	@MockBean
	private RestaurantService restaurantService;

	@MockBean
	private EntityManager em;

	private RestaurantInput restaurantInput;

	@BeforeEach
	public void setup() {
		this.restaurantInput = new RestaurantInput("name", "description", TypeOfFoodEnum.JAPANESE, "address", 1.0, 1.0);
	}

	private String replaceEnumJson(String json) {
		return json.replace("\"JAPANESE\"", "{\"id\":1,\"description\":\"Japanese\"}");
	}

	@Test
	public void tryGetRestaurantsWithoutLogin() throws Exception {
		this.mockMvc.perform(get("/api/restaurants")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}

	@WithMockUser()
	@Test
	public void getRestaurantsAsUser() throws Exception {
		Mockito.when(this.userContext.get()).thenReturn(SecurityTestUtils.USER());
		this.mockMvc.perform(get("/api/restaurants")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
		Mockito.verify(this.restaurantService, times(1)).getRestaurants(any(), any());
		Mockito.verify(this.restaurantService, times(0)).getOwnedRestaurants(any(), any(), any());
	}

	@WithMockUser(value = "admin", roles = { "ADMIN" })
	@Test
	public void getRestaurantsAsAdmin() throws Exception {
		Mockito.when(this.userContext.get()).thenReturn(SecurityTestUtils.ADMIN());
		this.mockMvc.perform(get("/api/restaurants")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
		Mockito.verify(this.restaurantService, times(1)).getRestaurants(any(), any());
		Mockito.verify(this.restaurantService, times(0)).getOwnedRestaurants(any(), any(), any());
	}

	@WithMockUser(value = "owner", roles = { "OWNER" })
	@Test
	public void getRestaurantsAsOwner() throws Exception {
		Mockito.when(this.userContext.get()).thenReturn(SecurityTestUtils.OWNER());
		this.mockMvc.perform(get("/api/restaurants")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
		Mockito.verify(this.restaurantService, times(0)).getRestaurants(any(), any());
		Mockito.verify(this.restaurantService, times(1)).getOwnedRestaurants(any(), any(), any());
	}

	@Test
	public void tryCreateRestaurantsWithoutLogin() throws Exception {
		this.mockMvc.perform(post("/api/restaurants")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}

	@WithMockUser()
	@Test
	public void tryCreateRestaurantsAsAdmin() throws Exception {
		this.mockMvc.perform(post("/api/restaurants")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(value = "admin", roles = { "ADMIN" })
	@Test
	public void tryCreateRestaurantsAsUser() throws Exception {
		this.mockMvc.perform(post("/api/restaurants")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(value = "owner", roles = { "OWNER" })
	@Test
	public void createRestaurantsAsOwner() throws Exception {
		TypeOfFood typeOfFood = new TypeOfFood();
		this.em.persist(typeOfFood);
		Restaurant restaurant = new Restaurant();
		this.em.persist(restaurant);
		Mockito.when(this.restaurantService.createRestaurant(Mockito.any(), Mockito.any())).thenReturn(restaurant);
		Gson gson = new Gson();
		String json = this.replaceEnumJson(gson.toJson(this.restaurantInput));
		this.mockMvc.perform(post("/api/restaurants")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
		Mockito.verify(this.restaurantService, times(1)).createRestaurant(any(), any());
	}

	@Test
	public void tryGetPendingRepliesWithoutLogin() throws Exception {
		this.mockMvc.perform(get("/api/restaurants/1/pendingReplies")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}

	@WithMockUser()
	@Test
	public void tryGetPendingRepliesAsUser() throws Exception {
		this.mockMvc.perform(get("/api/restaurants/1/pendingReplies")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(value = "admin", roles = { "ADMIN" })
	@Test
	public void tryGetPendingRepliesAsAdmin() throws Exception {
		this.mockMvc.perform(get("/api/restaurants/1/pendingReplies")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(value = "owner", roles = { "OWNER" })
	@Test
	public void getPendingRepliesAsOwner() throws Exception {
		when(this.userContext.get()).thenReturn(SecurityTestUtils.OWNER());
		when(this.restaurantService.getPendingReplies(any(), any(), any())).thenReturn(new PageImpl<Review>(Collections.emptyList()));
		this.mockMvc.perform(get("/api/restaurants/1/pendingReplies")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@WithMockUser(value = "owner", roles = { "OWNER" })
	@Test
	public void getPendingRepliesAsNotTheOwnerShouldBeUnprocessable() throws Exception {
		when(this.userContext.get()).thenReturn(SecurityTestUtils.OWNER());
		when(this.restaurantService.getPendingReplies(any(), any(), any())).thenReturn(null);
		this.mockMvc.perform(get("/api/restaurants/1/pendingReplies")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	public void tryGetRestaurantDetailWithoutLogin() throws Exception {
		this.mockMvc.perform(get("/api/restaurants/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}

	@WithMockUser()
	@Test
	public void getRestaurantsDetailAsUser() throws Exception {
		Mockito.when(this.userContext.get()).thenReturn(SecurityTestUtils.USER());
		this.mockMvc.perform(get("/api/restaurants/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
		Mockito.verify(this.restaurantService, times(1)).getRestaurantView(any());
	}

	@WithMockUser(value = "admin", roles = { "ADMIN" })
	@Test
	public void getRestaurantsDetailAsAdmin() throws Exception {
		Mockito.when(this.userContext.get()).thenReturn(SecurityTestUtils.ADMIN());
		this.mockMvc.perform(get("/api/restaurants/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
		Mockito.verify(this.restaurantService, times(1)).getRestaurantView(any());
	}

	@WithMockUser(value = "owner", roles = { "OWNER" })
	@Test
	public void getRestaurantsDetailAsOwner() throws Exception {
		RestaurantView restaurant = new RestaurantView();
		restaurant.setRestaurantOwnerId(1l);
		Mockito.when(this.restaurantService.getRestaurantView(any())).thenReturn(restaurant);
		Mockito.when(this.userContext.get()).thenReturn(SecurityTestUtils.OWNER());
		this.mockMvc.perform(get("/api/restaurants/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
		Mockito.verify(this.restaurantService, times(1)).getRestaurantView(any());
	}

	@WithMockUser(value = "owner", roles = { "OWNER" })
	@Test
	public void getRestaurantsDetailAsNotOwner() throws Exception {
		RestaurantView restaurant = new RestaurantView();
		restaurant.setRestaurantOwnerId(2l);
		Mockito.when(this.restaurantService.getRestaurantView(any())).thenReturn(restaurant);
		Mockito.when(this.userContext.get()).thenReturn(SecurityTestUtils.OWNER());
		this.mockMvc.perform(get("/api/restaurants/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is("you must be the owner of the restaurant")));
	}

	@Test
	public void tryGetRestaurantReviewsWithoutLogin() throws Exception {
		this.mockMvc.perform(get("/api/restaurants/1/reviews")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}

	@WithMockUser()
	@Test
	public void getRestaurantsReviewAsUser() throws Exception {
		Mockito.when(this.userContext.get()).thenReturn(SecurityTestUtils.USER());
		this.mockMvc.perform(get("/api/restaurants/1/reviews")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
		Mockito.verify(this.restaurantService, times(1)).getRestaurant(any());
		Mockito.verify(this.restaurantService, times(1)).getReviews(any(), any());
	}

	@WithMockUser(value = "admin", roles = { "ADMIN" })
	@Test
	public void getRestaurantsReviewsAsAdmin() throws Exception {
		Mockito.when(this.userContext.get()).thenReturn(SecurityTestUtils.ADMIN());
		this.mockMvc.perform(get("/api/restaurants/1/reviews")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
		Mockito.verify(this.restaurantService, times(1)).getRestaurant(any());
		Mockito.verify(this.restaurantService, times(1)).getReviews(any(), any());
	}

	@WithMockUser(value = "owner", roles = { "OWNER" })
	@Test
	public void getRestaurantsReviewsAsOwner() throws Exception {
		Restaurant restaurant = new Restaurant();
		restaurant.setOwner(SecurityTestUtils.OWNER());
		Mockito.when(this.restaurantService.getRestaurant(any())).thenReturn(restaurant);
		Mockito.when(this.userContext.get()).thenReturn(SecurityTestUtils.OWNER());
		this.mockMvc.perform(get("/api/restaurants/1/reviews")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
		Mockito.verify(this.restaurantService, times(1)).getRestaurant(any());
		Mockito.verify(this.restaurantService, times(1)).getReviews(any(), any());
	}

	@WithMockUser(value = "owner", roles = { "OWNER" })
	@Test
	public void getRestaurantsReviewsAsNotOwner() throws Exception {
		Restaurant restaurant = new Restaurant();
		User user = new User();
		user.setId(2l);
		restaurant.setOwner(user);
		Mockito.when(this.restaurantService.getRestaurant(any())).thenReturn(restaurant);
		Mockito.when(this.userContext.get()).thenReturn(SecurityTestUtils.OWNER());
		this.mockMvc.perform(get("/api/restaurants/1/reviews")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is("you must be the owner of the restaurant")));
		Mockito.verify(this.restaurantService, times(1)).getRestaurant(any());
		Mockito.verify(this.restaurantService, times(0)).getReviews(any(), any());
	}

	@Test
	public void tryGetRestaurantHighestReviewWithoutLogin() throws Exception {
		this.mockMvc.perform(get("/api/restaurants/1/highestReview")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}

	@WithMockUser()
	@Test
	public void getRestaurantsHighestReviewAsUser() throws Exception {
		Mockito.when(this.userContext.get()).thenReturn(SecurityTestUtils.USER());
		this.mockMvc.perform(get("/api/restaurants/1/highestReview")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
		Mockito.verify(this.restaurantService, times(1)).getRestaurant(any());
		Mockito.verify(this.restaurantService, times(1)).getHighestReview(any());
	}

	@WithMockUser(value = "admin", roles = { "ADMIN" })
	@Test
	public void getRestaurantsHighestReviewAsAdmin() throws Exception {
		Mockito.when(this.userContext.get()).thenReturn(SecurityTestUtils.ADMIN());
		this.mockMvc.perform(get("/api/restaurants/1/highestReview")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
		Mockito.verify(this.restaurantService, times(1)).getRestaurant(any());
		Mockito.verify(this.restaurantService, times(1)).getHighestReview(any());
	}

	@WithMockUser(value = "owner", roles = { "OWNER" })
	@Test
	public void getRestaurantsHighestReviewAsOwner() throws Exception {
		Restaurant restaurant = new Restaurant();
		restaurant.setOwner(SecurityTestUtils.OWNER());
		Mockito.when(this.restaurantService.getRestaurant(any())).thenReturn(restaurant);
		Mockito.when(this.userContext.get()).thenReturn(SecurityTestUtils.OWNER());
		this.mockMvc.perform(get("/api/restaurants/1/highestReview")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
		Mockito.verify(this.restaurantService, times(1)).getRestaurant(any());
		Mockito.verify(this.restaurantService, times(1)).getHighestReview(any());
	}

	@WithMockUser(value = "owner", roles = { "OWNER" })
	@Test
	public void getRestaurantsHighestReviewAsNotOwner() throws Exception {
		Restaurant restaurant = new Restaurant();
		User user = new User();
		user.setId(2l);
		restaurant.setOwner(user);
		Mockito.when(this.restaurantService.getRestaurant(any())).thenReturn(restaurant);
		Mockito.when(this.userContext.get()).thenReturn(SecurityTestUtils.OWNER());
		this.mockMvc.perform(get("/api/restaurants/1/highestReview")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is("you must be the owner of the restaurant")));
		Mockito.verify(this.restaurantService, times(1)).getRestaurant(any());
		Mockito.verify(this.restaurantService, times(0)).getHighestReview(any());
	}

	@Test
	public void tryGetRestaurantLowestReviewWithoutLogin() throws Exception {
		this.mockMvc.perform(get("/api/restaurants/1/lowestReview")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}

	@WithMockUser()
	@Test
	public void getRestaurantsLowestReviewAsUser() throws Exception {
		Mockito.when(this.userContext.get()).thenReturn(SecurityTestUtils.USER());
		this.mockMvc.perform(get("/api/restaurants/1/lowestReview")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
		Mockito.verify(this.restaurantService, times(1)).getRestaurant(any());
		Mockito.verify(this.restaurantService, times(1)).getLowestReview(any());
	}

	@WithMockUser(value = "admin", roles = { "ADMIN" })
	@Test
	public void getRestaurantsLowestReviewAsAdmin() throws Exception {
		Mockito.when(this.userContext.get()).thenReturn(SecurityTestUtils.ADMIN());
		this.mockMvc.perform(get("/api/restaurants/1/lowestReview")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
		Mockito.verify(this.restaurantService, times(1)).getRestaurant(any());
		Mockito.verify(this.restaurantService, times(1)).getLowestReview(any());
	}

	@WithMockUser(value = "owner", roles = { "OWNER" })
	@Test
	public void getRestaurantsLowestReviewAsOwner() throws Exception {
		Restaurant restaurant = new Restaurant();
		restaurant.setOwner(SecurityTestUtils.OWNER());
		Mockito.when(this.restaurantService.getRestaurant(any())).thenReturn(restaurant);
		Mockito.when(this.userContext.get()).thenReturn(SecurityTestUtils.OWNER());
		this.mockMvc.perform(get("/api/restaurants/1/lowestReview")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
		Mockito.verify(this.restaurantService, times(1)).getRestaurant(any());
		Mockito.verify(this.restaurantService, times(1)).getLowestReview(any());
	}

	@WithMockUser(value = "owner", roles = { "OWNER" })
	@Test
	public void getRestaurantsLowestReviewAsNotOwner() throws Exception {
		Restaurant restaurant = new Restaurant();
		User user = new User();
		user.setId(2l);
		restaurant.setOwner(user);
		Mockito.when(this.restaurantService.getRestaurant(any())).thenReturn(restaurant);
		Mockito.when(this.userContext.get()).thenReturn(SecurityTestUtils.OWNER());
		this.mockMvc.perform(get("/api/restaurants/1/lowestReview")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is("you must be the owner of the restaurant")));
		Mockito.verify(this.restaurantService, times(1)).getRestaurant(any());
		Mockito.verify(this.restaurantService, times(0)).getLowestReview(any());
	}

}