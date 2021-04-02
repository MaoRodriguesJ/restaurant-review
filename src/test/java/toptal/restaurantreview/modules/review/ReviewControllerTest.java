package dev.restaurantreview.modules.review;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.time.LocalDate;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import dev.restaurantreview.config.security.SecurityTestUtils;
import dev.restaurantreview.modules.review.model.Review;
import dev.restaurantreview.modules.review.service.ReviewService;
import dev.restaurantreview.modules.user.service.UserContext;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ReviewControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserContext userContext;

	@MockBean
	private ReviewService reviewService;

	@Test
	public void tryPostReviewWithouLogin() throws Exception {
		this.mockMvc.perform(post("/api/reviews")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}

	@WithMockUser(value = "admin", roles = { "ADMIN" })
	@Test
	public void tryPostReviewAsAdmin() throws Exception {
		this.mockMvc.perform(post("/api/reviews")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(value = "owner", roles = { "OWNER" })
	@Test
	public void tryPostReviewAsOwner() throws Exception {
		this.mockMvc.perform(post("/api/reviews")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser()
	@Test
	public void postReviewAsUser() throws Exception {
		Review r = new Review();
		r.setId(1l);
		Mockito.when(this.userContext.get()).thenReturn(SecurityTestUtils.USER());
		Mockito.when(this.reviewService.saveReview(any(), any())).thenReturn(r);
		String input = "{\"restaurantId\": 1,\"rate\": 5,\"comment\": \"comment\",\"visitDate\": \"" + LocalDate.now().toString() + "\" }";
		this.mockMvc.perform(post("/api/reviews")
				.content(input)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
		verify(this.reviewService, times(1)).saveReview(any(), any());
	}

	@WithMockUser()
	@Test
	public void postReviewAsUserWithoutRestaurantId() throws Exception {
		String input = "{\"rate\": 5,\"comment\": \"comment\",\"visitDate\": \"" + LocalDate.now().toString() + "\" }";
		this.mockMvc.perform(post("/api/reviews")
				.content(input)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
				.andExpect(MockMvcResultMatchers.jsonPath("$.restaurantId", Is.is("must not be null")));
	}

	@WithMockUser()
	@Test
	public void postReviewAsUserWithoutRate() throws Exception {
		String input = "{\"restaurantId\": 1,\"comment\": \"comment\",\"visitDate\": \"" + LocalDate.now().toString() + "\" }";
		this.mockMvc.perform(post("/api/reviews")
				.content(input)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
				.andExpect(MockMvcResultMatchers.jsonPath("$.rate", Is.is("must not be null")));
	}

	@WithMockUser()
	@Test
	public void postReviewAsUserWithRateLessThan0() throws Exception {
		String input = "{\"restaurantId\": 1,\"rate\": -1,\"comment\": \"comment\",\"visitDate\": \"" + LocalDate.now().toString() + "\" }";
		this.mockMvc.perform(post("/api/reviews")
				.content(input)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
				.andExpect(MockMvcResultMatchers.jsonPath("$.rate", Is.is("must be greater than or equal to 0")));
	}

	@WithMockUser()
	@Test
	public void postReviewAsUserWithRateGreaterThanFive() throws Exception {
		String input = "{\"restaurantId\": 1,\"rate\": 6,\"comment\": \"comment\",\"visitDate\": \"" + LocalDate.now().toString() + "\" }";
		this.mockMvc.perform(post("/api/reviews")
				.content(input)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
				.andExpect(MockMvcResultMatchers.jsonPath("$.rate", Is.is("must be less than or equal to 5")));
	}

	@WithMockUser()
	@Test
	public void postReviewAsUserWithoutComment() throws Exception {
		Review r = new Review();
		r.setId(1l);
		Mockito.when(this.userContext.get()).thenReturn(SecurityTestUtils.USER());
		Mockito.when(this.reviewService.saveReview(any(), any())).thenReturn(r);
		String input = "{\"restaurantId\": 1,\"rate\": 5,\"visitDate\": \"" + LocalDate.now().toString() + "\" }";
		this.mockMvc.perform(post("/api/reviews")
				.content(input)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@WithMockUser()
	@Test
	public void postReviewAsUserWithoutDate() throws Exception {
		String input = "{\"restaurantId\": 1,\"rate\": 5,\"comment\": \"comment\"}";
		this.mockMvc.perform(post("/api/reviews")
				.content(input)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
				.andExpect(MockMvcResultMatchers.jsonPath("$.visitDate", Is.is("must not be null")));
	}

	@WithMockUser()
	@Test
	public void postReviewAsUserWithFutureDate() throws Exception {
		String input = "{\"restaurantId\": 1,\"rate\": 5,\"comment\": \"comment\",\"visitDate\": \"" + LocalDate.now().plusDays(1).toString() + "\" }";
		this.mockMvc.perform(post("/api/reviews")
				.content(input)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
				.andExpect(MockMvcResultMatchers.jsonPath("$.visitDate", Is.is("must be a date in the past or in the present")));
	}

	@Test
	public void tryReplyWithoutLogin() throws Exception {
		this.mockMvc.perform(post("/api/reviews/1/replies")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}

	@WithMockUser(value = "admin", roles = { "ADMIN" })
	@Test
	public void tryReplyAsAdmin() throws Exception {
		this.mockMvc.perform(post("/api/reviews/1/replies")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser()
	@Test
	public void tryReplyAsUser() throws Exception {
		this.mockMvc.perform(post("/api/reviews/1/replies")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(value = "owner", roles = { "OWNER" })
	@Test
	public void tryReplyAsOwnerWithoutReply() throws Exception {
		this.mockMvc.perform(post("/api/reviews/1/replies")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@WithMockUser(value = "owner", roles = { "OWNER" })
	@Test
	public void tryReplyAsNotTheOwner() throws Exception {
		Review review = new Review();
		Mockito.when(this.reviewService.hasReply(any())).thenReturn(review);
		Mockito.when(this.userContext.get()).thenReturn(SecurityTestUtils.OWNER());
		Mockito.when(this.reviewService.ownerReply(any(), any(), any())).thenReturn(null);
		this.mockMvc.perform(post("/api/reviews/1/replies")
				.content("{\"reply\":\"reply\"}")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is("you must be the owner of the restaurant of the review")));
	}

	@WithMockUser(value = "owner", roles = { "OWNER" })
	@Test
	public void tryReplyAsOwner() throws Exception {
		Review r = new Review();
		r.setId(1l);
		Mockito.when(this.reviewService.hasReply(any())).thenReturn(r);
		Mockito.when(this.userContext.get()).thenReturn(SecurityTestUtils.OWNER());
		Mockito.when(this.reviewService.ownerReply(any(), any(), any())).thenReturn(r);
		this.mockMvc.perform(post("/api/reviews/1/replies")
				.content("{\"reply\":\"reply\"}")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@WithMockUser(value = "owner", roles = { "OWNER" })
	@Test
	public void tryReplyAlreadyRepliedReview() throws Exception {
		Mockito.when(this.reviewService.hasReply(any())).thenReturn(null);
		Mockito.when(this.userContext.get()).thenReturn(SecurityTestUtils.OWNER());
		this.mockMvc.perform(post("/api/reviews/1/replies")
				.content("{\"reply\":\"reply\"}")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.reply", Is.is("review already replied")));
	}

}