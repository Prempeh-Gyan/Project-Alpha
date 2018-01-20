package org.isag_ghana.alpha.rss;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/rss")
public class ControllerRssFeed {
/*	@Autowired
	private MyFeed myFeed;

	@RequestMapping(value = "/feed.*", method = RequestMethod.GET)
	public ModelAndView getContent() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("feeds", myFeed.createFeed());
		return mav;
	}

	private List<SampleContent> contentList = new ArrayList<SampleContent>();

	@RequestMapping(value = "/content.*", method = RequestMethod.GET)
	public ModelAndView getContent() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("content");
		mav.addObject("sampleContentList", contentList);
		return mav;
	}

	@RequestMapping(value = "/content.html", method = RequestMethod.POST)
	public String addContent() {
		contentList.add(SampleContent.generateContent("Alef Arendsen", new Date()));
		return "redirect:content.html";
	}*/

	private final ServiceFeedReciever feedReciever;
	private final ServiceItemsRetriever itemsRetriever;

	@Autowired
    public ControllerRssFeed(ServiceFeedReciever feedReciever, ServiceItemsRetriever itemsRetriever) {
	this.feedReciever = feedReciever;
	this.itemsRetriever = itemsRetriever;
    }

	@RequestMapping(value = "/public/feed", method = RequestMethod.POST)
	public String addFeed(@RequestParam("feedUrl") String feedUrl) {
		feedReciever.addFeed(feedUrl);
		return "redirect:all";
	}

	@RequestMapping(value = "/public/all", method = RequestMethod.GET)
	public ModelAndView showAll() {
		List<ModelFeedItem> items = itemsRetriever.get();
		ModelAndView modelAndView = new ModelAndView("rssItems");
		modelAndView.addObject("items", items);
		return modelAndView;
	}
}
