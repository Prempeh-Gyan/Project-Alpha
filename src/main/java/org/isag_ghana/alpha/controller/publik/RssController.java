package org.isag_ghana.alpha.controller.publik;

import java.util.Iterator;
import java.util.List;

import org.isag_ghana.alpha.rss.ModelFeedItem;
import org.isag_ghana.alpha.rss.ServiceFeedReciever;
import org.isag_ghana.alpha.rss.ServiceItemsRetriever;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class RssController {

	private final ServiceFeedReciever feedReciever;
	private final ServiceItemsRetriever itemsRetriever;

	@Autowired
	public RssController(ServiceFeedReciever feedReciever, ServiceItemsRetriever itemsRetriever) {
		this.feedReciever = feedReciever;
		this.itemsRetriever = itemsRetriever;
	}

	@RequestMapping(value = { "/public/rss" }, method = RequestMethod.GET)
	public @ResponseBody List<ModelFeedItem> retrieveFeedItems(@RequestParam("feedURL") String feedURL,
			@RequestParam("newsAgency") String newsAgency) {

		feedReciever.addFeed(feedURL);
		List<ModelFeedItem> feedItems = itemsRetriever.get();

		String category;

		if (newsAgency.equals("CNN")) {
			category = feedURL.substring(feedURL.lastIndexOf("_") + 1, feedURL.indexOf(".rss"));
		} else if (newsAgency.equals("BBC")) {
			category = feedURL.substring(feedURL.indexOf("/news/") + 5, feedURL.indexOf("/rss.xml?edition=uk"));
			;
		} else if (newsAgency.equals("DW")) {
			category = feedURL.substring(feedURL.lastIndexOf("rss") + 7);
		} else if (newsAgency.equals("MyJoy Online")) {
			category = feedURL.substring(feedURL.indexOf("_") + 1, feedURL.indexOf(".xml"));
		} else if (newsAgency.equals("Ghana Web")) {
			category = feedURL.substring(feedURL.indexOf("/feed/") + 6, feedURL.indexOf(".xml"));
		} else {
			category = "";
		}

		for (Iterator<ModelFeedItem> itemsIterator = feedItems.iterator(); itemsIterator.hasNext();) {
			ModelFeedItem item = itemsIterator.next();
			if (!item.getLink().contains(newsAgency.toLowerCase()) || !item.getLink().contains(category)
					|| item.getDescription() == null || item.getDescription().equals(null) || item.getDate() == null
					|| item.getDate().equals(null)) {
				itemsIterator.remove();
			}
		}

		feedItems.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()));

		log.info("newsAgency = {}", newsAgency);
		log.info("Category = {}", category);
		log.info("feedURL = {}", feedURL);
		log.info("Size = {}", feedItems.size());

		return feedItems;
	}
}
