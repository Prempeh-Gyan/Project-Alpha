package org.isag_ghana.alpha.model;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne
	private Message parentMessage;

	private String subject;

	private String message;

	private String sendersName;

	private String sendersEmail;

	private String sendersPhoneNumber;

	private LocalDateTime date = LocalDateTime.now();

	@OneToMany
	private Set<Photo> photos;

	@OneToMany
	private Set<Video> videos;

	@OneToMany
	private Set<Document> documents;

	@ManyToOne
	private User sender;

}
