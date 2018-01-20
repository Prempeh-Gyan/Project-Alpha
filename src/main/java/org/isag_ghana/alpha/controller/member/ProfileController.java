package org.isag_ghana.alpha.controller.member;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.isag_ghana.alpha.dto.AddBusinessFormDTO;
import org.isag_ghana.alpha.model.Business;
import org.isag_ghana.alpha.model.BusinessAndPhotos;
import org.isag_ghana.alpha.model.BusinessAndVideos;
import org.isag_ghana.alpha.model.BusinessType;
import org.isag_ghana.alpha.model.Committee;
import org.isag_ghana.alpha.model.CommitteeAndCommitteeMember;
import org.isag_ghana.alpha.model.Photo;
import org.isag_ghana.alpha.model.Profile;
import org.isag_ghana.alpha.model.User;
import org.isag_ghana.alpha.model.Video;
import org.isag_ghana.alpha.service.BusinessAndPhotosService;
import org.isag_ghana.alpha.service.BusinessAndVideosService;
import org.isag_ghana.alpha.service.BusinessService;
import org.isag_ghana.alpha.service.BusinessTypeService;
import org.isag_ghana.alpha.service.CommitteeAndCommitteeMembersService;
import org.isag_ghana.alpha.service.CommitteeService;
import org.isag_ghana.alpha.service.MessageRecipientService;
import org.isag_ghana.alpha.service.PhotoService;
import org.isag_ghana.alpha.service.PositionAndPositionHoldersService;
import org.isag_ghana.alpha.service.ProductService;
import org.isag_ghana.alpha.service.ProfileService;
import org.isag_ghana.alpha.service.UserService;
import org.isag_ghana.alpha.service.VideoService;
import org.isag_ghana.alpha.util.CurrentUser;
import org.isag_ghana.alpha.util.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
public class ProfileController {

	private final @NotNull UserService userService;
	private final @NotNull PhotoService photoService;
	private final @NotNull MessageRecipientService messageRecipientService;
	private final @NotNull CommitteeService committeeService;
	private final @NotNull ProductService productService;
	private final @NotNull ProfileService profileService;
	private final @NotNull CommitteeAndCommitteeMembersService committeeAndCommitteeMembersService;
	private final @NotNull PositionAndPositionHoldersService positionAndPositionHoldersService;
	private final @NotNull BusinessTypeService businessTypeService;
	private final @NotNull BusinessService businessService;
	private final @NotNull BusinessAndPhotosService businessAndPhotosService;
	private final @NotNull BusinessAndVideosService businessAndVideosService;
	private final @NotNull VideoService videoService;

	@RequestMapping(value = { "/member/my-profile" }, method = RequestMethod.GET)
	public String getProfile(@CurrentUser User currentUser, Model model, Pageable pageable) {
		model.addAttribute("addBusinessFormDTO", new AddBusinessFormDTO());
		model.addAttribute("committees", committeeService.findAll());
		model.addAttribute("page", productService.findAllProductsByUser(currentUser, pageable));
		model.addAttribute("usersBusinesses", businessService.findAllByUser(currentUser));
		model.addAttribute("recentCommittees",
				committeeAndCommitteeMembersService.findAllCurrentCommitteesByUser(currentUser));
		model.addAttribute("recentPositions",
				positionAndPositionHoldersService.findAllCurrentPositionsByUser(currentUser));
		model.addAttribute("allCommittees", committeeAndCommitteeMembersService.findAllCommitteesByUser(currentUser));
		Notification.getNotifications(model, currentUser, userService, messageRecipientService,
				committeeAndCommitteeMembersService);
		return "my-profile";
	}

