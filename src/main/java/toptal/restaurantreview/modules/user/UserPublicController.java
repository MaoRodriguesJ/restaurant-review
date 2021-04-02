package dev.restaurantreview.modules.user;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import dev.restaurantreview.config.accesspolicy.RoleEnum;
import dev.restaurantreview.modules.mail.service.MailerSender;
import dev.restaurantreview.modules.restaurant.service.RestaurantService;
import dev.restaurantreview.modules.user.model.OwnerInput;
import dev.restaurantreview.modules.user.model.ResetPasswordInput;
import dev.restaurantreview.modules.user.model.User;
import dev.restaurantreview.modules.user.model.UserInput;
import dev.restaurantreview.modules.user.service.UserDetailsServiceImpl;
import dev.restaurantreview.modules.user.service.UserService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/public/users")
public class UserPublicController {

	private static final String SALT_RESET_TOKEN = "#dev";

	private final UserService userService;
	private final RestaurantService restaurantService;
	private final UserDetailsServiceImpl userDetailsService;
	private final MailerSender mailerSender;

	@PostMapping
	public ResponseEntity storeUser(@Valid @RequestBody UserInput userInput) {
		User user = this.userService.createRegularUser(userInput, RoleEnum.ROLE_USER);
		if (user == null) {
			Map<String, String> errors = new HashMap<>();
			errors.put("email", "email already in use");
			return ResponseEntity.unprocessableEntity().body(errors);
		}
		return new ResponseEntity<>(user.getId(), HttpStatus.OK);
	}

	@PostMapping("/restaurant")
	public ResponseEntity storeOwner(@Valid @RequestBody OwnerInput ownerInput) {
		User user = this.userService.createRegularUser(ownerInput.getUser(), RoleEnum.ROLE_OWNER);
		if (user == null) {
			Map<String, String> errors = new HashMap<>();
			errors.put("email", "email already in use");
			return ResponseEntity.unprocessableEntity().body(errors);
		}
		this.restaurantService.createRestaurant(ownerInput.getRestaurant(), user);
		return new ResponseEntity<>(user.getId(), HttpStatus.OK);
	}

	@PostMapping("/resetToken")
	public ResponseEntity forgotPassword(@RequestBody String email) throws Exception {
		User user = this.userService.getUserByEmail(email);
		if (user == null) {
			Map<String, String> errors = new HashMap<>();
			errors.put("email", "email not found");
			return ResponseEntity.unprocessableEntity().body(errors);
		}

		String token = user.getResetToken();
		if (token == null || user.getExpirationDate().isBefore(LocalDateTime.now())) {
			token = DigestUtils.sha512Hex(UUID.randomUUID().toString() + SALT_RESET_TOKEN);
			this.userDetailsService.createResetToken(user, token);
		}

		this.mailerSender.sendResetPasswordMail(email, token);
		return new ResponseEntity<>(true, HttpStatus.OK);
	}

	@GetMapping("/password/{token}/{email}/valid")
	public ResponseEntity<Boolean> validResetToken(@PathVariable String token, @PathVariable String email) {
		User user = this.userService.getUserByEmail(email);
		if (!this.userDetailsService.isValidResetToken(token, user)) {
			return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(true, HttpStatus.OK);
	}

	@PutMapping("/password/{token}/{email}")
	public ResponseEntity<Boolean> resetPassword(@Valid @RequestBody ResetPasswordInput passwordInput, @PathVariable String token, @PathVariable String email) {
		User user = this.userService.getUserByEmail(email);
		if (!this.userDetailsService.isValidResetToken(token, user)) {
			return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
		}
		this.userDetailsService.changePassword(passwordInput.getPassword(), user);
		this.userDetailsService.clearResetToken(user);
		return new ResponseEntity<>(true, HttpStatus.OK);
	}

}
