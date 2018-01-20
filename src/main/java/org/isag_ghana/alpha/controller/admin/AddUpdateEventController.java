package org.isag_ghana.alpha.controller.admin;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.isag_ghana.alpha.dto.AddEventFormDTO;
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
import org.isag_ghana.alpha.service.PhotoService;
import org.isag_ghana.alpha.service.UserService;
import org.isag_ghana.alpha.service.VideoService;
import org.isag_ghana.alpha.util.CurrentUser;
import org.isag_ghana.alpha.util.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor(onConstructor = @__({ @Autowired }))
public class AddUpdateEventController {

	private final @NotNull MessageRecipientService messageRecipientService;
	private final @NotNull UserService userService;
	private final @NotNull PhotoService photoService;
	private final @NotNull VideoService videoService;
	private final @NotNull EventService eventService;
	private final @NotNull EventAndPhotosService eventAndPhotosService;
	private final @NotNull EventAndVideosService eventAndVideosService;
	private final @NotNull CommitteeAndCommitteeMembersService committeeAndCommitteeMembersService;

	@RequestMapping(value = { "/admin/add-event" }, method = RequestMethod.GET)
	public String indexPage(@CurrentUser User currentUser, Model model) {

		log.info("Contact-Us page fired...");
		Notification.getNotifications(model, currentUser, userService, messageRecipientService,
				committeeAndCommitteeMembersService);
		model.addAttribute("addEventFormDTO", new AddEventFormDTO());
		model.addAttribute("addEvent", "Yes");
		model.addAttribute("upCommingEvents", eventService.findAllUpComingISAGEvents());
		model.addAttribute("justEndedEvents", eventService.findJustEndedISAGEvent());
		return "add-event";
	}

	@RequestMapping(value = { "/admin/add-event" }, method = RequestMethod.POST)
	public String addEvent(@Valid @ModelAttribute("addEventFormDTO") AddEventFormDTO addEventFormDTO,
			BindingResult bindingResult, Model model, @CurrentUser User currentUser,
			@RequestParam(name = "coverPicture") MultipartFile coverPicture,
			@RequestParam(name = "promotionalVideo") MultipartFile promotionalVideo,
			RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("addEvent", "Yes");
			model.addAttribute("addEventFormDTO", addEventFormDTO);
			model.addAttribute("upCommingEvents", eventService.findAllUpComingISAGEvents());
			model.addAttribute("justEndedEvents", eventService.findJustEndedISAGEvent());
			Notification.getNotifications(model, currentUser, userService, messageRecipientService,
					committeeAndCommitteeMembersService);
			return "add-event";
		}

		Event event = addEventFormDTO.createEvent();
		eventService.save(event);

		if (coverPicture != null && !coverPicture.isEmpty()) {
			if ((coverPicture.getOriginalFilename().contains(".")) && (coverPicture.getOriginalFilename()
					.substring(coverPicture.getOriginalFilename().lastIndexOf("."))
					.matches("((\\.(?i)(jpg||png||gif||bmp))$)"))) {
				log.info("Picture format is a match");

				// Save Cover Picture
				try {
					Photo photo = new Photo();
					photo.setImage(coverPicture.getBytes());
					photo.setName("coverPicture");
					photoService.save(photo);

					EventAndPhotos eventAndPhotos = new EventAndPhotos();
					eventAndPhotos.setIsCoverPicture(true);
					eventAndPhotos.setPhoto(photo);
					eventAndPhotos.setEvent(event);
					eventAndPhotosService.save(eventAndPhotos);

					event.setHasPhoto(true);
					event.setHasCoverPicture(true);
					eventService.save(event);
				} catch (IOException e) {
					e.printStackTrace();
				}

			} else {
				model.addAttribute("addEvent", "Yes");
				model.addAttribute("error", "Picture format incorrect!");
				model.addAttribute("addEventFormDTO", addEventFormDTO);
				model.addAttribute("upCommingEvents", eventService.findAllUpComingISAGEvents());
				model.addAttribute("justEndedEvents", eventService.findJustEndedISAGEvent());
				Notification.getNotifications(model, currentUser, userService, messageRecipientService,
						committeeAndCommitteeMembersService);
				return "add-event";
			}
		}

