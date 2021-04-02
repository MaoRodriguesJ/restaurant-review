package dev.restaurantreview.modules.user.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import dev.restaurantreview.modules.validation.Password;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInput {

	@NotBlank
	private String name;

	@Email
	@NotBlank
	private String email;

	@NotBlank
	@Password
	private String password;

}
