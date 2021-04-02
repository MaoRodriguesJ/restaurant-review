package dev.restaurantreview.modules.user.service;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import dev.restaurantreview.config.accesspolicy.RoleEnum;
import dev.restaurantreview.modules.restaurant.service.RestaurantService;
import dev.restaurantreview.modules.review.service.ReviewService;
import dev.restaurantreview.modules.user.model.Role;
import dev.restaurantreview.modules.user.model.User;
import dev.restaurantreview.modules.user.model.UserInput;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private EntityManager em;

	@Mock
	private RestaurantService restaurantService;

	@Mock
	private ReviewService reviewService;

	private UserService userService;
	private UserInput userInput = new UserInput("name", "email", "password");

	@BeforeEach
	public void setup() {
		this.userService = new UserService(this.userRepository, this.passwordEncoder, this.em, this.reviewService, this.restaurantService);
	}

	@Test
	public void returnNullWhenUserWithEmailAlreadyExists() {
		Mockito.when(this.userRepository.findByUsername(Mockito.anyString())).thenReturn(new User());
		Assertions.assertNull(this.userService.createRegularUser(this.userInput, RoleEnum.ROLE_USER));
	}

	@Test
	public void returnUserWhenNoUserWithEmailExists() {
		Mockito.when(this.userRepository.findByUsername(Mockito.anyString())).thenReturn(null);
		Mockito.when(this.em.getReference(Mockito.any(), Mockito.any())).thenReturn(new Role());
		User returnUser = new User();
		returnUser.setUsername(this.userInput.getEmail());
		Mockito.when(this.userRepository.save(Mockito.any())).thenReturn(returnUser);
		Mockito.when(this.passwordEncoder.encode(Mockito.anyString())).thenReturn("password");
		Assertions.assertEquals(this.userInput.getEmail(), this.userService.createRegularUser(this.userInput, RoleEnum.ROLE_USER).getUsername());
	}
}