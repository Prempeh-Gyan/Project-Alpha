package org.isag_ghana.alpha.controller.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.isag_ghana.alpha.dto.AddNewsFormDTO;
import org.isag_ghana.alpha.model.NewsAndPhotos;
import org.isag_ghana.alpha.model.NewsAndVideos;
import org.isag_ghana.alpha.model.NewsBlog;
import org.isag_ghana.alpha.model.Photo;
import org.isag_ghana.alpha.model.User;
import org.isag_ghana.alpha.model.Video;
import org.isag_ghana.alpha.service.CommitteeAndCommitteeMembersService;
import org.isag_ghana.alpha.service.MessageRecipientService;
import org.isag_ghana.alpha.service.NewsAndPhotosService;
import org.isag_ghana.alpha.service.NewsAndVideosService;
import org.isag_ghana.alpha.service.NewsBlogService;
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
public class AddNewsController {

	private final @NotNull UserService userService;
	private final @NotNull NewsBlogService newsBlogService;
	private final @NotNull PhotoService photoService;
	private final @NotNull VideoService videoService;
	private final @NotNull NewsAndPhotosService newsAndPhotosService;
	private final @NotNull NewsAndVideosService newsAndVideosService;
	private final @NotNull MessageRecipientService messageRecipientService;
	private final @NotNull CommitteeAndCommitteeMembersService committeeAndCommitteeMembersService;

	@RequestMapping(value = { "/admin/add-news" }, method = RequestMethod.GET)
	public String getUpdateIndexPage(@CurrentUser User currentUser, Model model) {
		model.addAttribute("addNewsFormDTO", new AddNewsFormDTO());
		Notification.getNotifications(model, currentUser, userService, messageRecipientService,
				committeeAndCommitteeMembersService);
		return "add-news";
	}

	@RequestMapping(value = { "/admin/add-news" }, method = RequestMethod.POST)
	public String setUpdateIndexPage(@Valid @ModelAttribute("addNewsFormDTO") AddNewsFormDTO addNewsFormDTO,
			BindingResult bindingResult, Model model, @CurrentUser User currentUser,
			@RequestParam(name = "files") List<MultipartFile> files,
			@RequestParam(name = "coverPicture") MultipartFile coverPicture,
			@RequestParam(name = "newsVideos") List<MultipartFile> newsVideos,
			@RequestParam(name = "promotionalVideo") MultipartFile promotionalVideo,
			RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("addNewsFormDTO", addNewsFormDTO);
			Notification.getNotifications(model, currentUser, userService, messageRecipientService,
					committeeAndCommitteeMembersService);
			return "add-news";
		}

		NewsBlog blog = addNewsFormDTO.createBlog();
		blog.setAuthor(currentUser);

		Set<NewsAndPhotos> newsAndPhotos = new HashSet<>();

		log.info("Total number of files = {}", files.size());

