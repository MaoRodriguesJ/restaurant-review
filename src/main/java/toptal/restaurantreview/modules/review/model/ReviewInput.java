package dev.restaurantreview.modules.review.model;

import java.time.LocalDate;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewInput {

	@NotNull
	private Long restaurantId;

	@NotNull
	@PositiveOrZero
	@Max(5)
	private Integer rate;

	private String comment;

	@NotNull
	@PastOrPresent
	private LocalDate visitDate;
}
