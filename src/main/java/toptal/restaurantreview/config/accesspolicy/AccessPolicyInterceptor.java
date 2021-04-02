package dev.restaurantreview.config.accesspolicy;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.val;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

public class AccessPolicyInterceptor implements HandlerInterceptor {

	private Set<String> getHasRoleValues(Method annotatedMethod) {
		Set<String> values = new HashSet<>();
		final AccessPolicy.HasAnyRole[] hasRoleMethodAnnotations = annotatedMethod.getAnnotationsByType(AccessPolicy.HasAnyRole.class);
		final AccessPolicy.HasAnyRole[] hasRoleClassAnnotations = annotatedMethod.getDeclaringClass().getAnnotationsByType(AccessPolicy.HasAnyRole.class);
		if (hasRoleMethodAnnotations != null && hasRoleMethodAnnotations.length > 0) {
			for (val annotation : hasRoleMethodAnnotations) {
				values.addAll(Arrays.asList(annotation.value()));
			}
		} else if (hasRoleClassAnnotations != null && hasRoleClassAnnotations.length > 0) {
			for (val annotation : hasRoleClassAnnotations) {
				values.addAll(Arrays.asList(annotation.value()));
			}
		}

		return values;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
		if (handler != null && handler instanceof HandlerMethod) {
			final HandlerMethod handlerMethod = (HandlerMethod) handler;
			final Set<String> values = this.getHasRoleValues(handlerMethod.getMethod());
			if (!values.isEmpty()) {
				if (request.getUserPrincipal() == null) {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
					return false;
				}
				if (values.stream().anyMatch(request::isUserInRole)) {
					return true;
				}
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
				return false;
			}
		}

		return true;
	}

}
