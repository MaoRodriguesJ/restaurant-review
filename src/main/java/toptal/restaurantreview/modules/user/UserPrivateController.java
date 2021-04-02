package dev.restaurantreview.modules.user;

import static dev.restaurantreview.config.accesspolicy.AccessPolicy.ADMIN;
import static dev.restaurantreview.config.accesspolicy.AccessPolicy.OWNER;
import static dev.restaurantreview.config.accesspolicy.AccessPolicy.USER;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dev.restaurantreview.config.accesspolicy.AccessPolicy.HasAnyRole;
import dev.restaurantreview.config.accesspolicy.RoleEnum;
import dev.restaurantreview.modules.user.model.PasswordInput;
import dev.restaurantreview.modules.user.model.User;
import dev.restaurantreview.modules.user.model.UserEditInput;
import dev.restaurantreview.modules.user.service.UserContext;
import dev.restaurantreview.modules.user.service.UserDetailsServiceImpl;
import dev.restaurantreview.modules.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserPrivateController {

	private final UserService userService;
	private final UserDetailsServiceImpl userDetailsService;
	private final UserContext userContext;

	@GetMapping({ "/{id}" })
	@HasAnyRole({ ADMIN })
	public ResponseEntity show(@PathVariable Long id) {
		return new ResponseEntity<>(this.userService.getUserById(id), HttpStatus.OK);
	}

	@GetMapping({ "/search/{email}" })
	@HasAnyRole({ ADMIN })
	public ResponseEntity show(@PathVariable String email) {
		return new ResponseEntity<>(this.userService.getUserByEmail(email), HttpStatus.OK);
	}

	@PutMapping({ "/{id}" })
	@HasAnyRole({ ADMIN, USER, OWNER })
	public ResponseEntity update(@PathVariable Long id, @Valid @RequestBody UserEditInput userEditInput) {
		User currentUser = this.userContext.get();
		if (!currentUser.getRoles().stream().anyMatch(t -> t.getId().equals(RoleEnum.ROLE_ADMIN.getId())) &&
				!id.equals(currentUser.getId())) {
			Map<String, String> errors = new HashMap<>();
			errors.put("id", "you can only edit your own user");
			return ResponseEntity.unprocessableEntity().body(errors);
		}
		User user = this.userService.updateUser(id, userEditInput);
		if (user == null) {
			Map<String, String> errors = new HashMap<>();
			errors.put("email", "email already in use");
			return ResponseEntity.unprocessableEntity().body(errors);
		}
		return new ResponseEntity<>(user.getId(), HttpStatus.OK);
	}

	@PutMapping({ "/{id}/password" })
	@HasAnyRole({ ADMIN, USER, OWNER })
	public ResponseEntity updatePassword(@PathVariable Long id, @Valid @RequestBody PasswordInput passwordInput) {
		User currentUser = this.userContext.get();
		if (!id.equals(currentUser.getId())) {
			Map<String, String> errors = new HashMap<>();
			errors.put("id", "you can only change your own password");
			return ResponseEntity.unprocessableEntity().body(errors);
		}
		if (!this.userDetailsService.checkPassword(passwordInput.getOldPassword(), currentUser)) {
			Map<String, String> errors = new HashMap<>();
			errors.put("password", "wrong password");
			return ResponseEntity.unprocessableEntity().body(errors);
		}
		this.userDetailsService.changePassword(passwordInput.getPassword(), currentUser);
		return new ResponseEntity<>(true, HttpStatus.OK);
	}

	@DeleteMapping({ "/{id}" })
	@HasAnyRole({ ADMIN })
	public ResponseEntity delete(@PathVariable Long id) {
		this.userService.deleteUser(id);
		return new ResponseEntity<>(true, HttpStatus.OK);
	}

	@GetMapping("/current")
	@HasAnyRole({ USER, ADMIN, OWNER })
	@ResponseBody
	public User getCurrentUser() {
		return this.userContext.get();
	}

}
