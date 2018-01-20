package org.isag_ghana.alpha.rss;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

@Service
public class ItemsExtractorImpl implements ItemsExtractor {

	@Override
	public List<ModelFeedItem> extractItems(String feedUrl) {
		try (CloseableHttpClient client = HttpClients.createMinimal()) {
			List<ModelFeedItem> result = Lists.newLinkedList();
			HttpUriRequest method = new HttpGet(feedUrl);
			try (CloseableHttpResponse response = client.execute(method);
					InputStream stream = response.getEntity().getContent()) {
				SyndFeedInput input = new SyndFeedInput();
				SyndFeed feed = input.build(new XmlReader(stream));
				List<SyndEntry> entries = feed.getEntries();
				for (SyndEntry feedEntry : entries) {
					result.add(new ModelFeedItem(feedEntry.getTitle(), feedEntry.getDescription().getValue(),
							new Date(feedEntry.getPublishedDate().getTime()),
							feedEntry.getLink()));
				}
				return result;
			} catch (com.rometools.rome.io.FeedException e) {
				e.printStackTrace();
			}
		} catch (IllegalArgumentException | IOException e) {
			throw new RuntimeException("Error getting feed from " + feedUrl, e);
		}
		return null;
	}

}
