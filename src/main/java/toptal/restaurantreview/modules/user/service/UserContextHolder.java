package dev.restaurantreview.modules.user.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import dev.restaurantreview.modules.user.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserContextHolder {

	public static User get() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl
				? ((UserDetailsImpl) authentication.getPrincipal()).getUser()
				: null;
	}
}
