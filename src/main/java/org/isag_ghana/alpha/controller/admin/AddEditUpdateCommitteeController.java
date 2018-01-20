package org.isag_ghana.alpha.controller.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.isag_ghana.alpha.dto.AddCommitteeFormDTO;
import org.isag_ghana.alpha.dto.EditCommitteeFormDTO;
import org.isag_ghana.alpha.model.Committee;
import org.isag_ghana.alpha.model.CommitteeAndCommitteeMember;
import org.isag_ghana.alpha.model.CommitteeAndDocuments;
import org.isag_ghana.alpha.model.CommitteeAndPhotos;
import org.isag_ghana.alpha.model.CommitteeAndVideos;
import org.isag_ghana.alpha.model.Document;
import org.isag_ghana.alpha.model.Photo;
import org.isag_ghana.alpha.model.User;
import org.isag_ghana.alpha.model.Video;
import org.isag_ghana.alpha.service.CommitteeAndCommitteeMembersService;
import org.isag_ghana.alpha.service.CommitteeAndDocumentsService;
import org.isag_ghana.alpha.service.CommitteeAndPhotosService;
import org.isag_ghana.alpha.service.CommitteeAndVideosService;
import org.isag_ghana.alpha.service.CommitteeService;
import org.isag_ghana.alpha.service.DocumentService;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor(onConstructor = @__({ @Autowired }))
public class AddEditUpdateCommitteeController {

	private final @NotNull UserService userService;
	private final @NotNull CommitteeService committeeService;
	private final @NotNull PhotoService photoService;
	private final @NotNull VideoService videoService;
	private final @NotNull DocumentService documentService;
	private final @NotNull CommitteeAndPhotosService committeeAndPhotosService;
	private final @NotNull CommitteeAndVideosService committeeAndVideosService;
	private final @NotNull CommitteeAndDocumentsService committeeAndDocumentsService;
	private final @NotNull MessageRecipientService messageRecipientService;
	private final @NotNull CommitteeAndCommitteeMembersService committeeAndCommitteeMembersService;

	@RequestMapping(value = { "/admin/add-edit-committee" }, method = RequestMethod.GET)
	public String getManageCommittee(@CurrentUser User currentUser, Model model) {
		log.info("rendering add-edit-committee...");
		model.addAttribute("addCommitteeFormDTO", new AddCommitteeFormDTO());
		model.addAttribute("editCommitteeFormDTO", new EditCommitteeFormDTO());

		if (currentUser.getRole().getId() < 3l) {
			model.addAttribute("currentCommitees", committeeService.findAll());
		} else {
			model.addAttribute("currentCommitees", getUsersCommittees(currentUser));
		}

		Notification.getNotifications(model, currentUser, userService, messageRecipientService,
				committeeAndCommitteeMembersService);
		return "add-edit-committee";
	}

	@RequestMapping(value = { "/admin/edit-existing-committee" }, method = RequestMethod.POST)
	public String setEditExistingCommittee(
			@Valid @ModelAttribute("editCommitteeFormDTO") EditCommitteeFormDTO editCommitteeFormDTO,
			BindingResult bindingResult, Model model, @CurrentUser User currentUser,
			@RequestParam(name = "coverPicture") MultipartFile coverPicture,
			@RequestParam(name = "committeeId") Long committeeId, RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors() || committeeId == -1) {
			model.addAttribute("addCommitteeFormDTO", new AddCommitteeFormDTO());
			model.addAttribute("editCommitteeFormDTO", editCommitteeFormDTO);
			if (currentUser.getRole().getId() < 3l) {
				model.addAttribute("currentCommitees", committeeService.findAll());
			} else {
				model.addAttribute("currentCommitees", getUsersCommittees(currentUser));
			}
			Notification.getNotifications(model, currentUser, userService, messageRecipientService,
					committeeAndCommitteeMembersService);
			model.addAttribute("duplicate", "Please select the Committee you wish to change");
			return "add-edit-committee";
		}

		Committee dbCommittee = committeeService.findById(committeeId);

		Committee committee = editCommitteeFormDTO.editCommittee(dbCommittee);

