package dev.restaurantreview.config.domainObject;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = DomainObjectDeserializer.class)
public interface DomainObject<T> {

	T getId();

	String getDescription();

}
