package org.isag_ghana.alpha.rss;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Repository
public class RepositoryItemsImpl implements RepositoryItems {
	private final Map<String, List<ModelFeedItem>> db;

	public RepositoryItemsImpl() {
		db = Maps.newHashMap();
	}

	@Override
	public void save(ModelFeedData data) {
		db.put(hasKeyForUrl(data.getFeedUrl()), data.getItems());
	}

	@Override
	public List<ModelFeedItem> findAll() {
		List<ModelFeedItem> result = Lists.newLinkedList();
		for (List<ModelFeedItem> itemsForUrl : db.values()) {
			result.addAll(itemsForUrl);
		}
		return result;
	}

	private String hasKeyForUrl(String feedUrl) {
		return feedUrl;
	}
}
