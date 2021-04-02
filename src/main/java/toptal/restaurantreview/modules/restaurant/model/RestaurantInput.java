package dev.restaurantreview.modules.restaurant.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantInput {

	@NotBlank
	private String name;

	@NotBlank
	private String description;

	@NotNull
	private TypeOfFoodEnum typeOfFood;

	@NotBlank
	private String address;

	@NotNull
	private Double latitude;

	@NotNull
	private Double longitude;

}
