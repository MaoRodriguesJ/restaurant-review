package dev.restaurantreview.modules.review.service;

import java.time.LocalDate;

import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import dev.restaurantreview.modules.restaurant.model.Restaurant;
import dev.restaurantreview.modules.restaurant.service.RestaurantService;
import dev.restaurantreview.modules.review.model.Review;
import dev.restaurantreview.modules.review.model.ReviewEditInput;
import dev.restaurantreview.modules.review.model.ReviewInput;
import dev.restaurantreview.modules.user.model.User;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final RestaurantService restaurantService;
	private final ReviewRepository reviewRepository;

	@Transactional
	public Review saveReview(ReviewInput reviewInput, User user) {
		Restaurant restaurant = this.restaurantService.getRestaurant(reviewInput.getRestaurantId());
		Review review = new Review();
		review.setAuthor(user);
		review.setRestaurant(restaurant);
		review.setRate(reviewInput.getRate());
		review.setComment(reviewInput.getComment());
		review.setVisitDate(reviewInput.getVisitDate());
		return this.reviewRepository.save(review);
	}

	public Review getReviewById(Long id) {
		return this.reviewRepository.findById(id).get();
	}

	@Transactional
	public Review updateReview(Long id, ReviewEditInput reviewEditInput) {
		Review review = this.reviewRepository.getOne(id);
		review.setRate(reviewEditInput.getRate());
		review.setComment(reviewEditInput.getComment());
		review.setVisitDate(reviewEditInput.getVisitDate());
		return this.reviewRepository.save(review);
	}

	@Transactional
	public Review updateReply(Long id, String reply) {
		Review review = this.reviewRepository.getOne(id);
		review.setReply(reply);
		return this.reviewRepository.save(review);
	}

	public Review hasReply(Long reviewId) {
		Review review = this.reviewRepository.getOne(reviewId);
		if (review.getReply() == null) {
			return review;
		}
		return null;
	}

	@Transactional
	public Review ownerReply(Review review, User owner, String reply) {
		if (this.restaurantService.isRestaurantOwner(review.getRestaurant().getId(), owner.getId())) {
			review.setReply(reply);
			review.setReplyDate(LocalDate.now());
			return this.reviewRepository.save(review);
		}
		return null;
	}

	@Transactional
	public void deleteReview(Long id) {
		this.reviewRepository.deleteById(id);
	}

	@Transactional
	public void deleteReviewByAuthor(User user) {
		this.reviewRepository.deleteByAuthor(user);
	}

	@Transactional
	public void deleteReply(Long id) {
		Review review = this.reviewRepository.getOne(id);
		review.setReply(null);
		review.setReplyDate(null);
		this.reviewRepository.save(review);
	}
}
