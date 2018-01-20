package org.isag_ghana.alpha.rss;

import java.util.List;

public interface RepositoryItems {

	void save(ModelFeedData data);

	List<ModelFeedItem> findAll();
}
