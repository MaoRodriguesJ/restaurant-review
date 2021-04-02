package dev.restaurantreview.config.domainObject;

import java.util.Arrays;
import java.util.function.Predicate;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GetEnumByAttribute {

	public static <E extends Enum<E> & DomainObject<T>, T> E getById(T id, Class<E> enumType) {
		return Arrays.stream(enumType.getEnumConstants()).
				filter(getIdPredicate(id))
				.findFirst()
				.orElse(null);
	}

	public static <E extends Enum<E> & DomainObject<T>, T> E getByDescricao(String description, Class<E> enumType) {
		return Arrays.stream(enumType.getEnumConstants()).
				filter(getDescricaoPredicate(description))
				.findFirst()
				.orElse(null);
	}

	private static <E extends Enum<E> & DomainObject<T>, T> Predicate<E> getIdPredicate(T id) {
		return e -> e.getId().equals(id);
	}

	private static <E extends Enum<E> & DomainObject<T>, T> Predicate<E> getDescricaoPredicate(String description) {
		return e -> e.getDescription().equals(description);
	}

}