		if (coverPicture != null && !coverPicture.isEmpty()) {
			if ((coverPicture.getOriginalFilename().contains(".")) && (coverPicture.getOriginalFilename()
					.substring(coverPicture.getOriginalFilename().lastIndexOf("."))
					.matches("((\\.(?i)(jpg||png||gif||bmp))$)"))) {
				log.info("file format is a match");
				// Save Cover Picture
				try {
					Photo photo = new Photo();
					photo.setImage(coverPicture.getBytes());
					photo.setName("coverPicture");
					photoService.save(photo);
					NewsAndPhotos newsAndPhoto = new NewsAndPhotos();
					newsAndPhoto.setNews(blog);
					newsAndPhoto.setIsCoverPicture(true);
					newsAndPhoto.setPhoto(photo);
					newsAndPhotos.add(newsAndPhoto);
					blog.setHasPhoto(true);
					blog.setHasCoverPhoto(true);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				model.addAttribute("addNewsFormDTO", addNewsFormDTO);
				model.addAttribute("fileFormatError", "File Format not accepted");
				Notification.getNotifications(model, currentUser, userService, messageRecipientService,
						committeeAndCommitteeMembersService);
				return "add-news";
			}
		}

		if (files != null && !files.isEmpty()) {

			for (MultipartFile file : files) {
				if (file.isEmpty()) {
					log.info("file is empty");
					continue; // go to the next file
				} else {
					log.info("file name is : {}", file.getOriginalFilename());
					log.info("ext is : {}",
							file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")));
					if ((file.getOriginalFilename().contains("."))
							&& (file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."))
									.matches("((\\.(?i)(jpg||png||gif||bmp))$)"))) {
						log.info("file format is a match");

						try {
							Photo photo = new Photo();
							photo.setImage(file.getBytes());
							photo.setName(
									file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")));
							photoService.save(photo);
							NewsAndPhotos newsAndPhoto = new NewsAndPhotos();
							newsAndPhoto.setNews(blog);
							newsAndPhoto.setPhoto(photo);
							newsAndPhotos.add(newsAndPhoto);
							log.info("Photo added : {}", file.getOriginalFilename());
							blog.setHasPhoto(true);
						} catch (IOException e) {
							e.printStackTrace();
						}

					} else {
						model.addAttribute("addNewsFormDTO", addNewsFormDTO);
						model.addAttribute("fileFormatError", "File Format not accepted");
						Notification.getNotifications(model, currentUser, userService, messageRecipientService,
								committeeAndCommitteeMembersService);
						return "add-news";
					}
				}
			}
		}

		Set<NewsAndVideos> newsAndVideos = new HashSet<>();

		if (promotionalVideo != null && !promotionalVideo.isEmpty()) {
			if ((promotionalVideo.getOriginalFilename().contains(".")) && (promotionalVideo.getOriginalFilename()
					.substring(promotionalVideo.getOriginalFilename().lastIndexOf(".")).matches(
							"((\\.(?i)(avi||amv||mp4||mpeg||svi||3gp||3g2||flv||m4v||wmv||mov||gif||mkv||webm||swf))$)"))) {
				log.info("file format is a match");
				// Save Promotional Video
				try {
					Video video = new Video();
					video.setVideo(promotionalVideo.getBytes());
					video.setName("promotionalVideo");
					videoService.save(video);
					NewsAndVideos newsAndvideo = new NewsAndVideos();
					newsAndvideo.setNews(blog);
					newsAndvideo.setVideo(video);
					newsAndvideo.setIsPromotionalVideo(true);
					newsAndVideos.add(newsAndvideo);
					log.info("Photo added : {}", promotionalVideo.getOriginalFilename());
					blog.setHasVideo(true);
					blog.setHasPromotionalVideo(true);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				model.addAttribute("addNewsFormDTO", addNewsFormDTO);
				model.addAttribute("fileFormatError", "File Format not accepted");
				Notification.getNotifications(model, currentUser, userService, messageRecipientService,
						committeeAndCommitteeMembersService);
				return "add-news";
			}
		}

		if (newsVideos != null && !newsVideos.isEmpty()) {

			for (MultipartFile videoFile : newsVideos) {
				if (videoFile.isEmpty()) {
					log.info("file is empty");
					continue; // go to the next file
				} else {
					log.info("file name is : {}", videoFile.getOriginalFilename());
					log.info("ext is : {}", videoFile.getOriginalFilename()
							.substring(videoFile.getOriginalFilename().lastIndexOf(".")));
					if ((videoFile.getOriginalFilename().contains(".")) && (videoFile.getOriginalFilename()
							.substring(videoFile.getOriginalFilename().lastIndexOf(".")).matches(
									"((\\.(?i)(avi||amv||mp4||mpeg||svi||3gp||3g2||flv||m4v||wmv||mov||gif||mkv||webm||swf))$)"))) {
						log.info("file format is a match");
						try {
							Video video = new Video();
							video.setVideo(videoFile.getBytes());
							video.setName(videoFile.getOriginalFilename()
									.substring(videoFile.getOriginalFilename().lastIndexOf(".")));
							videoService.save(video);
							NewsAndVideos newsAndvideo = new NewsAndVideos();
							newsAndvideo.setNews(blog);
							newsAndvideo.setVideo(video);
							newsAndVideos.add(newsAndvideo);
							log.info("Photo added : {}", videoFile.getOriginalFilename());
							blog.setHasVideo(true);
						} catch (IOException e) {
							e.printStackTrace();
						}

					} else {
						model.addAttribute("addNewsFormDTO", addNewsFormDTO);
						model.addAttribute("fileFormatError", "File Format not accepted");
						Notification.getNotifications(model, currentUser, userService, messageRecipientService,
								committeeAndCommitteeMembersService);
						return "add-news";
					}
				}
			}
		}

		newsBlogService.saveBlog(blog);
		newsAndPhotosService.save(new ArrayList<>(newsAndPhotos));
		newsAndVideosService.save(new ArrayList<>(newsAndVideos));

		redirectAttributes.addFlashAttribute("blogAddedSuccessfully", "You Have Successfully added a blog!");
		return "redirect:/admin/add-news";
	}
}
