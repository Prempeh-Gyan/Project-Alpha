package org.isag_ghana.alpha.rss;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ModelFeedData {
	private final String feedUrl;
	private final List<ModelFeedItem> items;
}
