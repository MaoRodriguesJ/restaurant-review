package dev.restaurantreview.modules.user.service;

import org.springframework.stereotype.Service;

import dev.restaurantreview.modules.user.model.User;

@Service
public class UserContext {

	public User get() {
		return UserContextHolder.get();
	}
}
