package org.isag_ghana.alpha.rss;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceFeedReciever {
	private final ItemsExtractor extractor;
	private final RepositoryItems repository;

	@Autowired
	    public ServiceFeedReciever(ItemsExtractor extractor, RepositoryItems repository) {
		this.extractor = extractor;
		this.repository = repository;
	    }

	public void addFeed(String feedUrl) {
		List<ModelFeedItem> items = extractor.extractItems(feedUrl);
		repository.save(new ModelFeedData(feedUrl, items));
	}
}
