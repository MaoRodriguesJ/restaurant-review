package dev.restaurantreview.modules.user.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dev.restaurantreview.config.database.DatabaseSchema;

@Data
@Entity
@Table(name = "tb_user", schema = DatabaseSchema.DEV)
public class User implements Serializable {

	private static final String SEQUENCE_NAME = "dev.user_sequser";
	private static final long serialVersionUID = -7566117768508443374L;

	@Id
	@Column(name = "user_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = User.SEQUENCE_NAME)
	@GenericGenerator(
			name = User.SEQUENCE_NAME,
			strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
			parameters = {
					@Parameter(name = "sequence_name", value = User.SEQUENCE_NAME),
					@Parameter(name = "initial_value", value = "1"),
					@Parameter(name = "increment_size", value = "1"),
					@Parameter(name = "optimizer", value = "pooled-lo")
			}
	)
	private Long id;

	@Column(name = "username")
	private String username;

	@Column(name = "display_name")
	private String displayName;

	@Column(name = "password_hash")
	@JsonIgnore
	private String password;

	@Column(name = "reset_token")
	@JsonIgnore
	private String resetToken;

	@Column(name = "token_expiration_date")
	@JsonIgnore
	private LocalDateTime expirationDate;

	@Column(name = "user_enabled")
	@JsonIgnore
	private Boolean enabled;
	
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
	@JoinTable(
			name = "rl_user_role",
			schema = DatabaseSchema.DEV,
			joinColumns = @JoinColumn(name = "user_seq"),
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;

}
