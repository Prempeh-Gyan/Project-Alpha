package org.isag_ghana.alpha.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class NewsBlog {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String title;

	private String content;

	private LocalDateTime date = LocalDateTime.now();

	private Boolean hasPhoto = false;
	
	private Boolean hasCoverPhoto = false;

	private Boolean hasVideo = false;
	
	private Boolean hasPromotionalVideo = false;

	@ManyToOne
	private User author;

}
