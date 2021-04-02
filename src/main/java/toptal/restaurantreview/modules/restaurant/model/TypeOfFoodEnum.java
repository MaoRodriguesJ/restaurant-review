package dev.restaurantreview.modules.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;

import dev.restaurantreview.config.domainObject.DomainObject;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TypeOfFoodEnum implements DomainObject<Long> {

	FASTFOOD(1L, "Fast food"),
	THAI(2L, "Thai"),
	CHINESE(3L, "Chinese"),
	PIZZA(4L, "Pizza"),
	HAMBURGUER(5L, "Hamburger"),
	JAPANESE(6L, "Japanese"),
	BRAZILIAN(7L, "Brazilian"),
	ITALIAN(8L, "Italian"),
	SEAFOOD(9L, "Seafood"),
	DESERT(10L, "Desert");

	private Long id;
	private String description;
}
