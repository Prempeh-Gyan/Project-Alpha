package org.isag_ghana.alpha.controller.member;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.isag_ghana.alpha.model.Business;
import org.isag_ghana.alpha.model.BusinessAndPhotos;
import org.isag_ghana.alpha.model.BusinessAndVideos;
import org.isag_ghana.alpha.model.Photo;
import org.isag_ghana.alpha.model.User;
import org.isag_ghana.alpha.model.Video;
import org.isag_ghana.alpha.service.BusinessAndPhotosService;
import org.isag_ghana.alpha.service.BusinessAndVideosService;
import org.isag_ghana.alpha.service.BusinessService;
import org.isag_ghana.alpha.service.CommitteeAndCommitteeMembersService;
import org.isag_ghana.alpha.service.MessageRecipientService;
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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor(onConstructor = @__({ @Autowired }))
public class MembersBusinessesController {

	private final @NotNull UserService userService;
	private final @NotNull BusinessService businessService;
	private final @NotNull BusinessAndPhotosService businessAndPhotosService;
	private final @NotNull BusinessAndVideosService businessAndVideosService;
	private final @NotNull MessageRecipientService messageRecipientService;
	private final @NotNull CommitteeAndCommitteeMembersService committeeAndCommitteeMembersService;

	@RequestMapping(value = { "/member/member-owned-businesses" }, method = RequestMethod.GET)
	public String indexPage(@CurrentUser User currentUser, Model model,
			@SortDefault(sort = "companyName", direction = Direction.DESC) Pageable pageable) {

		log.info("Contact-Us page fired...");
		model.addAttribute("page", businessService.findAll(pageable));
		Notification.getNotifications(model, currentUser, userService, messageRecipientService,
				committeeAndCommitteeMembersService);
		return "member-owned-businesses";
	}

	@RequestMapping(value = { "/member/business/{businessId}" }, method = RequestMethod.GET)
	public String newsBlog(@CurrentUser User currentUser, Model model, @PathVariable("businessId") Long businessId) {
		log.info("businessId is : {}", businessId);

		Business business = businessService.findById(businessId);

		model.addAttribute("business", business);

		List<Photo> photos = new ArrayList<>();

		List<BusinessAndPhotos> bnp = businessAndPhotosService.findAllByBusiness(business);

		bnp.parallelStream().forEach(bp -> photos.add(bp.getPhoto()));

		model.addAttribute("businessPhotos", photos);

		List<Video> videos = new ArrayList<>();

		List<BusinessAndVideos> bnv = businessAndVideosService.findAllByBusiness(business);

		bnv.parallelStream().forEach(bv -> videos.add(bv.getVideo()));

		model.addAttribute("businessVideos", videos);

		Notification.getNotifications(model, currentUser, userService, messageRecipientService,
				committeeAndCommitteeMembersService);
		model.addAttribute("currentUser", currentUser);
		return "business";
	}

	@RequestMapping(value = "/member/getBusinessCoverPicture/{parentId}", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
	public ResponseEntity<byte[]> getImage(@PathVariable("parentId") Long parentId) throws IOException {
		log.info("Parent id is : {}", parentId);
		Business business = businessService.findById(parentId);
		List<BusinessAndPhotos> bnp = businessAndPhotosService
				.findBusinessAndBusinessPhotoByBusinessAndIsCoverPicture(business);
		byte[] imageContent = bnp.get(0).getPhoto().getImage();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		log.info("returning image");
		return new ResponseEntity<byte[]>(imageContent, headers, HttpStatus.OK);
	}
}