	@RequestMapping(value = { "/member/update-profile" }, method = RequestMethod.POST)
	public String setUpdateIndexPage(@CurrentUser User currentUser, @RequestParam(name = "firstName") String firstName,
			@RequestParam(name = "lastName") String lastName, @RequestParam(name = "email") String email,
			@RequestParam(name = "phoneNumber") String phoneNumber,
			@RequestParam(name = "profession") String profession,
			@RequestParam(name = "dateOfBirth") String dateOfBirth,
			@RequestParam(name = "countryOfResidence") String countryOfResidence,
			@RequestParam(name = "city") String city,
			@RequestParam(name = "profilePicture") MultipartFile profilePicture,
			RedirectAttributes redirectAttributes) {

		String emailRegex = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

		String phoneRegex = "\\d+";

		if (firstName == null || firstName == "" || firstName.isEmpty() || firstName.length() > 30) {
			redirectAttributes.addFlashAttribute("manageAccount", "true");
			redirectAttributes.addFlashAttribute("profileTab", 1);
			redirectAttributes.addFlashAttribute("firtNameError", "true");
			redirectAttributes.addFlashAttribute("profileError",
					"First Name cannot be empty or more than 30 characters!");
			return "redirect:/member/my-profile";
		} else if (lastName == null || lastName == "" || lastName.isEmpty() || lastName.length() > 30) {
			redirectAttributes.addFlashAttribute("manageAccount", "true");
			redirectAttributes.addFlashAttribute("profileTab", 1);
			redirectAttributes.addFlashAttribute("lastNameError", "true");
			redirectAttributes.addFlashAttribute("profileError",
					"Last Name cannot be empty or more than 30 characters!");
			return "redirect:/member/my-profile";
		} else if (email == null || email == "" || email.isEmpty() || !email.matches(emailRegex)) {
			redirectAttributes.addFlashAttribute("manageAccount", "true");
			redirectAttributes.addFlashAttribute("profileTab", 1);
			redirectAttributes.addFlashAttribute("emailError", "true");
			redirectAttributes.addFlashAttribute("profileError", "Incorrect email format!");
			return "redirect:/member/my-profile";
		} else if (phoneNumber == null || phoneNumber == "" || phoneNumber.isEmpty() || phoneNumber.length() > 30
				|| phoneNumber.length() < 10 || !phoneNumber.matches(phoneRegex)) {
			redirectAttributes.addFlashAttribute("manageAccount", "true");
			redirectAttributes.addFlashAttribute("profileTab", 1);
			redirectAttributes.addFlashAttribute("phoneNumberError", "true");
			redirectAttributes.addFlashAttribute("profileError",
					"Incorrect input for phone number, eighter too many digits or non number characters!");
			return "redirect:/member/my-profile";
		} else if (profession == null || profession == "" || profession.isEmpty() || profession.length() > 40) {
			redirectAttributes.addFlashAttribute("manageAccount", "true");
			redirectAttributes.addFlashAttribute("profileTab", 1);
			redirectAttributes.addFlashAttribute("professionError", "true");
			redirectAttributes.addFlashAttribute("profileError",
					"Profession cannot be empty or more than 40 characters!");
			return "redirect:/member/my-profile";
		} else if (dateOfBirth == null || dateOfBirth == "" || dateOfBirth.isEmpty() || dateOfBirth.length() > 10) {
			redirectAttributes.addFlashAttribute("manageAccount", "true");
			redirectAttributes.addFlashAttribute("profileTab", 1);
			redirectAttributes.addFlashAttribute("dateOfBirthError", "true");
			redirectAttributes.addFlashAttribute("profileError",
					"Date of birth cannot be empty or more than 10 characters!");
			return "redirect:/member/my-profile";
		} else if (countryOfResidence == null || countryOfResidence == "" || countryOfResidence.isEmpty()
				|| countryOfResidence.length() > 50) {
			redirectAttributes.addFlashAttribute("manageAccount", "true");
			redirectAttributes.addFlashAttribute("profileTab", 1);
			redirectAttributes.addFlashAttribute("countryOfResidenceError", "true");
			redirectAttributes.addFlashAttribute("profileError", "Country cannot be empty or more than 50 characters!");
			return "redirect:/member/my-profile";
		} else if (city == null || city == "" || city.isEmpty() || city.length() > 50) {
			redirectAttributes.addFlashAttribute("manageAccount", "true");
			redirectAttributes.addFlashAttribute("profileTab", 1);
			redirectAttributes.addFlashAttribute("cityError", "true");
			redirectAttributes.addFlashAttribute("profileError", "City cannot be empty or more than 50 characters!");
			return "redirect:/member/my-profile";
		}

		LocalDate usersDateOfBirth;

		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			usersDateOfBirth = LocalDate.parse(dateOfBirth, formatter);
		} catch (DateTimeParseException e) {
			redirectAttributes.addFlashAttribute("manageAccount", "true");
			redirectAttributes.addFlashAttribute("profileTab", 1);
			redirectAttributes.addFlashAttribute("dateOfBirthError", "true");
			redirectAttributes.addFlashAttribute("profileError", "Date of birth must be of format \"MM/dd/yyyy\"!");
			return "redirect:/member/my-profile";
		}

		Profile profile = currentUser.getProfile();

		if (profile.getPhoto() == null) {
			// check profile picture type
			if (profilePicture != null && !profilePicture.isEmpty()) {
				if ((profilePicture.getOriginalFilename().contains(".")) && (profilePicture.getOriginalFilename()
						.substring(profilePicture.getOriginalFilename().lastIndexOf("."))
						.matches("((\\.(?i)(jpg||png||gif||bmp))$)"))) {
					log.info("Profile Picture file format is correct, saving profile picture");
					// Save Profile Picture
					Photo photo = new Photo();
					try {
						photo.setImage(profilePicture.getBytes());
					} catch (IOException e) {
						redirectAttributes.addFlashAttribute("manageAccount", "true");
						redirectAttributes.addFlashAttribute("profileTab", 1);
						redirectAttributes.addFlashAttribute("profileError",
								"Something went wrong with saving your picture, please try again later!");
						return "redirect:/member/my-profile";
					}
					photo.setName("Profile Picture");
					photoService.save(photo);
					profile.setPhoto(photo);
				} else {
					redirectAttributes.addFlashAttribute("manageAccount", "true");
					redirectAttributes.addFlashAttribute("profileTab", 1);
					redirectAttributes.addFlashAttribute("profileError", "Invalid Pictue format!");
					return "redirect:/member/my-profile";
				}
			}
		} else {
			// check profile picture type
			if (profilePicture != null && !profilePicture.isEmpty()) {
				if ((profilePicture.getOriginalFilename().contains(".")) && (profilePicture.getOriginalFilename()
						.substring(profilePicture.getOriginalFilename().lastIndexOf("."))
						.matches("((\\.(?i)(jpg||png||gif||bmp))$)"))) {
					log.info("Profile Picture file format is correct, saving profile picture");
					// Save Profile Picture
					Photo photo = profile.getPhoto();
					try {
						photo.setImage(profilePicture.getBytes());
					} catch (IOException e) {
						redirectAttributes.addFlashAttribute("manageAccount", "true");
						redirectAttributes.addFlashAttribute("profileTab", 1);
						redirectAttributes.addFlashAttribute("profileError",
								"Something went wrong with saving your picture, please try again later!");
						return "redirect:/member/my-profile";
					}
					photoService.save(photo);
					profile.setPhoto(photo);
				} else {
					redirectAttributes.addFlashAttribute("manageAccount", "true");
					redirectAttributes.addFlashAttribute("profileTab", 1);
					redirectAttributes.addFlashAttribute("profileError", "Invalid Pictue format!");
					return "redirect:/member/my-profile";
				}
			}
		}

