package dev.restaurantreview.modules.review;

import static dev.restaurantreview.config.accesspolicy.AccessPolicy.ADMIN;
import static dev.restaurantreview.config.accesspolicy.AccessPolicy.OWNER;
import static dev.restaurantreview.config.accesspolicy.AccessPolicy.USER;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;

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
import dev.restaurantreview.modules.review.model.Review;
import dev.restaurantreview.modules.review.model.ReviewEditInput;
import dev.restaurantreview.modules.review.model.ReviewInput;
import dev.restaurantreview.modules.review.service.ReviewService;
import dev.restaurantreview.modules.user.service.UserContext;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

	private final ReviewService reviewService;
	private final UserContext userContext;

	@PostMapping
	@HasAnyRole({ USER })
	public ResponseEntity storeReview(@Valid @RequestBody ReviewInput reviewInput) {
		return new ResponseEntity<>(this.reviewService.saveReview(reviewInput, this.userContext.get()).getId(), HttpStatus.OK);
	}

	@PostMapping({ "/{id}/replies" })
	@HasAnyRole({ OWNER })
	public ResponseEntity ownerReply(@RequestBody String reply, @PathVariable Long id) {
		Review review = this.reviewService.hasReply(id);
		if (review == null) {
			Map<String, String> errors = new HashMap<>();
			errors.put("reply", "review already replied");
			return ResponseEntity.badRequest().body(errors);
		}

		if (this.reviewService.ownerReply(review, this.userContext.get(), reply) == null) {
			Map<String, String> errors = new HashMap<>();
			errors.put("id", "you must be the owner of the restaurant of the review");
			return ResponseEntity.badRequest().body(errors);
		}
		return new ResponseEntity<>(review.getId(), HttpStatus.OK);
	}

	@GetMapping({ "/{id}" })
	@HasAnyRole({ ADMIN })
	public ResponseEntity show(@PathVariable Long id) {
		return new ResponseEntity<>(this.reviewService.getReviewById(id), HttpStatus.OK);
	}

	@PutMapping({ "/{id}" })
	@HasAnyRole({ ADMIN })
	public ResponseEntity update(@PathVariable Long id, @Valid @RequestBody ReviewEditInput reviewEditInput) {
		return new ResponseEntity<>(this.reviewService.updateReview(id, reviewEditInput).getId(), HttpStatus.OK);
	}

	@PutMapping({ "/{id}/replies" })
	@HasAnyRole({ ADMIN })
	public ResponseEntity update(@PathVariable Long id, @RequestBody String reply) {
		return new ResponseEntity<>(this.reviewService.updateReply(id, reply).getId(), HttpStatus.OK);
	}

	@DeleteMapping({ "/{id}" })
	@HasAnyRole({ ADMIN })
	public ResponseEntity delete(@PathVariable Long id) {
		this.reviewService.deleteReview(id);
		return new ResponseEntity<>(true, HttpStatus.OK);
	}

	@DeleteMapping({ "/{id}/replies" })
	@HasAnyRole({ ADMIN })
	public ResponseEntity deleteReply(@PathVariable Long id) {
		this.reviewService.deleteReply(id);
		return new ResponseEntity<>(true, HttpStatus.OK);
	}
}