		if (promotionalVideo != null && !promotionalVideo.isEmpty()) {
			if ((promotionalVideo.getOriginalFilename().contains(".")) && (promotionalVideo.getOriginalFilename()
					.substring(promotionalVideo.getOriginalFilename().lastIndexOf(".")).matches(
							"((\\.(?i)(avi||amv||mp4||mpeg||svi||3gp||3g2||flv||m4v||wmv||mov||gif||mkv||webm||swf))$)"))) {
				log.info("Picture format is a match");

				// Save Promotional Video
				try {
					Video video = new Video();
					video.setVideo(promotionalVideo.getBytes());
					video.setName("promotionalVideo");
					videoService.save(video);

					EventAndVideos eventAndVideo = new EventAndVideos();
					eventAndVideo.setEvent(event);
					eventAndVideo.setIsPromotionalVideo(true);
					eventAndVideo.setVideo(video);
					eventAndVideosService.save(eventAndVideo);

					event.setHasVideo(true);
					event.setHasPromotionalVideo(true);
					eventService.save(event);
				} catch (IOException e) {
					e.printStackTrace();
				}

			} else {
				model.addAttribute("addEvent", "Yes");
				model.addAttribute("error", "Video format incorrect!");
				model.addAttribute("addEventFormDTO", addEventFormDTO);
				model.addAttribute("upCommingEvents", eventService.findAllUpComingISAGEvents());
				model.addAttribute("justEndedEvents", eventService.findJustEndedISAGEvent());
				Notification.getNotifications(model, currentUser, userService, messageRecipientService,
						committeeAndCommitteeMembersService);
				return "add-event";
			}
		}

