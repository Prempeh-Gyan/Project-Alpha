package org.isag_ghana.alpha.rss;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.feed.AbstractRssFeedView;

import com.rometools.rome.feed.rss.Channel;
import com.rometools.rome.feed.rss.Content;
import com.rometools.rome.feed.rss.Item;

public class ContentRssView extends AbstractRssFeedView {

	@Override
	protected void buildFeedMetadata(Map<String, Object> model, Channel channel, HttpServletRequest request) {
		channel.setTitle("Concretepage.com");
		channel.setLink("http://www.concretepage.com");
		channel.setDescription("Concretepage.com is a java tutorial.");
	}

	@Override
	protected List<Item> buildFeedItems(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<Item> items = new ArrayList<>();
		Object ob = model.get("feeds");
		if (ob instanceof List) {
			for (int i = 0; i < ((List<?>) ob).size(); i++) {
				Object feedObj = ((List<?>) ob).get(i);
				MyFeed myFeed = (MyFeed) feedObj;
				Item item = new Item();
				item.setTitle(myFeed.getTitle());
				item.setLink(myFeed.getLink());
				item.setPubDate(myFeed.getPubDate());
				Content content = new Content();
				content.setValue(myFeed.getDescription());
				item.setContent(content);
				items.add(item);
			}
		}
		return items;
	}
}
