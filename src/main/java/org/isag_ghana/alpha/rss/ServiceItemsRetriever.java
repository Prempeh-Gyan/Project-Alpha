package org.isag_ghana.alpha.rss;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceItemsRetriever {
	private RepositoryItems repository;

	@Autowired
	public ServiceItemsRetriever(RepositoryItems repository) {
		this.repository = repository;
	}

	public List<ModelFeedItem> get() {
		return repository.findAll();
	}
}
