package org.isag_ghana.alpha.model;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Question {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String question;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	private Set<QuestionType> questionTypes;

	private LocalDateTime date = LocalDateTime.now();

	@ManyToOne
	private User owner;

}
