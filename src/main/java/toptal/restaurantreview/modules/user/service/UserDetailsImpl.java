package dev.restaurantreview.modules.user.service;

import java.util.Collection;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import dev.restaurantreview.modules.user.model.User;

@RequiredArgsConstructor
@Getter
public class UserDetailsImpl implements UserDetails {
	private final transient User user;
	private static final long serialVersionUID = 8893477516759815872L;

	@Override public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	@Override public String getPassword() {
		return this.user.getPassword();
	}

	@Override public String getUsername() {
		return this.user.getUsername();
	}

	@Override public boolean isAccountNonExpired() {
		return true;
	}

	@Override public boolean isAccountNonLocked() {
		return true;
	}

	@Override public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override public boolean isEnabled() {
		return this.user.getEnabled();
	}
}
