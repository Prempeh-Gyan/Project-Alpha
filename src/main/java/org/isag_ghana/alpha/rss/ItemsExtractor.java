package org.isag_ghana.alpha.rss;

import java.util.List;

public interface ItemsExtractor {
	List<ModelFeedItem> extractItems(String feedUrl);
}
