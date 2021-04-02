package dev.restaurantreview.modules.review.model;

import java.time.LocalDate;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

import dev.restaurantreview.config.database.DatabaseSchema;
import dev.restaurantreview.modules.restaurant.model.Restaurant;
import dev.restaurantreview.modules.user.model.User;

@Data
@Entity
@Table(name = "tb_review", schema = DatabaseSchema.DEV)
public class Review {

	private static final String SEQUENCE_NAME = "dev.review_seqreview";

	@Id
	@Column(name = "review_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = Review.SEQUENCE_NAME)
	@GenericGenerator(
			name = Review.SEQUENCE_NAME,
			strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
			parameters = {
					@Parameter(name = "sequence_name", value = Review.SEQUENCE_NAME),
					@Parameter(name = "initial_value", value = "1"),
					@Parameter(name = "increment_size", value = "1"),
					@Parameter(name = "optimizer", value = "pooled-lo")
			}
	)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant")
	@JsonIgnore
	private Restaurant restaurant;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "review_author")
	private User author;

	@Column(name = "rate")
	private Integer rate;

	@Column(name = "comment")
	private String comment;

	@Column(name = "visit_date")
	private LocalDate visitDate;

	@Column(name = "reply")
	private String reply;

	@Column(name = "reply_date")
	private LocalDate replyDate;
}
