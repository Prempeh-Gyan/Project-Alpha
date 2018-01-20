package org.isag_ghana.alpha.controller.publik;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.isag_ghana.alpha.model.Event;
import org.isag_ghana.alpha.model.EventAndPhotos;
import org.isag_ghana.alpha.model.EventAndVideos;
import org.isag_ghana.alpha.model.Photo;
import org.isag_ghana.alpha.model.User;
import org.isag_ghana.alpha.model.Video;
import org.isag_ghana.alpha.service.CommitteeAndCommitteeMembersService;
import org.isag_ghana.alpha.service.EventAndPhotosService;
import org.isag_ghana.alpha.service.EventAndVideosService;
import org.isag_ghana.alpha.service.EventService;
import org.isag_ghana.alpha.service.MessageRecipientService;
import org.isag_ghana.alpha.service.PositionAndPositionHoldersService;
import org.isag_ghana.alpha.service.PositionService;
import org.isag_ghana.alpha.service.UserService;
import org.isag_ghana.alpha.util.CurrentUser;
import org.isag_ghana.alpha.util.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor(onConstructor = @__({ @Autowired }))
public class EventsController {

	private final @NotNull MessageRecipientService messageRecipientService;
	private final @NotNull PositionService positionService;
	private final @NotNull PositionAndPositionHoldersService positionAndPositionHoldersService;
	private final @NotNull UserService userService;
	private final @NotNull CommitteeAndCommitteeMembersService committeeAndCommitteeMembersService;
	private final @NotNull EventService eventService;
	private final @NotNull EventAndPhotosService eventAndPhotosService;
	private final @NotNull EventAndVideosService eventAndVideosService;

	@RequestMapping(value = { "/public/events-list" }, method = RequestMethod.GET)
	public String eventsList(@CurrentUser User currentUser, Model model, Pageable pageable) {

		log.info("Event-list page fired...");

		if (currentUser != null) {
			Notification.getNotifications(model, currentUser, userService, messageRecipientService,
					committeeAndCommitteeMembersService);
		} else {
			model.addAttribute("currentUser", currentUser);
		}

		model.addAttribute("page", eventService.findAllEventsSorted(pageable));

		return "events-list";
	}

	@RequestMapping(value = { "/public/event" }, method = RequestMethod.GET)
	public String indexPage(RedirectAttributes redirectAttributes) {
		return "redirect:/public/events-list";
	}

	@RequestMapping(value = { "/public/event/{eventId}" }, method = RequestMethod.GET)
	public String newsBlog(@CurrentUser User currentUser, Model model, @PathVariable("eventId") Long eventId) {
		log.info("eventId is : {}", eventId);
		Event event = eventService.findById(eventId);

		model.addAttribute("event", event);

		List<EventAndPhotos> ep = eventAndPhotosService.findAllByEvent(event);

		List<Photo> photos = new ArrayList<>();

		if (!ep.isEmpty()) {
			ep.parallelStream().forEach(enp -> photos.add(enp.getPhoto()));
			model.addAttribute("eventPhotos", photos);
		}

		List<EventAndVideos> ev = eventAndVideosService.findAllByEvent(event);

		List<Video> videos = new ArrayList<>();

		if (!ev.isEmpty()) {
			ev.parallelStream().forEach(env -> videos.add(env.getVideo()));
			model.addAttribute("eventVideos", videos);
		}

		if (currentUser != null) {
			Notification.getNotifications(model, currentUser, userService, messageRecipientService,
					committeeAndCommitteeMembersService);
		} else {
			model.addAttribute("currentUser", currentUser);
		}
		return "event";
	}

	@RequestMapping(value = "/public/getEventCoverPicture/{parentId}", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
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

	@RequestMapping(value = "/public/getEventVideo/{parentId}", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
	public ResponseEntity<byte[]> getVideo(@PathVariable("parentId") Long parentId) throws IOException {
		log.info("Parent id is : {}", parentId);
		Event event = eventService.findById(parentId);
		List<EventAndVideos> env = eventAndVideosService.findByEventAndIsPromotionalVideo(event);
		byte[] imageContent = env.get(0).getVideo().getVideo();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		log.info("returning image");
		return new ResponseEntity<byte[]>(imageContent, headers, HttpStatus.OK);
	}
}
