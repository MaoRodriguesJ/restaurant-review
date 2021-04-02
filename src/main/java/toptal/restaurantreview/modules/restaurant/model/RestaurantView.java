package dev.restaurantreview.modules.restaurant.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

import org.hibernate.annotations.Immutable;

import net.minidev.json.annotate.JsonIgnore;
import dev.restaurantreview.config.database.DatabaseSchema;

@Data
@Entity
@Immutable
@Table(name = "vw_restaurant_list", schema = DatabaseSchema.DEV)
public class RestaurantView {

	@Id
	@Column(name = "restaurant_seq")
	private Long id;

	@JsonIgnore
	@Column(name = "restaurant_owner")
	private Long restaurantOwnerId;

	@Column(name = "restaurant_name")
	private String name;

	@Column(name = "restaurant_description")
	private String description;

	@Column(name = "restaurant_address")
	private String address;

	@Column(name = "type_of_food_description")
	private String typeOfFood;

	@Column(name = "average_rate")
	private Double avgRate;

	@Column(name = "pending_replies")
	private Integer pendingReplies;

	@Column(name = "restaurant_latitude")
	private Double latitude;

	@Column(name = "restaurant_longitude")
	private Double longitude;
}
