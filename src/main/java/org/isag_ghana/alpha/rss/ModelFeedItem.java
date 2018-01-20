package org.isag_ghana.alpha.rss;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.rometools.rome.feed.synd.SyndEntry;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ModelFeedItem {

	@JsonManagedReference
	private String title;

	@JsonManagedReference
	private String description;

	@JsonManagedReference
	private Date date;

	@JsonManagedReference
	private String link;

	public static ModelFeedItem fromSyndEntry(SyndEntry feedEntry) {
		return new ModelFeedItem(feedEntry.getTitle(), feedEntry.getDescription().getValue(),
				new Date(feedEntry.getPublishedDate().getTime()), feedEntry.getLink());
	}
}
