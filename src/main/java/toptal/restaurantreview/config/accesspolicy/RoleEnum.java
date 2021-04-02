package dev.restaurantreview.config.accesspolicy;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleEnum {

	ROLE_ADMIN(1L, "ROLE_ADMIN"),
	ROLE_USER(2L, "ROLE_USER"),
	ROLE_OWNER(3L, "ROLE_OWNER");

	private Long id;
	private String description;

}