		if (coverPicture != null && !coverPicture.isEmpty()) {
			if ((coverPicture.getOriginalFilename().contains(".")) && (coverPicture.getOriginalFilename()
					.substring(coverPicture.getOriginalFilename().lastIndexOf("."))
					.matches("((\\.(?i)(jpg||png||gif||bmp))$)"))) {
				log.info("Picture format is a match");

				// Save Cover Picture
				Photo coverPhoto = new Photo();
				Set<Photo> photos = new HashSet<>();
				try {
					coverPhoto.setImage(coverPicture.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
				coverPhoto.setName("coverPicture");
				photoService.save(coverPhoto);
				photos.add(coverPhoto);

				photos.parallelStream().forEach((p) -> {
					CommitteeAndPhotos committeeAndPhotos = new CommitteeAndPhotos();
					committeeAndPhotos.setCommittee(committee);
					committeeAndPhotos.setPhoto(p);
					committeeAndPhotos.setIsCoverPicture(true);
					committeeAndPhotosService.save(committeeAndPhotos);
				});

			} else {
				model.addAttribute("addCommitteeFormDTO", new AddCommitteeFormDTO());
				model.addAttribute("editCommitteeFormDTO", editCommitteeFormDTO);
				if (currentUser.getRole().getId() < 3l) {
					model.addAttribute("currentCommitees", committeeService.findAll());
				} else {
					model.addAttribute("currentCommitees", getUsersCommittees(currentUser));
				}
				Notification.getNotifications(model, currentUser, userService, messageRecipientService,
						committeeAndCommitteeMembersService);
				model.addAttribute("duplicate", "Picture format is incorrect!");
				return "add-edit-committee";
			}
		}

		if (!dbCommittee.getName().equals(committee.getName())) {

			if (committeeService.findByName(committee.getName()) != null) {
				log.info("Duplicate Position!");
				model.addAttribute("addCommitteeFormDTO", new AddCommitteeFormDTO());
				model.addAttribute("editCommitteeFormDTO", editCommitteeFormDTO);
				if (currentUser.getRole().getId() < 3l) {
					model.addAttribute("currentCommitees", committeeService.findAll());
				} else {
					model.addAttribute("currentCommitees", getUsersCommittees(currentUser));
				}
				Notification.getNotifications(model, currentUser, userService, messageRecipientService,
						committeeAndCommitteeMembersService);
				model.addAttribute("duplicate", "This Committee already exist!");
				return "add-edit-committee";
			}

		}

		committeeService.save(committee);

		redirectAttributes.addFlashAttribute("addedSuccessfully", "You Have Successfully updated the Committee!");
		return "redirect:/admin/add-edit-committee";
	}

	@RequestMapping(value = { "/admin/getCommitteeDescription" }, method = RequestMethod.GET)
	public @ResponseBody String[] committeeDescription(@RequestParam("committeeId") Long committeeId) {

		String[] data = new String[2];

		if (committeeId != null && committeeId != -1) {
			Committee committee = committeeService.findById(committeeId);
			data[0] = committee.getName();
			data[1] = committee.getDescription();
		}

		return data;
	}

	@RequestMapping(value = { "/admin/add-committee-documents" }, method = RequestMethod.POST)
	public String setCommitteeDocuments(Model model, @CurrentUser User currentUser,
			@RequestParam("committeeId") Long committeeId,
			@RequestParam(name = "pictures") List<MultipartFile> pictures,
			@RequestParam(name = "videos") List<MultipartFile> videos,
			@RequestParam(name = "documents") List<MultipartFile> documents,
			@RequestParam(name = "reports") List<MultipartFile> reports,
			@RequestParam(name = "minutes") List<MultipartFile> minutes,
			@RequestParam(name = "confDocuments") List<MultipartFile> confDocuments,
			RedirectAttributes redirectAttributes) {

		if (committeeId == null || committeeId == -1) {
			model.addAttribute("addCommitteeFormDTO", new AddCommitteeFormDTO());
			model.addAttribute("editCommitteeFormDTO", new EditCommitteeFormDTO());
			if (currentUser.getRole().getId() < 3l) {
				model.addAttribute("currentCommitees", committeeService.findAll());
			} else {
				model.addAttribute("currentCommitees", getUsersCommittees(currentUser));
			}
			Notification.getNotifications(model, currentUser, userService, messageRecipientService,
					committeeAndCommitteeMembersService);
			model.addAttribute("duplicate2", "Please Select Committee first!");
			return "add-edit-committee";
		}

		Committee committee = committeeService.findById(committeeId);

		if (pictures != null && !pictures.isEmpty()) {

			Set<Photo> committeePictures = new HashSet<>();

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
							photo.setName(picture.getOriginalFilename().substring(0,
									picture.getOriginalFilename().lastIndexOf(".")));
							committeePictures.add(photo);
							log.info("Photo added : {}", picture.getOriginalFilename());
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						model.addAttribute("addCommitteeFormDTO", new AddCommitteeFormDTO());
						model.addAttribute("editCommitteeFormDTO", new EditCommitteeFormDTO());
						if (currentUser.getRole().getId() < 3l) {
							model.addAttribute("currentCommitees", committeeService.findAll());
						} else {
							model.addAttribute("currentCommitees", getUsersCommittees(currentUser));
						}
						Notification.getNotifications(model, currentUser, userService, messageRecipientService,
								committeeAndCommitteeMembersService);
						model.addAttribute("duplicate2", "the format : \"" + picture.getOriginalFilename()
								+ "\" is not a valid picture format!");
						return "add-edit-committee";
					}
				}
			}

