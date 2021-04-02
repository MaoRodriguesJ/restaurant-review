package dev.restaurantreview.modules.user.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

import dev.restaurantreview.config.database.DatabaseSchema;

@Data
@Entity
@Table(name = "tb_role", schema = DatabaseSchema.DEV)
public class Role implements Serializable {

	private static final long serialVersionUID = 7579721536281711998L;

	@Id
	@Column(name = "role_id")
	private Long id;

	@Column(name = "role_name")
	private String name;

}
