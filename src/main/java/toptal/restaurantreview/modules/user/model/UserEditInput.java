package dev.restaurantreview.modules.user.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEditInput {

	@NotBlank
	private String name;

	@Email
	@NotBlank
	private String email;
	
}