		redirectAttributes.addFlashAttribute("addEvent", "Yes");
		redirectAttributes.addFlashAttribute("success", "You Have Successfully Added An Event!");
		return "redirect:/admin/add-event";
	}

	@RequestMapping(value = { "/admin/edit-event" }, method = RequestMethod.POST)
	public String editEvent(@Valid @ModelAttribute("addEventFormDTO") AddEventFormDTO addEventFormDTO,
			BindingResult bindingResult, Model model, @CurrentUser User currentUser,
			@RequestParam(name = "coverPicture") MultipartFile coverPicture,
			@RequestParam(name = "eventId") Long eventId,
			@RequestParam(name = "promotionalVideo") MultipartFile promotionalVideo,
			RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("editEvent", "Yes");
			model.addAttribute("addEventFormDTO", addEventFormDTO);
			model.addAttribute("upCommingEvents", eventService.findAllUpComingISAGEvents());
			model.addAttribute("justEndedEvents", eventService.findJustEndedISAGEvent());
			Notification.getNotifications(model, currentUser, userService, messageRecipientService,
					committeeAndCommitteeMembersService);
			return "add-event";
		}

		if (eventId == -1) {
			model.addAttribute("editEvent", "Yes");
			model.addAttribute("error", "Please Select The Event You Wish To Edit!");
			model.addAttribute("addEventFormDTO", addEventFormDTO);
			model.addAttribute("upCommingEvents", eventService.findAllUpComingISAGEvents());
			model.addAttribute("justEndedEvents", eventService.findJustEndedISAGEvent());
			Notification.getNotifications(model, currentUser, userService, messageRecipientService,
					committeeAndCommitteeMembersService);
			return "add-event";
		}

		Event event = addEventFormDTO.editEvent(eventService.findById(eventId));
		eventService.save(event);

		if (coverPicture != null && !coverPicture.isEmpty()) {
			if ((coverPicture.getOriginalFilename().contains(".")) && (coverPicture.getOriginalFilename()
					.substring(coverPicture.getOriginalFilename().lastIndexOf("."))
					.matches("((\\.(?i)(jpg||png||gif||bmp))$)"))) {
				log.info("Picture format is a match");

				// Save Cover Picture
				try {

					List<EventAndPhotos> enp = eventAndPhotosService.findByEventAndIsCoverPicture(event);

					if (!enp.isEmpty()) {
						EventAndPhotos eventAndPhotos = enp.get(0);
						Photo photo = eventAndPhotos.getPhoto();
						photo.setImage(coverPicture.getBytes());
						photoService.save(photo);

					} else {
						Photo photo = new Photo();
						photo.setImage(coverPicture.getBytes());
						photo.setName("coverPicture");
						photoService.save(photo);

						EventAndPhotos eventAndPhotos = new EventAndPhotos();
						eventAndPhotos.setIsCoverPicture(true);
						eventAndPhotos.setPhoto(photo);
						eventAndPhotos.setEvent(event);
						eventAndPhotosService.save(eventAndPhotos);

						event.setHasPhoto(true);
						event.setHasCoverPicture(true);
						eventService.save(event);
					}

				} catch (IOException e) {
					e.printStackTrace();
				}

			} else {
				model.addAttribute("editEvent", "Yes");
				model.addAttribute("error", "Picture format incorrect!");
				model.addAttribute("addEventFormDTO", addEventFormDTO);
				model.addAttribute("upCommingEvents", eventService.findAllUpComingISAGEvents());
				model.addAttribute("justEndedEvents", eventService.findJustEndedISAGEvent());
				Notification.getNotifications(model, currentUser, userService, messageRecipientService,
						committeeAndCommitteeMembersService);
				return "add-event";
			}
		}

		if (promotionalVideo != null && !promotionalVideo.isEmpty()) {
			if ((promotionalVideo.getOriginalFilename().contains(".")) && (promotionalVideo.getOriginalFilename()
					.substring(promotionalVideo.getOriginalFilename().lastIndexOf(".")).matches(
							"((\\.(?i)(avi||amv||mp4||mpeg||svi||3gp||3g2||flv||m4v||wmv||mov||gif||mkv||webm||swf))$)"))) {
				log.info("Picture format is a match");

				// Save Promotional Video
				try {

					List<EventAndVideos> env = eventAndVideosService.findByEventAndIsPromotionalVideo(event);
					if (!env.isEmpty()) {

						EventAndVideos eventAndVideo = env.get(0);
						Video video = eventAndVideo.getVideo();
						video.setVideo(promotionalVideo.getBytes());
						videoService.save(video);
					} else {
						Video video = new Video();
						video.setVideo(promotionalVideo.getBytes());
						video.setName("promotionalVideo");
						videoService.save(video);

						EventAndVideos eventAndVideo = new EventAndVideos();
						eventAndVideo.setEvent(event);
						eventAndVideo.setIsPromotionalVideo(true);
						eventAndVideo.setVideo(video);
						eventAndVideosService.save(eventAndVideo);

						event.setHasVideo(true);
						event.setHasPromotionalVideo(true);
						eventService.save(event);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			} else {
				model.addAttribute("editEvent", "Yes");
				model.addAttribute("error", "Video format incorrect!");
				model.addAttribute("addEventFormDTO", addEventFormDTO);
				model.addAttribute("upCommingEvents", eventService.findAllUpComingISAGEvents());
				model.addAttribute("justEndedEvents", eventService.findJustEndedISAGEvent());
				Notification.getNotifications(model, currentUser, userService, messageRecipientService,
						committeeAndCommitteeMembersService);
				return "add-event";
			}
		}

		redirectAttributes.addFlashAttribute("editEvent", "Yes");
		redirectAttributes.addFlashAttribute("success", "You Have Successfully Changed The Event!");
		return "redirect:/admin/add-event";
	}

	@RequestMapping(value = { "/admin/update-event" }, method = RequestMethod.POST)
	public String updateEvent(Model model, @CurrentUser User currentUser,
			@RequestParam(name = "eventSummary") String eventSummary,
			@RequestParam(name = "pictures") List<MultipartFile> pictures,
			@RequestParam(name = "videos") List<MultipartFile> videos, @RequestParam(name = "eventId") Long eventId,
			RedirectAttributes redirectAttributes) {

		if (eventId == -1) {
			model.addAttribute("updateEvent", "Yes");
			model.addAttribute("error", "Please Select The Event You Wish To Edit!");
			model.addAttribute("addEventFormDTO", new AddEventFormDTO());
			model.addAttribute("upCommingEvents", eventService.findAllUpComingISAGEvents());
			model.addAttribute("justEndedEvents", eventService.findJustEndedISAGEvent());
			Notification.getNotifications(model, currentUser, userService, messageRecipientService,
					committeeAndCommitteeMembersService);
			return "add-event";
		}

		Event event = eventService.findById(eventId);
		event.setIsPassed(true);
		if (eventSummary != null || eventSummary != "" || !eventSummary.isEmpty()) {
			event.setSummary(eventSummary);
		}
		eventService.save(event);

		if (pictures != null && !pictures.isEmpty()) {

			for (MultipartFile picture : pictures) {
				if (picture.isEmpty()) {
					log.info("picture is empty");
					continue; // go to the next file
				} else {
					log.info("picture name is : {}", picture.getOriginalFilename());
					if ((picture.getOriginalFilename().contains("."))
							&& (picture.getOriginalFilename().substring(picture.getOriginalFilename().lastIndexOf("."))
									.matches("((\\.(?i)(jpg||png||gif||bmp))$)"))) {
						log.info("picture ext is : {}", picture.getOriginalFilename()
								.substring(picture.getOriginalFilename().lastIndexOf(".")));
						log.info("picture format is a match");
						try {
							Photo photo = new Photo();
							photo.setImage(picture.getBytes());
							photo.setName(picture.getOriginalFilename()
									.substring(picture.getOriginalFilename().lastIndexOf(".")));
							photoService.save(photo);

							EventAndPhotos eventAndPhotos = new EventAndPhotos();
							eventAndPhotos.setPhoto(photo);
							eventAndPhotos.setEvent(event);
							eventAndPhotos.setIsAfterEvent(true);
							eventAndPhotosService.save(eventAndPhotos);

							event.setHasPhoto(true);
							eventService.save(event);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						model.addAttribute("updateEvent", "Yes");
						model.addAttribute("error", "Invalid picture format!");
						model.addAttribute("addEventFormDTO", new AddEventFormDTO());
						model.addAttribute("upCommingEvents", eventService.findAllUpComingISAGEvents());
						model.addAttribute("justEndedEvents", eventService.findJustEndedISAGEvent());
						Notification.getNotifications(model, currentUser, userService, messageRecipientService,
								committeeAndCommitteeMembersService);
						return "add-event";
					}
				}
			}
		}

		if (videos != null && !videos.isEmpty()) {

			for (MultipartFile eventvideo : videos) {
				if (eventvideo.isEmpty()) {
					log.info("video is empty");
					continue; // go to the next file
				} else {
					log.info("Video name is : {}", eventvideo.getOriginalFilename());
					if ((eventvideo.getOriginalFilename().contains(".")) && (eventvideo.getOriginalFilename()
							.substring(eventvideo.getOriginalFilename().lastIndexOf(".")).matches(
									"((\\.(?i)(avi||amv||mp4||mpeg||svi||3gp||3g2||flv||m4v||wmv||mov||gif||mkv||webm||swf))$)"))) {
						log.info("Video ext is : {}", eventvideo.getOriginalFilename()
								.substring(eventvideo.getOriginalFilename().lastIndexOf(".")));
						log.info("video format is a match");
						try {
							Video video = new Video();
							video.setVideo(eventvideo.getBytes());
							video.setName(eventvideo.getOriginalFilename().substring(0,
									eventvideo.getOriginalFilename().lastIndexOf(".")));
							videoService.save(video);

							EventAndVideos eventAndVideo = new EventAndVideos();
							eventAndVideo.setEvent(event);
							eventAndVideo.setVideo(video);
							eventAndVideo.setIsAfterEvent(true);
							eventAndVideosService.save(eventAndVideo);

							event.setHasVideo(true);
							eventService.save(event);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						model.addAttribute("updateEvent", "Yes");
						model.addAttribute("error", "Invalid video format!");
						model.addAttribute("addEventFormDTO", new AddEventFormDTO());
						model.addAttribute("upCommingEvents", eventService.findAllUpComingISAGEvents());
						model.addAttribute("justEndedEvents", eventService.findJustEndedISAGEvent());
						Notification.getNotifications(model, currentUser, userService, messageRecipientService,
								committeeAndCommitteeMembersService);
						return "add-event";
					}
				}
			}
		}

		redirectAttributes.addFlashAttribute("updateEvent", "Yes");
		redirectAttributes.addFlashAttribute("success", "You Have Successfully Updated The Event!");
		return "redirect:/admin/add-event";
	}

}
