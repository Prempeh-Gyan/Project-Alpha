package org.isag_ghana.alpha.controller.publik;

import java.io.IOException;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.isag_ghana.alpha.model.NewsAndPhotos;
import org.isag_ghana.alpha.model.NewsBlog;
import org.isag_ghana.alpha.model.User;
import org.isag_ghana.alpha.service.CommitteeAndCommitteeMembersService;
import org.isag_ghana.alpha.service.MessageRecipientService;
import org.isag_ghana.alpha.service.MessageService;
import org.isag_ghana.alpha.service.NewsAndPhotosService;
import org.isag_ghana.alpha.service.NewsAndVideosService;
import org.isag_ghana.alpha.service.NewsBlogService;
import org.isag_ghana.alpha.service.PhotoService;
import org.isag_ghana.alpha.service.UserService;
import org.isag_ghana.alpha.util.CurrentUser;
import org.isag_ghana.alpha.util.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor(onConstructor = @__({ @Autowired }))
public class NewsController {
	private final @NotNull NewsBlogService newsBlogService;
	private final @NotNull UserService userService;
	private final @NotNull PhotoService photoService;
	private final @NotNull MessageService messageService;
	private final @NotNull NewsAndPhotosService newsAndPhotosService;
	private final @NotNull NewsAndVideosService newsAndVideosService;
	private final @NotNull MessageRecipientService messageRecipientService;
	private final @NotNull CommitteeAndCommitteeMembersService committeeAndCommitteeMembersService;

	@RequestMapping(value = { "/public/news-list" }, method = RequestMethod.GET)
	public String newsBlogList(@CurrentUser User currentUser, Model model,
			@SortDefault(sort = "date", direction = Direction.DESC) Pageable pageable) {
		log.info("news fired...");
		model.addAttribute("page", newsBlogService.findAll(pageable));

		if (currentUser != null) {
			Notification.getNotifications(model, currentUser, userService, messageRecipientService,
					committeeAndCommitteeMembersService);
		} else {
			model.addAttribute("currentUser", currentUser);
		}

		return "news-list";
	}

	@RequestMapping(value = { "/public/news" }, method = RequestMethod.GET)
	public String newsBlog(@CurrentUser User currentUser, Model model,
			@SortDefault(sort = "date", direction = Direction.DESC) Pageable pageable) {
		log.info("news fired...");
		model.addAttribute("page", newsBlogService.findAll(pageable));
		if (currentUser != null) {
			Notification.getNotifications(model, currentUser, userService, messageRecipientService,
					committeeAndCommitteeMembersService);
		} else {
			model.addAttribute("currentUser", currentUser);
		}

		return "news-list";
	}

	@RequestMapping(value = { "/public/news/{url}" }, method = RequestMethod.GET)
	public String newsBlog(@CurrentUser User currentUser, Model model, @PathVariable("url") Long blogId) {
		log.info("BlogId is : {}", blogId);
		NewsBlog blog = newsBlogService.findById(blogId);
		model.addAttribute("blog", blog);
		model.addAttribute("blogPhotos", newsAndPhotosService.findAllByNews(blog));
		model.addAttribute("blogVideos", newsAndVideosService.findAllByNews(blog));
		if (currentUser != null) {
			Notification.getNotifications(model, currentUser, userService, messageRecipientService,
					committeeAndCommitteeMembersService);
		} else {
			model.addAttribute("currentUser", currentUser);
		}
		return "news";
	}

	@RequestMapping(value = { "/public/news" }, method = RequestMethod.POST)
	public String newsBlogPost(@CurrentUser User currentUser, Model model, @RequestParam Long id) {
		log.info("BlogId is ... {}", id);
		NewsBlog blog = newsBlogService.findById(id);
		model.addAttribute("blog", blog);
		model.addAttribute("blogPhotos", newsAndPhotosService.findAllByNews(blog));
		model.addAttribute("blogVideos", newsAndVideosService.findAllByNews(blog));
		if (currentUser != null) {
			Notification.getNotifications(model, currentUser, userService, messageRecipientService,
					committeeAndCommitteeMembersService);
		} else {
			model.addAttribute("currentUser", currentUser);
		}
		return "news";
	}

	@RequestMapping(value = "/public/getBlogPicture/{parentId}", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
	public ResponseEntity<byte[]> getImage(@PathVariable("parentId") Long parentId) throws IOException {
		log.info("Parent id is : {}", parentId);
		NewsBlog blog = newsBlogService.findById(parentId);
		List<NewsAndPhotos> nnp = newsAndPhotosService.findByNewsAndIsCoverPicture(blog);
		byte[] imageContent = nnp.get(0).getPhoto().getImage();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		log.info("returning image");
		return new ResponseEntity<byte[]>(imageContent, headers, HttpStatus.OK);
	}
}
