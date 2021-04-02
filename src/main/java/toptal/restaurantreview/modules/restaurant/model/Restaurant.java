package dev.restaurantreview.modules.restaurant.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import dev.restaurantreview.config.database.DatabaseSchema;
import dev.restaurantreview.modules.user.model.User;

@Data
@Entity
@Table(name = "tb_restaurant", schema = DatabaseSchema.DEV)
public class Restaurant implements Serializable {

	private static final long serialVersionUID = 2144661298469624437L;
	private static final String SEQUENCE_NAME = "dev.restaurant_seqrestaurant";

	@Id
	@Column(name = "restaurant_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = Restaurant.SEQUENCE_NAME)
	@GenericGenerator(
			name = Restaurant.SEQUENCE_NAME,
			strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
			parameters = {
					@Parameter(name = "sequence_name", value = Restaurant.SEQUENCE_NAME),
					@Parameter(name = "initial_value", value = "1"),
					@Parameter(name = "increment_size", value = "1"),
					@Parameter(name = "optimizer", value = "pooled-lo")
			}
	)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_owner")
	private User owner;

	@Column(name = "restaurant_name")
	private String name;

	@Column(name = "restaurant_description")
	private String description;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "restaurant_type_of_food")
	private TypeOfFood typeOfFood;

	@Column(name = "restaurant_address")
	private String address;

	@Column(name = "restaurant_latitude")
	private Double latitude;

	@Column(name = "restaurant_longitude")
	private Double longitude;
}
