package dev.restaurantreview.modules.user.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import dev.restaurantreview.modules.restaurant.model.RestaurantInput;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnerInput {

	@NotNull
	@Valid
	private UserInput user;

	@NotNull
	@Valid
	private RestaurantInput restaurant;
}
