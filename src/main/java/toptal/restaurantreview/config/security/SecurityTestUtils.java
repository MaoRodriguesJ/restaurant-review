package dev.restaurantreview.config.security;

import java.util.HashSet;
import java.util.Set;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import dev.restaurantreview.config.accesspolicy.RoleEnum;
import dev.restaurantreview.modules.user.model.Role;
import dev.restaurantreview.modules.user.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityTestUtils {

	public static User USER() {
		User user = new User();
		user.setId(1l);
		user.setUsername("user");
		user.setPassword("password1234");
		user.setEnabled(true);
		Set<Role> roles = new HashSet<>();
		Role role = new Role();
		role.setId(RoleEnum.ROLE_USER.getId());
		role.setName(RoleEnum.ROLE_USER.getDescription());
		roles.add(role);
		user.setRoles(roles);
		return user;
	}

	public static User OWNER() {
		User user = new User();
		user.setId(1l);
		user.setUsername("owner");
		user.setPassword("password1234");
		user.setEnabled(true);
		Set<Role> roles = new HashSet<>();
		Role role = new Role();
		role.setId(RoleEnum.ROLE_OWNER.getId());
		role.setName(RoleEnum.ROLE_OWNER.getDescription());
		roles.add(role);
		user.setRoles(roles);
		return user;
	}

	public static User ADMIN() {
		User user = new User();
		user.setId(1l);
		user.setUsername("admin");
		user.setPassword("password1234");
		user.setEnabled(true);
		Set<Role> roles = new HashSet<>();
		Role role = new Role();
		role.setId(RoleEnum.ROLE_ADMIN.getId());
		role.setName(RoleEnum.ROLE_ADMIN.getDescription());
		roles.add(role);
		user.setRoles(roles);
		return user;
	}
}
