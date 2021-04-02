package dev.restaurantreview.modules.user.service;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dev.restaurantreview.modules.user.model.User;

@RequiredArgsConstructor
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

	private static final int EXPIRE_RESET_TOKEN = 2;

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional(readOnly = true)
	@Override public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = this.userRepository.findByUsername(email);
		if (user == null) {
			throw new UsernameNotFoundException("User with email not found: " + email);
		}

		return new UserDetailsImpl(user);
	}

	public boolean isValidResetToken(String token, @NotNull User user) {
		LocalDateTime expirationDate = user.getExpirationDate();
		if (token == null || !token.equals(user.getResetToken()) || expirationDate == null || expirationDate.isBefore(LocalDateTime.now())) {
			this.clearResetToken(user);
			return false;
		}
		return true;
	}

	public boolean checkPassword(String password, User user) {
		return this.passwordEncoder.matches(password, user.getPassword());
	}

	@Transactional
	public void changePassword(String password, User user) {
		user.setPassword(this.passwordEncoder.encode(password));
		this.userRepository.save(user);
	}

	@Transactional
	public void createResetToken(User user, String token) {
		user.setResetToken(token);
		user.setExpirationDate(LocalDateTime.now().plusHours(EXPIRE_RESET_TOKEN));
		this.userRepository.save(user);
	}

	@Transactional
	public void clearResetToken(User user) {
		user.setResetToken(null);
		user.setExpirationDate(null);
		this.userRepository.save(user);
	}

}
