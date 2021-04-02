package dev.restaurantreview.modules.user.service;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.restaurantreview.config.accesspolicy.RoleEnum;
import dev.restaurantreview.modules.restaurant.service.RestaurantService;
import dev.restaurantreview.modules.review.service.ReviewService;
import dev.restaurantreview.modules.user.model.Role;
import dev.restaurantreview.modules.user.model.User;
import dev.restaurantreview.modules.user.model.UserEditInput;
import dev.restaurantreview.modules.user.model.UserInput;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final EntityManager em;
	private final ReviewService reviewService;
	private final RestaurantService restaurantService;

	@Transactional
	public User createRegularUser(UserInput userInput, RoleEnum role) {
		User user = this.userRepository.findByUsername(userInput.getEmail());
		if (user != null) {
			return null;
		}
		user = new User();
		user.setUsername(userInput.getEmail());
		user.setDisplayName(userInput.getName());
		user.setPassword(this.passwordEncoder.encode(userInput.getPassword()));
		user.setEnabled(true);
		Set<Role> roles = new HashSet<>();
		roles.add(this.em.getReference(Role.class, role.getId()));
		user.setRoles(roles);
		return this.userRepository.save(user);
	}

	public User getUserByEmail(String email) {
		return this.userRepository.findByUsername(email);
	}

	public User getUserById(Long id) {
		return this.userRepository.findById(id).get();
	}

	@Transactional
	public User updateUser(Long id, UserEditInput userEditInput) {
		User user = this.userRepository.getOne(id);
		if (!StringUtils.equals(userEditInput.getEmail(), user.getUsername())) {
			User user2 = this.userRepository.findByUsername(userEditInput.getEmail());
			if (user2 != null) {
				return null;
			}
		}
		user.setDisplayName(userEditInput.getName());
		user.setUsername(userEditInput.getEmail());
		return this.userRepository.save(user);
	}

	@Transactional
	public void deleteUser(Long id) {
		User user = this.userRepository.getOne(id);
		if (user.getRoles().stream().anyMatch(t -> t.getId().equals(RoleEnum.ROLE_OWNER.getId()))) {
			this.restaurantService.deleteRestaurantByOwner(user);
		}
		if (user.getRoles().stream().anyMatch(t -> t.getId().equals(RoleEnum.ROLE_USER.getId()))) {
			this.reviewService.deleteReviewByAuthor(user);
		}
		this.userRepository.delete(user);
	}

}
