package org.isag_ghana.alpha.controller.publik;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.isag_ghana.alpha.model.Event;
import org.isag_ghana.alpha.model.EventAndPhotos;
import org.isag_ghana.alpha.model.Photo;
import org.isag_ghana.alpha.model.User;
import org.isag_ghana.alpha.service.AdvertService;
import org.isag_ghana.alpha.service.CommitteeAndCommitteeMembersService;
import org.isag_ghana.alpha.service.EventAndPhotosService;
import org.isag_ghana.alpha.service.EventService;
import org.isag_ghana.alpha.service.MessageRecipientService;
import org.isag_ghana.alpha.service.NewsBlogService;
import org.isag_ghana.alpha.service.ProductService;
import org.isag_ghana.alpha.service.UserService;
import org.isag_ghana.alpha.util.CurrentUser;
import org.isag_ghana.alpha.util.EventNotifiction;
import org.isag_ghana.alpha.util.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor(onConstructor = @__({ @Autowired }))
public class IndexController {

	private final @NotNull EventService eventService;
	private final @NotNull AdvertService advertService;
	private final @NotNull NewsBlogService blogService;
	private final @NotNull ProductService productService;
	private final @NotNull EventAndPhotosService eventAndPhotosService;
	private final @NotNull MessageRecipientService messageRecipientService;
	private final @NotNull UserService userService;
	private final @NotNull CommitteeAndCommitteeMembersService committeeAndCommitteeMembersService;

	@RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
	public String indexPage(@CurrentUser User currentUser, Model model) {

		model.addAttribute("currentUser", currentUser);

		log.info("fetching today's events...");

		List<Event> upComingISAGEvents = eventService.findNextISAGEvent();

		List<Event> justEndedEvents = eventService.findJustEndedISAGEvent();

		List<EventAndPhotos> eventAndPhotos = justEndedEvents.isEmpty() ? new ArrayList<>()
				: eventAndPhotosService.findAllByEvent(justEndedEvents.get(0));

		List<Photo> justEndedEventPhotos = new ArrayList<Photo>();

		eventAndPhotos.parallelStream().forEach(ap -> justEndedEventPhotos.add(ap.getPhoto()));

		model.addAttribute("products", productService.findAllAvailableProducts(true));
		model.addAttribute("nextEvent", upComingISAGEvents.isEmpty() ? null : upComingISAGEvents.get(0));
		model.addAttribute("justEndedEvent", justEndedEvents.isEmpty() ? null : justEndedEvents.get(0));
		model.addAttribute("justEndedEventPhotos", justEndedEventPhotos);
		model.addAttribute("upComingEvents", upComingISAGEvents);
		model.addAttribute("blog", !blogService.findByDateOrderByDateDesc().isEmpty()
				? blogService.findByDateOrderByDateDesc().get(0) : null);

		log.info("Home page fired...");
		if (currentUser != null) {
			Notification.getNotifications(model, currentUser, userService, messageRecipientService,
					committeeAndCommitteeMembersService);
		} else {
			model.addAttribute("currentUser", currentUser);
		}

		return "index";
	}

	@RequestMapping(value = { "/index/todaysEvents" }, method = RequestMethod.GET)
	public @ResponseBody EventNotifiction getTodaysEvents() {

		log.info("Ajax fired for today's events...");

		List<Event> todaysEvents = eventService.findNextISAGEvent();

		Event todaysEvent = todaysEvents.isEmpty() ? null : todaysEvents.get(0);

		log.info("Today's event is = {}", todaysEvent);
		log.info("Today's date is = {}", LocalDate.now());

		if (todaysEvent != null) {
			EventNotifiction notification = new EventNotifiction();
			notification.setTitle("<strong>Happening Live in ISAG...</strong>");
			notification.setText("<strong> Event: </strong>" + todaysEvent.getName() + "<br />"
					+ "<strong> Venue:</strong> " + todaysEvent.getVenue() + " <br />"
					+ "<strong> Closing Time:</strong> " + todaysEvent.getEndTime() + " <br /> <br />"
					+ "<strong> Live feed from venue:</strong> " + todaysEvent.getLiveFeed());
			notification.setType("info");
			notification.setAddclass("dark");

			return notification;
		}

		return new EventNotifiction();
	}

	@RequestMapping(value = "/index/getIndexEventCoverPicture/{parentId}", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
	public ResponseEntity<byte[]> getImage(@PathVariable("parentId") Long parentId) throws IOException {
		log.info("Parent id is : {}", parentId);
		Event event = eventService.findById(parentId);
		List<EventAndPhotos> enp = eventAndPhotosService.findByEventAndIsCoverPicture(event);
		byte[] imageContent = enp.get(0).getPhoto().getImage();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		log.info("returning image");
		return new ResponseEntity<byte[]>(imageContent, headers, HttpStatus.OK);
	}
}
