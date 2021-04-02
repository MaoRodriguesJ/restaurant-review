package dev.restaurantreview.modules.restaurant.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import dev.restaurantreview.config.database.DatabaseSchema;

@Data
@Entity
@Table(name = "tb_type_of_food", schema = DatabaseSchema.DEV)
public class TypeOfFood {

	private static final String SEQUENCE_NAME = "dev.typeoffood_seqtypeoffood";

	@Id
	@Column(name = "type_of_food_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TypeOfFood.SEQUENCE_NAME)
	@GenericGenerator(
			name = TypeOfFood.SEQUENCE_NAME,
			strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
			parameters = {
					@Parameter(name = "sequence_name", value = TypeOfFood.SEQUENCE_NAME),
					@Parameter(name = "initial_value", value = "1"),
					@Parameter(name = "increment_size", value = "1"),
					@Parameter(name = "optimizer", value = "pooled-lo")
			}
	)
	private Long id;

	@Column(name = "type_of_food_description")
	private String description;
}