		profile.setFirstName(firstName);
		profile.setLastName(lastName);
		profile.setEmail(email);
		profile.setPhoneNumber(phoneNumber);
		profile.setProfession(profession);
		profile.setDateOfBirth(usersDateOfBirth);
		profile.setCountryOfResidence(countryOfResidence);
		profile.setCity(city);

		profileService.save(profile);

		redirectAttributes.addFlashAttribute("manageAccount", "true");
		redirectAttributes.addFlashAttribute("profileTab", 1);
		redirectAttributes.addFlashAttribute("profileSuccess", "You Have Successfully Updated Your Profile!");
		return "redirect:/member/my-profile";

	}

	@RequestMapping(value = { "/member/update-committee" }, method = RequestMethod.POST)
	public String updateCommittee(@CurrentUser User currentUser, @RequestParam("committeeId") Long committeeId,
			@RequestParam("leavingCommitteeId") Long leavingCommitteeId, RedirectAttributes redirectAttributes) {

		if (committeeId != -1) {
			Committee committee = committeeService.findById(committeeId);
			List<CommitteeAndCommitteeMember> committeeAndCommitteeMembers = committeeAndCommitteeMembersService
					.findCommitteeByUserAndCommittee(currentUser, committee);

			if (!committeeAndCommitteeMembers.isEmpty()) {
				redirectAttributes.addFlashAttribute("manageAccount", "true");
				redirectAttributes.addFlashAttribute("profileTab", 0);
				redirectAttributes.addFlashAttribute("committeeTab", "true");
				redirectAttributes.addFlashAttribute("committeeError",
						"Error! You are already a member of the " + committee.getName());
				return "redirect:/member/my-profile";
			}

			CommitteeAndCommitteeMember ccm = new CommitteeAndCommitteeMember();
			ccm.setCommittee(committee);
			ccm.setCommitteeMember(currentUser);
			ccm.setRequestedToJoin(true);

			if (committee.getName().contains("ADVOCACY")) {

				ccm.setIsAdvocacyCommittee(true);

			} else if (committee.getName().contains("EVENTS")) {

				ccm.setIsEventsCommittee(true);

			} else if (committee.getName().contains("MEMBERSHIP")) {

				ccm.setIsMembershipCommittee(true);

			} else if (committee.getName().contains("ADMINISTRATION")) {

				ccm.setIsAdministrationCommittee(true);

			} else if (committee.getName().contains("WEBSITE")) {

				ccm.setIsWebsiteCommittee(true);

			} else if (committee.getName().contains("ARCHIVES")) {

				ccm.setIsArchivesCommittee(true);
			} else if (committee.getName().contains("BAATSOONA")) {

				ccm.setIsBaatsoonaLandCommittee(true);

			}

			committeeAndCommitteeMembersService.save(ccm);
		}

		if (leavingCommitteeId != -1) {
			Committee committee = committeeService.findById(leavingCommitteeId);
			List<CommitteeAndCommitteeMember> committeeAndCommitteeMembers = committeeAndCommitteeMembersService
					.findCommitteeByUserAndCommittee(currentUser, committee);

			if (committeeAndCommitteeMembers.isEmpty()) {
				redirectAttributes.addFlashAttribute("manageAccount", "true");
				redirectAttributes.addFlashAttribute("profileTab", 0);
				redirectAttributes.addFlashAttribute("committeeTab", "true");
				redirectAttributes.addFlashAttribute("committeeError",
						"Error! You are not a member of the " + committee.getName());
				return "redirect:/member/my-profile";

			}

			CommitteeAndCommitteeMember ccm = committeeAndCommitteeMembers.get(0);
			ccm.setRequestedToLeave(true);

			committeeAndCommitteeMembersService.save(ccm);

		}

		redirectAttributes.addFlashAttribute("manageAccount", "true");
		redirectAttributes.addFlashAttribute("profileTab", 0);
		redirectAttributes.addFlashAttribute("committeeTab", "true");
		redirectAttributes.addFlashAttribute("committeeSuccess", "You Have Successfully Updated Your Committees!");
		return "redirect:/member/my-profile";

	}

	@RequestMapping(value = { "/member/add-business" }, method = RequestMethod.POST)
	public String addBusiness(@Valid @ModelAttribute("addBusinessFormDTO") AddBusinessFormDTO addBusinessFormDTO,
			BindingResult bindingResult, Model model, @CurrentUser User currentUser, Pageable pageable,
			@RequestParam(name = "files") List<MultipartFile> files,
			@RequestParam(name = "coverPicture") MultipartFile coverPicture,
			@RequestParam(name = "promotionalVideo") MultipartFile promotionalVideo,
			@RequestParam(name = "businessTypes") String businessTypes, RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("manageAccount", "true");
			model.addAttribute("profileTab", 0);
			model.addAttribute("businessTab", "true");
			model.addAttribute("page", productService.findAllProductsByUser(currentUser, pageable));
			model.addAttribute("addBusinessFormDTO", addBusinessFormDTO);
			model.addAttribute("committees", committeeService.findAll());
			model.addAttribute("recentCommittees",
					committeeAndCommitteeMembersService.findAllCurrentCommitteesByUser(currentUser));
			model.addAttribute("recentPositions",
					positionAndPositionHoldersService.findAllCurrentPositionsByUser(currentUser));
			model.addAttribute("allCommittees",
					committeeAndCommitteeMembersService.findAllCommitteesByUser(currentUser));
			Notification.getNotifications(model, currentUser, userService, messageRecipientService,
					committeeAndCommitteeMembersService);
			return "my-profile";
		}

		Set<String> businessTypesStrings = new HashSet<>();
		Set<BusinessType> businessTypesToSave = new HashSet<>();

		if (businessTypes != null && businessTypes != "" && businessTypes.contains("#")) {

			businessTypes = businessTypes.replaceAll("\\s+", "");
			StringTokenizer stringTokenizer = new StringTokenizer(businessTypes, "#");

			while (stringTokenizer.hasMoreTokens()) {
				String currentToken = stringTokenizer.nextToken().toLowerCase();
				businessTypesStrings.add(currentToken);
			}

		} else {
			model.addAttribute("manageAccount", "true");
			model.addAttribute("profileTab", 0);
			model.addAttribute("businessTab", "true");
			model.addAttribute("businessError", "Business Type(s) not specified!");
			model.addAttribute("page", productService.findAllProductsByUser(currentUser, pageable));
			model.addAttribute("addBusinessFormDTO", addBusinessFormDTO);
			model.addAttribute("committees", committeeService.findAll());
			model.addAttribute("recentCommittees",
					committeeAndCommitteeMembersService.findAllCurrentCommitteesByUser(currentUser));
			model.addAttribute("recentPositions",
					positionAndPositionHoldersService.findAllCurrentPositionsByUser(currentUser));
			model.addAttribute("allCommittees",
					committeeAndCommitteeMembersService.findAllCommitteesByUser(currentUser));
			Notification.getNotifications(model, currentUser, userService, messageRecipientService,
					committeeAndCommitteeMembersService);
			return "my-profile";
		}

		for (String tag : businessTypesStrings) {
			log.info("HashTag:  #{}", tag);
			BusinessType dbBusinessType = businessTypeService.findOne(tag);
			if (dbBusinessType == null) {
				log.info("#{} does not exist in the database, creating new hashtag", tag);
				BusinessType businessType = new BusinessType();
				businessType.setName(tag);
				businessTypeService.save(businessType);
				businessTypesToSave.add(businessType);
			} else {
				log.info("#{} already exist in the database, using already existing hastag : #{}", tag,
						dbBusinessType.getName());
				businessTypesToSave.add(dbBusinessType);
			}
		}

		Business business = addBusinessFormDTO.createBusiness();
		business.setOwner(currentUser);
		business.setBusinessTypes(businessTypesToSave);
		businessService.save(business);

		List<BusinessAndPhotos> businessAndPhotos = new ArrayList<>();

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
					BusinessAndPhotos businessAndPhoto = new BusinessAndPhotos();
					businessAndPhoto.setBusiness(business);
					businessAndPhoto.setIsCoverPicture(true);
					businessAndPhoto.setPhoto(photo);
					businessAndPhotos.add(businessAndPhoto);
				} catch (IOException e) {
					e.printStackTrace();
				}

			} else {
				model.addAttribute("manageAccount", "true");
				model.addAttribute("profileTab", 0);
				model.addAttribute("businessTab", "true");
				model.addAttribute("businessError", "File format is incorrect!");
				model.addAttribute("addBusinessFormDTO", addBusinessFormDTO);
				model.addAttribute("page", productService.findAllProductsByUser(currentUser, pageable));
				model.addAttribute("committees", committeeService.findAll());
				model.addAttribute("recentCommittees",
						committeeAndCommitteeMembersService.findAllCurrentCommitteesByUser(currentUser));
				model.addAttribute("recentPositions",
						positionAndPositionHoldersService.findAllCurrentPositionsByUser(currentUser));
				model.addAttribute("allCommittees",
						committeeAndCommitteeMembersService.findAllCommitteesByUser(currentUser));
				Notification.getNotifications(model, currentUser, userService, messageRecipientService,
						committeeAndCommitteeMembersService);
				return "my-profile";
			}
		}

		if (files != null && !files.isEmpty()) {
			for (MultipartFile file : files) {
				if (file.isEmpty()) {
					log.info("file is empty");
					continue; // go to the next file
				} else {
					log.info("file name is : {}", file.getOriginalFilename());
					if ((file.getOriginalFilename().contains("."))
							&& (file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."))
									.matches("((\\.(?i)(jpg||png||gif||bmp))$)"))) {

						log.info("file format is a match");

						try {
							Photo photo = new Photo();
							photo.setImage(file.getBytes());
							photo.setName(file.getOriginalFilename().substring(0,
									file.getOriginalFilename().lastIndexOf(".")));
							photoService.save(photo);
							BusinessAndPhotos businessAndPhoto = new BusinessAndPhotos();
							businessAndPhoto.setBusiness(business);
							businessAndPhoto.setPhoto(photo);
							businessAndPhotos.add(businessAndPhoto);
						} catch (IOException e) {
							e.printStackTrace();
						}

					} else {
						model.addAttribute("manageAccount", "true");
						model.addAttribute("profileTab", 0);
						model.addAttribute("businessTab", "true");
						model.addAttribute("businessError", "File format is incorrect!");
						model.addAttribute("addBusinessFormDTO", addBusinessFormDTO);
						model.addAttribute("page", productService.findAllProductsByUser(currentUser, pageable));
						model.addAttribute("committees", committeeService.findAll());
						model.addAttribute("recentCommittees",
								committeeAndCommitteeMembersService.findAllCurrentCommitteesByUser(currentUser));
						model.addAttribute("recentPositions",
								positionAndPositionHoldersService.findAllCurrentPositionsByUser(currentUser));
						model.addAttribute("allCommittees",
								committeeAndCommitteeMembersService.findAllCommitteesByUser(currentUser));
						Notification.getNotifications(model, currentUser, userService, messageRecipientService,
								committeeAndCommitteeMembersService);
						return "my-profile";
					}
				}
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
					video.setName(promotionalVideo.getOriginalFilename().substring(0,
							promotionalVideo.getOriginalFilename().lastIndexOf(".")));
					videoService.save(video);
					BusinessAndVideos businessAndVideo = new BusinessAndVideos();
					businessAndVideo.setBusiness(business);
					businessAndVideo.setIsPromotionalVideo(true);
					businessAndVideo.setVideo(video);
					businessAndVideosService.save(businessAndVideo);
				} catch (IOException e) {
					e.printStackTrace();
				}

			} else {
				model.addAttribute("manageAccount", "true");
				model.addAttribute("profileTab", 0);
				model.addAttribute("businessTab", "true");
				model.addAttribute("businessError", "File format is incorrect!");
				model.addAttribute("addBusinessFormDTO", addBusinessFormDTO);
				model.addAttribute("page", productService.findAllProductsByUser(currentUser, pageable));
				model.addAttribute("committees", committeeService.findAll());
				model.addAttribute("recentCommittees",
						committeeAndCommitteeMembersService.findAllCurrentCommitteesByUser(currentUser));
				model.addAttribute("recentPositions",
						positionAndPositionHoldersService.findAllCurrentPositionsByUser(currentUser));
				model.addAttribute("allCommittees",
						committeeAndCommitteeMembersService.findAllCommitteesByUser(currentUser));
				Notification.getNotifications(model, currentUser, userService, messageRecipientService,
						committeeAndCommitteeMembersService);
				return "my-profile";
			}
		}

		businessAndPhotos.parallelStream().forEach(bp -> {
			businessAndPhotosService.save(bp);
		});

		redirectAttributes.addFlashAttribute("manageAccount", "true");
		redirectAttributes.addFlashAttribute("profileTab", 0);
		redirectAttributes.addFlashAttribute("businessTab", "true");
		redirectAttributes.addFlashAttribute("businessSuccess", "You Have Successfully Added Your Business!");
		return "redirect:/member/my-profile";
	}

	@RequestMapping(value = { "/member/getBusiness" }, method = RequestMethod.POST)
	public String getBusiness(@RequestParam(name = "businessId") Long businessId,
			RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("manageAccount", "true");
		redirectAttributes.addFlashAttribute("profileTab", 0);
		redirectAttributes.addFlashAttribute("businessTab", "true");
		redirectAttributes.addFlashAttribute("business", businessService.findById(businessId));
		return "redirect:/member/my-profile";
	}

	@RequestMapping(value = { "/member/update-business" }, params = { "update", "!delete",
			"!cancel" }, method = RequestMethod.POST)
	public String updateBusiness(@CurrentUser User currentUser, @RequestParam(name = "businessId") Long businessId,
			@RequestParam(name = "companyName") String companyName, @RequestParam(name = "date") String date,
			@RequestParam(name = "address") String address, @RequestParam(name = "website") String website,
			@RequestParam(name = "email") String email, @RequestParam(name = "phoneNumber") String phoneNumber,
			@RequestParam(name = "buisinessDescription") String buisinessDescription,
			@RequestParam(name = "businessTypes") String businessTypes,
			@RequestParam(name = "coverPicture") MultipartFile coverPicture,
			@RequestParam(name = "promotionalVideo") MultipartFile promotionalVideo,
			@RequestParam(name = "files") List<MultipartFile> files, RedirectAttributes redirectAttributes) {

		String emailRegex = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

		String phoneRegex = "\\d+";

		if (companyName == null || companyName == "" || companyName.isEmpty() || companyName.length() > 200) {
			redirectAttributes.addFlashAttribute("manageAccount", "true");
			redirectAttributes.addFlashAttribute("profileTab", 0);
			redirectAttributes.addFlashAttribute("business", businessService.findById(businessId));
			redirectAttributes.addFlashAttribute("businessTab", "true");
			redirectAttributes.addFlashAttribute("companyNameError", "true");
			redirectAttributes.addFlashAttribute("businessError",
					"Business / Company Name cannot be empty or more than 200 characters!");
			return "redirect:/member/my-profile";
		} else if (address == null || address == "" || address.isEmpty() || address.length() > 300) {
			redirectAttributes.addFlashAttribute("manageAccount", "true");
			redirectAttributes.addFlashAttribute("profileTab", 0);
			redirectAttributes.addFlashAttribute("business", businessService.findById(businessId));
			redirectAttributes.addFlashAttribute("businessTab", "true");
			redirectAttributes.addFlashAttribute("addressError", "true");
			redirectAttributes.addFlashAttribute("businessError",
					"Business / Company Address cannot be empty or more than 300 characters!");
			return "redirect:/member/my-profile";
		} else if (website != null && website != "" && !website.isEmpty() && website.length() > 200) {
			redirectAttributes.addFlashAttribute("manageAccount", "true");
			redirectAttributes.addFlashAttribute("profileTab", 0);
			redirectAttributes.addFlashAttribute("business", businessService.findById(businessId));
			redirectAttributes.addFlashAttribute("businessTab", "true");
			redirectAttributes.addFlashAttribute("websiteError", "true");
			redirectAttributes.addFlashAttribute("businessError", "Website address is too long!");
			return "redirect:/member/my-profile";
		} else if (email != null && email != "" && !email.isEmpty() && !email.matches(emailRegex)) {
			redirectAttributes.addFlashAttribute("manageAccount", "true");
			redirectAttributes.addFlashAttribute("profileTab", 0);
			redirectAttributes.addFlashAttribute("business", businessService.findById(businessId));
			redirectAttributes.addFlashAttribute("businessTab", "true");
			redirectAttributes.addFlashAttribute("emailError", "true");
			redirectAttributes.addFlashAttribute("businessError", "Incorrect email format!");
			return "redirect:/member/my-profile";
		} else if (phoneNumber == null || phoneNumber == "" || phoneNumber.isEmpty() || phoneNumber.length() > 30
				|| phoneNumber.length() < 10 || !phoneNumber.matches(phoneRegex)) {
			redirectAttributes.addFlashAttribute("manageAccount", "true");
			redirectAttributes.addFlashAttribute("profileTab", 0);
			redirectAttributes.addFlashAttribute("business", businessService.findById(businessId));
			redirectAttributes.addFlashAttribute("businessTab", "true");
			redirectAttributes.addFlashAttribute("phoneNumberError", "true");
			redirectAttributes.addFlashAttribute("businessError",
					"Incorrect input for phone number, eighter too many digits or non number characters!");
			return "redirect:/member/my-profile";
		} else if (buisinessDescription == null || buisinessDescription == "" || buisinessDescription.isEmpty()
				|| buisinessDescription.length() > 2000) {
			redirectAttributes.addFlashAttribute("manageAccount", "true");
			redirectAttributes.addFlashAttribute("profileTab", 0);
			redirectAttributes.addFlashAttribute("business", businessService.findById(businessId));
			redirectAttributes.addFlashAttribute("businessTab", "true");
			redirectAttributes.addFlashAttribute("buisinessDescriptionError", "true");
			redirectAttributes.addFlashAttribute("businessError",
					"Business / Company Description cannot be empty or more than 2000 characters!");
			return "redirect:/member/my-profile";
		} else if (businessTypes == null || businessTypes == "" || businessTypes.isEmpty()
				|| !businessTypes.contains("#")) {
			redirectAttributes.addFlashAttribute("manageAccount", "true");
			redirectAttributes.addFlashAttribute("profileTab", 0);
			redirectAttributes.addFlashAttribute("business", businessService.findById(businessId));
			redirectAttributes.addFlashAttribute("businessTab", "true");
			redirectAttributes.addFlashAttribute("businessTypesError", "true");
			redirectAttributes.addFlashAttribute("businessError", "Please Specify Business Type with HashTags!");
			return "redirect:/member/my-profile";
		} else if (date != null && date != "" && !date.isEmpty() && date.length() > 10) {
			redirectAttributes.addFlashAttribute("manageAccount", "true");
			redirectAttributes.addFlashAttribute("profileTab", 0);
			redirectAttributes.addFlashAttribute("business", businessService.findById(businessId));
			redirectAttributes.addFlashAttribute("businessTab", "true");
			redirectAttributes.addFlashAttribute("dateError", "true");
			redirectAttributes.addFlashAttribute("businessError", "Date cannot be more than 10 characters!");
			return "redirect:/member/my-profile";
		}

		LocalDate dateFounded = null;

		if (date != null && date != "" && !date.isEmpty()) {
			try {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
				dateFounded = LocalDate.parse(date, formatter);
			} catch (DateTimeParseException e) {
				redirectAttributes.addFlashAttribute("manageAccount", "true");
				redirectAttributes.addFlashAttribute("profileTab", 0);
				redirectAttributes.addFlashAttribute("business", businessService.findById(businessId));
				redirectAttributes.addFlashAttribute("businessTab", "true");
				redirectAttributes.addFlashAttribute("dateError", "true");
				redirectAttributes.addFlashAttribute("businessError", "Invalid Date format, use: \"MM/dd/yyyy\"!");
				return "redirect:/member/my-profile";
			}
		}

		Set<String> businessTypesStrings = new HashSet<>();
		Set<BusinessType> businessTypesToSave = new HashSet<>();

		if (businessTypes != null && businessTypes != "" && businessTypes.contains("#")) {

			businessTypes = businessTypes.replaceAll("\\s+", "");
			StringTokenizer stringTokenizer = new StringTokenizer(businessTypes, "#");

			while (stringTokenizer.hasMoreTokens()) {
				String currentToken = stringTokenizer.nextToken().toLowerCase();
				businessTypesStrings.add(currentToken);
			}

		}

		for (String tag : businessTypesStrings) {
			log.info("HashTag:  #{}", tag);
			BusinessType dbBusinessType = businessTypeService.findOne(tag);
			if (dbBusinessType == null) {
				log.info("#{} does not exist in the database, creating new hashtag", tag);
				BusinessType businessType = new BusinessType();
				businessType.setName(tag);
				businessTypeService.save(businessType);
				businessTypesToSave.add(businessType);
			} else {
				log.info("#{} already exist in the database, using already existing hastag : #{}", tag,
						dbBusinessType.getName());
				businessTypesToSave.add(dbBusinessType);
			}
		}

		Business business = businessService.findById(businessId);

		business.setAddress(address);
		business.setBuisinessDescription(buisinessDescription);
		business.setBusinessTypes(businessTypesToSave);
		business.setCompanyName(companyName);
		business.setPhoneNumber(phoneNumber);

		if (dateFounded != null) {
			business.setDateFounded(dateFounded);
		}
		if (website != null && website != "" && !website.isEmpty()) {

			business.setWebsite(website);
		}
		if (email != null && email != "" && !email.isEmpty()) {

			business.setEmail(email);
		}

		businessService.save(business);

		List<BusinessAndPhotos> businessAndPhotos = new ArrayList<>();

		if (coverPicture != null && !coverPicture.isEmpty()) {
			if ((coverPicture.getOriginalFilename().contains(".")) && (coverPicture.getOriginalFilename()
					.substring(coverPicture.getOriginalFilename().lastIndexOf("."))
					.matches("((\\.(?i)(jpg||png||gif||bmp))$)"))) {
				log.info("Picture format is a match");

				// Save Cover Picture

				List<BusinessAndPhotos> bnp = businessAndPhotosService
						.findBusinessAndBusinessPhotoByBusinessAndIsCoverPicture(business);

				if (bnp.isEmpty()) {
					try {
						Photo photo = new Photo();
						photo.setImage(coverPicture.getBytes());
						photo.setName("coverPicture");
						photoService.save(photo);
						BusinessAndPhotos businessAndPhoto = new BusinessAndPhotos();
						businessAndPhoto.setBusiness(business);
						businessAndPhoto.setIsCoverPicture(true);
						businessAndPhoto.setPhoto(photo);
						businessAndPhotos.add(businessAndPhoto);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					try {
						Photo photo = bnp.get(0).getPhoto();
						photo.setImage(coverPicture.getBytes());
						photo.setName("coverPicture");
						photoService.save(photo);
						BusinessAndPhotos businessAndPhoto = bnp.get(0);
						businessAndPhoto.setIsCoverPicture(true);
						businessAndPhoto.setPhoto(photo);
						businessAndPhotos.add(businessAndPhoto);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			} else {
				redirectAttributes.addFlashAttribute("manageAccount", "true");
				redirectAttributes.addFlashAttribute("profileTab", 0);
				redirectAttributes.addFlashAttribute("business", businessService.findById(businessId));
				redirectAttributes.addFlashAttribute("businessTab", "true");
				redirectAttributes.addFlashAttribute("businessError", "Invalid Picture format!");
				return "redirect:/member/my-profile";
			}
		}

		List<BusinessAndPhotos> bnp = businessAndPhotosService
				.findBusinessAndBusinessPhotoByBusinessAndIsNotCoverPicture(business);

		int availableSpace = 10 - bnp.size();

		int maxIndexOfExistingPics = bnp.size() - 1;

		if (files != null && !files.isEmpty()) {

			for (int i = 0; i < files.size(); i++) {

				MultipartFile file = files.get(i);

				if (file.isEmpty()) {
					log.info("file is empty");
					continue; // go to the next file
				} else {
					log.info("file name is : {}", file.getOriginalFilename());
					if ((file.getOriginalFilename().contains("."))
							&& (file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."))
									.matches("((\\.(?i)(jpg||png||gif||bmp))$)"))) {

						log.info("file format is a match");

						if (availableSpace > 0) {
							try {
								Photo photo = new Photo();
								photo.setImage(file.getBytes());
								photo.setName(file.getOriginalFilename().substring(0,
										file.getOriginalFilename().lastIndexOf(".")));
								photoService.save(photo);
								BusinessAndPhotos businessAndPhoto = new BusinessAndPhotos();
								businessAndPhoto.setBusiness(business);
								businessAndPhoto.setPhoto(photo);
								businessAndPhotos.add(businessAndPhoto);
							} catch (IOException e) {
								e.printStackTrace();
							}
							availableSpace--;
						} else {
							if (maxIndexOfExistingPics >= 0) {
								try {
									Photo photo = bnp.get(maxIndexOfExistingPics).getPhoto();
									photo.setImage(file.getBytes());
									photo.setName(file.getOriginalFilename().substring(0,
											file.getOriginalFilename().lastIndexOf(".")));
									photoService.save(photo);
									BusinessAndPhotos businessAndPhoto = bnp.get(maxIndexOfExistingPics);
									businessAndPhoto.setBusiness(business);
									businessAndPhoto.setPhoto(photo);
									businessAndPhotos.add(businessAndPhoto);
								} catch (IOException e) {
									e.printStackTrace();
								}
								maxIndexOfExistingPics--;
							}
						}

					} else {
						redirectAttributes.addFlashAttribute("manageAccount", "true");
						redirectAttributes.addFlashAttribute("profileTab", 0);
						redirectAttributes.addFlashAttribute("business", businessService.findById(businessId));
						redirectAttributes.addFlashAttribute("businessTab", "true");
						redirectAttributes.addFlashAttribute("businessError", "Invalid Picture format!");
						return "redirect:/member/my-profile";
					}
				}
			}
		}

		if (promotionalVideo != null && !promotionalVideo.isEmpty()) {
			if ((promotionalVideo.getOriginalFilename().contains(".")) && (promotionalVideo.getOriginalFilename()
					.substring(promotionalVideo.getOriginalFilename().lastIndexOf(".")).matches(
							"((\\.(?i)(avi||amv||mp4||mpeg||svi||3gp||3g2||flv||m4v||wmv||mov||gif||mkv||webm||swf))$)"))) {
				log.info("Picture format is a match");

				// Save Promotional Video

				List<BusinessAndVideos> bnv = businessAndVideosService.findAllByBusiness(business);

				if (bnv.isEmpty()) {
					try {
						Video video = new Video();
						video.setVideo(promotionalVideo.getBytes());
						video.setName(promotionalVideo.getOriginalFilename().substring(0,
								promotionalVideo.getOriginalFilename().lastIndexOf(".")));
						videoService.save(video);
						BusinessAndVideos businessAndVideo = new BusinessAndVideos();
						businessAndVideo.setBusiness(business);
						businessAndVideo.setIsPromotionalVideo(true);
						businessAndVideo.setVideo(video);
						businessAndVideosService.save(businessAndVideo);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					try {
						Video video = bnv.get(0).getVideo();
						video.setVideo(promotionalVideo.getBytes());
						video.setName(promotionalVideo.getOriginalFilename().substring(0,
								promotionalVideo.getOriginalFilename().lastIndexOf(".")));
						videoService.save(video);
						BusinessAndVideos businessAndVideo = bnv.get(0);
						businessAndVideo.setIsPromotionalVideo(true);
						businessAndVideo.setVideo(video);
						businessAndVideosService.save(businessAndVideo);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			} else {
				redirectAttributes.addFlashAttribute("manageAccount", "true");
				redirectAttributes.addFlashAttribute("profileTab", 0);
				redirectAttributes.addFlashAttribute("business", businessService.findById(businessId));
				redirectAttributes.addFlashAttribute("businessTab", "true");
				redirectAttributes.addFlashAttribute("businessError", "Invalid Video format!");
				return "redirect:/member/my-profile";
			}
		}

		businessAndPhotos.parallelStream().forEach(bp -> {
			businessAndPhotosService.save(bp);
		});

		redirectAttributes.addFlashAttribute("manageAccount", "true");
		redirectAttributes.addFlashAttribute("profileTab", 0);
		redirectAttributes.addFlashAttribute("businessTab", "true");
		redirectAttributes.addFlashAttribute("businessSuccess", "You Have Successfully Updated Your Business!");
		return "redirect:/member/my-profile";
	}

	@RequestMapping(value = { "/member/update-business" }, params = { "delete", "!update",
			"!cancel" }, method = RequestMethod.POST)
	public String deleteBusiness(@CurrentUser User currentUser, @RequestParam(name = "businessId") Long businessId,
			@RequestParam(name = "companyName") String companyName, @RequestParam(name = "date") String date,
			@RequestParam(name = "address") String address, @RequestParam(name = "website") String website,
			@RequestParam(name = "email") String email, @RequestParam(name = "phoneNumber") String phoneNumber,
			@RequestParam(name = "buisinessDescription") String buisinessDescription,
			@RequestParam(name = "businessTypes") String businessTypes,
			@RequestParam(name = "coverPicture") MultipartFile coverPicture,
			@RequestParam(name = "promotionalVideo") MultipartFile promotionalVideo,
			@RequestParam(name = "files") List<MultipartFile> files, RedirectAttributes redirectAttributes) {

		Business business = businessService.findById(businessId);

		businessService.delete(business);

		redirectAttributes.addFlashAttribute("manageAccount", "true");
		redirectAttributes.addFlashAttribute("profileTab", 0);
		redirectAttributes.addFlashAttribute("businessTab", "true");
		redirectAttributes.addFlashAttribute("businessSuccess", "You Have Successfully Deleted Your Business!");
		return "redirect:/member/my-profile";
	}

	@RequestMapping(value = { "/member/update-business" }, params = { "cancel", "!update",
			"!delete" }, method = RequestMethod.POST)
	public String cancelBusiness(@CurrentUser User currentUser, @RequestParam(name = "businessId") String businessId,
			@RequestParam(name = "companyName") String companyName, @RequestParam(name = "date") String date,
			@RequestParam(name = "address") String address, @RequestParam(name = "website") String website,
			@RequestParam(name = "email") String email, @RequestParam(name = "phoneNumber") String phoneNumber,
			@RequestParam(name = "buisinessDescription") String buisinessDescription,
			@RequestParam(name = "businessTypes") String businessTypes,
			@RequestParam(name = "coverPicture") MultipartFile coverPicture,
			@RequestParam(name = "promotionalVideo") MultipartFile promotionalVideo,
			@RequestParam(name = "files") List<MultipartFile> files, RedirectAttributes redirectAttributes) {

		redirectAttributes.addFlashAttribute("manageAccount", "true");
		redirectAttributes.addFlashAttribute("profileTab", 0);
		redirectAttributes.addFlashAttribute("businessTab", "true");
		redirectAttributes.addFlashAttribute("businessSuccess", "You Have Cancelled The Operation On Your Business!");
		return "redirect:/member/my-profile";
	}
}
