package org.isag_ghana.alpha.model.pages;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.isag_ghana.alpha.model.Event;
import org.isag_ghana.alpha.model.NewsBlog;
import org.isag_ghana.alpha.model.Photo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IndexPage {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@OneToMany
	private List<Photo> bannerAdverts;

	private String title;

	@OneToMany
	private List<Event> upcomingEvents;

	@OneToOne
	private Event justEndedEvent;

	@OneToMany
	private List<Photo> justEndedEventPhotos;

	@OneToOne
	private NewsBlog IsagNews;

	@OneToMany
	private List<Photo> squareAdverts;

}
