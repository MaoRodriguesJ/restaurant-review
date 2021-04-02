package dev.restaurantreview.config.accesspolicy;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccessPolicy {

	public static final String USER = "USER";
	public static final String ADMIN = "ADMIN";
	public static final String OWNER = "OWNER";

	@Retention(RetentionPolicy.RUNTIME)
	public @interface HasAnyRole {
		String[] value();
	}

}
