package dev.restaurantreview.config.domainObject;

import java.io.IOException;
import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.TextNode;

@NoArgsConstructor
@AllArgsConstructor
public class DomainObjectDeserializer<T> extends JsonDeserializer<DomainObject<T>> implements ContextualDeserializer {

	private JavaType type;

	@Override public JsonDeserializer<?> createContextual(DeserializationContext deserializationContext, BeanProperty beanProperty) {
		return new DomainObjectDeserializer<>(deserializationContext.getContextualType());
	}

	@Override public DomainObject<T> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		TreeNode idNode = p.getCodec().readTree(p).get("id");

		return idNode.asToken().isNumeric() ?
				this.getEnum(((IntNode) idNode).asLong(), (Class<DomainObject<T>>) this.type.getRawClass()) :
				this.getEnum(((TextNode) idNode).asText(), (Class<DomainObject<T>>) this.type.getRawClass());
	}

	private DomainObject<T> getEnum(Object id, Class<DomainObject<T>> clazz) {
		return Arrays.stream(clazz.getEnumConstants()).
				filter(e -> {
					try {
						return clazz.getMethod("getId").invoke(e).equals(id);
					} catch (Exception ex) {
						System.out.println("Deserializer domain object enum error");
					}

					return false;
				})
				.findFirst()
				.orElse(null);
	}

}
