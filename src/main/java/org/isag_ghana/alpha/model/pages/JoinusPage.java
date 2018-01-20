package org.isag_ghana.alpha.model.pages;

import java.util.Calendar;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.isag_ghana.alpha.model.Photo;
import org.isag_ghana.alpha.model.MessageAndRecipient;
import org.isag_ghana.alpha.model.User;
import org.isag_ghana.alpha.model.Video;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JoinusPage {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String content;

	private String subject;

	private Boolean isRead;

	private Calendar date = Calendar.getInstance();

	@OneToMany
	private Set<Photo> photos;

	@OneToMany
	private Set<Video> videos;

	@OneToMany
	private Set<MessageAndRecipient> replies;

	@OneToOne
	@JoinColumn(name = "sender_id", referencedColumnName = "id")
	private User sender;

	@OneToOne
	@JoinColumn(name = "recipient_id", referencedColumnName = "id")
	private User recipient;

}