			if (!committeePictures.isEmpty()) {
				committeePictures.parallelStream().forEach((photo) -> {
					log.info("saving photo: {}", photo.getName());
					photoService.save(photo);
				});
			}

			committeePictures.parallelStream().forEach((p) -> {
				CommitteeAndPhotos committeeAndPhotos = new CommitteeAndPhotos();
				committeeAndPhotos.setCommittee(committee);
				committeeAndPhotos.setPhoto(p);
				committeeAndPhotosService.save(committeeAndPhotos);
			});
		}

		if (videos != null && !videos.isEmpty()) {

			Set<Video> committeeVideos = new HashSet<>();

			for (MultipartFile video : videos) {
				if (video.isEmpty()) {
					log.info("picture is empty");
					continue; // go to the next file
				} else {
					log.info("Video name is : {}", video.getOriginalFilename());
					if ((video.getOriginalFilename().contains(".")) && (video.getOriginalFilename()
							.substring(video.getOriginalFilename().lastIndexOf(".")).matches(
									"((\\.(?i)(avi||amv||mp4||mpeg||svi||3gp||3g2||flv||m4v||wmv||mov||gif||mkv||webm||swf))$)"))) {
						log.info("Video ext is : {}",
								video.getOriginalFilename().substring(video.getOriginalFilename().lastIndexOf(".")));
						log.info("video format is a match");
						try {
							Video videoPicture = new Video();
							videoPicture.setVideo(video.getBytes());
							videoPicture.setName(video.getOriginalFilename().substring(0,
									video.getOriginalFilename().lastIndexOf(".")));
							committeeVideos.add(videoPicture);
							log.info("Video added : {}", video.getOriginalFilename());
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						model.addAttribute("addCommitteeFormDTO", new AddCommitteeFormDTO());
						model.addAttribute("editCommitteeFormDTO", new EditCommitteeFormDTO());
						if (currentUser.getRole().getId() < 3l) {
							model.addAttribute("currentCommitees", committeeService.findAll());
						} else {
							model.addAttribute("currentCommitees", getUsersCommittees(currentUser));
						}
						Notification.getNotifications(model, currentUser, userService, messageRecipientService,
								committeeAndCommitteeMembersService);
						model.addAttribute("duplicate2",
								"the format : \"" + video.getOriginalFilename() + "\" is not a valid video format!");
						return "add-edit-committee";
					}
				}
			}

			if (!committeeVideos.isEmpty()) {
				committeeVideos.parallelStream().forEach((video) -> {
					log.info("saving video: {}", video.getName());
					videoService.save(video);
				});
			}

			committeeVideos.parallelStream().forEach((v) -> {
				CommitteeAndVideos committeeAndVideos = new CommitteeAndVideos();
				committeeAndVideos.setCommittee(committee);
				committeeAndVideos.setVideo(v);
				committeeAndVideosService.save(committeeAndVideos);
			});

		}

		if (documents != null && !documents.isEmpty()) {

			Set<Document> committeeDocuments = new HashSet<>();

			for (MultipartFile document : documents) {
				if (document.isEmpty()) {
					log.info("document is empty");
					continue; // go to the next file
				} else {
					log.info("document name is : {}", document.getOriginalFilename());
					if ((document.getOriginalFilename().contains(".")) && (document.getOriginalFilename()
							.substring(document.getOriginalFilename().lastIndexOf("."))
							.matches("((\\.(?i)(doc||odt||pdf||rtf||tex||txt||wks||wpd))$)"))) {
						log.info("document ext is : {}", document.getOriginalFilename()
								.substring(document.getOriginalFilename().lastIndexOf(".")));
						log.info("document format is a match");
						try {
							Document committeeDocument = new Document();
							committeeDocument.setDocument(document.getBytes());
							committeeDocument.setName(document.getOriginalFilename().substring(0,
									document.getOriginalFilename().lastIndexOf(".")));
							committeeDocuments.add(committeeDocument);
							log.info("document added : {}", document.getOriginalFilename());
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						model.addAttribute("addCommitteeFormDTO", new AddCommitteeFormDTO());
						model.addAttribute("editCommitteeFormDTO", new EditCommitteeFormDTO());
						if (currentUser.getRole().getId() < 3l) {
							model.addAttribute("currentCommitees", committeeService.findAll());
						} else {
							model.addAttribute("currentCommitees", getUsersCommittees(currentUser));
						}
						Notification.getNotifications(model, currentUser, userService, messageRecipientService,
								committeeAndCommitteeMembersService);
						model.addAttribute("duplicate2", "the format : \"" + document.getOriginalFilename()
								+ "\" is not a valid document format!");
						return "add-edit-committee";
					}
				}
			}

			if (!committeeDocuments.isEmpty()) {
				committeeDocuments.parallelStream().forEach((document) -> {
					log.info("saving document: {}", document.getName());
					documentService.save(document);
				});
			}

			committeeDocuments.parallelStream().forEach((d) -> {
				CommitteeAndDocuments committeeAndDocuments = new CommitteeAndDocuments();
				committeeAndDocuments.setCommittee(committee);
				committeeAndDocuments.setDocument(d);
				committeeAndDocumentsService.save(committeeAndDocuments);
			});

		}

		if (reports != null && !reports.isEmpty()) {

			Set<Document> committeeReports = new HashSet<>();

			for (MultipartFile document : documents) {
				if (document.isEmpty()) {
					log.info("document is empty");
					continue; // go to the next file
				} else {
					log.info("document name is : {}", document.getOriginalFilename());
					if ((document.getOriginalFilename().contains(".")) && (document.getOriginalFilename()
							.substring(document.getOriginalFilename().lastIndexOf("."))
							.matches("((\\.(?i)(doc||odt||pdf||rtf||tex||txt||wks||wpd))$)"))) {
						log.info("document ext is : {}", document.getOriginalFilename()
								.substring(document.getOriginalFilename().lastIndexOf(".")));
						log.info("document format is a match");
						try {
							Document committeeDocument = new Document();
							committeeDocument.setDocument(document.getBytes());
							committeeDocument.setName(document.getOriginalFilename().substring(0,
									document.getOriginalFilename().lastIndexOf(".")));
							committeeReports.add(committeeDocument);
							log.info("document added : {}", document.getOriginalFilename());
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						model.addAttribute("addCommitteeFormDTO", new AddCommitteeFormDTO());
						model.addAttribute("editCommitteeFormDTO", new EditCommitteeFormDTO());
						if (currentUser.getRole().getId() < 3l) {
							model.addAttribute("currentCommitees", committeeService.findAll());
						} else {
							model.addAttribute("currentCommitees", getUsersCommittees(currentUser));
						}
						Notification.getNotifications(model, currentUser, userService, messageRecipientService,
								committeeAndCommitteeMembersService);
						model.addAttribute("duplicate2", "the format : \"" + document.getOriginalFilename()
								+ "\" is not a valid document format!");
						return "add-edit-committee";
					}
				}
			}

			if (!committeeReports.isEmpty()) {
				committeeReports.parallelStream().forEach((document) -> {
					log.info("saving document: {}", document.getName());
					documentService.save(document);
				});
			}

			committeeReports.parallelStream().forEach((d) -> {
				CommitteeAndDocuments committeeAndDocuments = new CommitteeAndDocuments();
				committeeAndDocuments.setCommittee(committee);
				committeeAndDocuments.setDocument(d);
				committeeAndDocuments.setIsReport(true);
				committeeAndDocumentsService.save(committeeAndDocuments);
			});

		}

		if (minutes != null && !minutes.isEmpty()) {

			Set<Document> committeeMinutes = new HashSet<>();

			for (MultipartFile document : minutes) {
				if (document.isEmpty()) {
					log.info("document is empty");
					continue; // go to the next file
				} else {
					log.info("document name is : {}", document.getOriginalFilename());
					if ((document.getOriginalFilename().contains(".")) && (document.getOriginalFilename()
							.substring(document.getOriginalFilename().lastIndexOf("."))
							.matches("((\\.(?i)(doc||odt||pdf||rtf||tex||txt||wks||wpd))$)"))) {
						log.info("document ext is : {}", document.getOriginalFilename()
								.substring(document.getOriginalFilename().lastIndexOf(".")));
						log.info("document format is a match");
						try {
							Document committeeMinute = new Document();
							committeeMinute.setDocument(document.getBytes());
							committeeMinute.setName(document.getOriginalFilename().substring(0,
									document.getOriginalFilename().lastIndexOf(".")));
							committeeMinutes.add(committeeMinute);
							log.info("document added : {}", document.getOriginalFilename());
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						model.addAttribute("addCommitteeFormDTO", new AddCommitteeFormDTO());
						model.addAttribute("editCommitteeFormDTO", new EditCommitteeFormDTO());
						if (currentUser.getRole().getId() < 3l) {
							model.addAttribute("currentCommitees", committeeService.findAll());
						} else {
							model.addAttribute("currentCommitees", getUsersCommittees(currentUser));
						}
						Notification.getNotifications(model, currentUser, userService, messageRecipientService,
								committeeAndCommitteeMembersService);
						model.addAttribute("duplicate2", "the format : \"" + document.getOriginalFilename()
								+ "\" is not a valid document format!");
						return "add-edit-committee";
					}
				}
			}

			if (!committeeMinutes.isEmpty()) {
				committeeMinutes.parallelStream().forEach((document) -> {
					log.info("saving document: {}", document.getName());
					documentService.save(document);
				});
			}

			committeeMinutes.parallelStream().forEach((d) -> {
				CommitteeAndDocuments committeeAndDocuments = new CommitteeAndDocuments();
				committeeAndDocuments.setCommittee(committee);
				committeeAndDocuments.setDocument(d);
				committeeAndDocuments.setIsMinute(true);
				committeeAndDocumentsService.save(committeeAndDocuments);
			});

		}

		if (confDocuments != null && !confDocuments.isEmpty()) {

			Set<Document> committeeConfDocuments = new HashSet<>();

			for (MultipartFile document : documents) {
				if (document.isEmpty()) {
					log.info("document is empty");
					continue; // go to the next file
				} else {
					log.info("document name is : {}", document.getOriginalFilename());
					if ((document.getOriginalFilename().contains(".")) && (document.getOriginalFilename()
							.substring(document.getOriginalFilename().lastIndexOf("."))
							.matches("((\\.(?i)(doc||odt||pdf||rtf||tex||txt||wks||wpd))$)"))) {
						log.info("document ext is : {}", document.getOriginalFilename()
								.substring(document.getOriginalFilename().lastIndexOf(".")));
						log.info("document format is a match");
						try {
							Document committeeDocument = new Document();
							committeeDocument.setDocument(document.getBytes());
							committeeDocument.setName(document.getOriginalFilename().substring(0,
									document.getOriginalFilename().lastIndexOf(".")));
							committeeConfDocuments.add(committeeDocument);
							log.info("document added : {}", document.getOriginalFilename());
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						model.addAttribute("addCommitteeFormDTO", new AddCommitteeFormDTO());
						model.addAttribute("editCommitteeFormDTO", new EditCommitteeFormDTO());
						if (currentUser.getRole().getId() < 3l) {
							model.addAttribute("currentCommitees", committeeService.findAll());
						} else {
							model.addAttribute("currentCommitees", getUsersCommittees(currentUser));
						}
						Notification.getNotifications(model, currentUser, userService, messageRecipientService,
								committeeAndCommitteeMembersService);
						model.addAttribute("duplicate2", "the format : \"" + document.getOriginalFilename()
								+ "\" is not a valid document format!");
						return "add-edit-committee";
					}
				}
			}

			if (!committeeConfDocuments.isEmpty()) {
				committeeConfDocuments.parallelStream().forEach((document) -> {
					log.info("saving document: {}", document.getName());
					documentService.save(document);
				});
			}

			committeeConfDocuments.parallelStream().forEach((d) -> {
				CommitteeAndDocuments committeeAndDocuments = new CommitteeAndDocuments();
				committeeAndDocuments.setCommittee(committee);
				committeeAndDocuments.setDocument(d);
				committeeAndDocuments.setIsConfidential(true);
				committeeAndDocumentsService.save(committeeAndDocuments);
			});

		}

		redirectAttributes.addFlashAttribute("addedSuccessfully",
				"You Have Successfully updated the Committee Documents!");
		return "redirect:/admin/add-edit-committee";
	}

	private List<Committee> getUsersCommittees(User user) {

		List<CommitteeAndCommitteeMember> committeeAndCommitteeMember = committeeAndCommitteeMembersService
				.findAllCurrentCommitteesByUser(user);

		List<Committee> committees = new ArrayList<>();

		if (!committeeAndCommitteeMember.isEmpty()) {

			committeeAndCommitteeMember.parallelStream().forEach(ccm -> committees.add(ccm.getCommittee()));
		}

		return committees;
	}
}
