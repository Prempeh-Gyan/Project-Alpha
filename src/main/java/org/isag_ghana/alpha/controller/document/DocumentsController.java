package org.isag_ghana.alpha.controller.document;

import javax.validation.constraints.NotNull;

import org.isag_ghana.alpha.model.User;
import org.isag_ghana.alpha.service.CommitteeAndCommitteeMembersService;
import org.isag_ghana.alpha.service.CommitteeAndDocumentsService;
import org.isag_ghana.alpha.service.CommitteeService;
import org.isag_ghana.alpha.service.DocumentService;
import org.isag_ghana.alpha.service.GroupService;
import org.isag_ghana.alpha.service.MessageRecipientService;
import org.isag_ghana.alpha.service.MessageService;
import org.isag_ghana.alpha.service.PhotoService;
import org.isag_ghana.alpha.service.UserService;
import org.isag_ghana.alpha.util.CurrentUser;
import org.isag_ghana.alpha.util.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.SortDefault;
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
public class DocumentsController {
	private final @NotNull PhotoService photoService;
	private final @NotNull UserService userService;
	private final @NotNull DocumentService documentService;
	private final @NotNull MessageRecipientService messageRecipientService;
	private final @NotNull GroupService groupService;
	private final @NotNull MessageService messageService;
	private final @NotNull CommitteeAndCommitteeMembersService committeeAndCommitteeMembersService;
	private final @NotNull CommitteeAndDocumentsService committeeAndDocumentsService;
	private final @NotNull CommitteeService committeeService;

	@RequestMapping(value = { "/document/document" }, method = RequestMethod.GET)
	public String getPdfViewer1(RedirectAttributes redirectAttributes) {
		redirectAttributes.addAttribute("file", "ISAGConstitution.pdf");
		return "redirect:/web/viewer";
	}

	@RequestMapping(value = { "/web/viewer" }, method = RequestMethod.GET)
	public String getPdfViewer2(RedirectAttributes redirectAttributes) {
		return "../../viewResources/web/viewer";
	}

	@RequestMapping(value = { "/document/ISAG-Constitution" }, method = RequestMethod.GET)
	public String getISAGConstitution(@CurrentUser User currentUser, Model model,
			@SortDefault(sort = "date", direction = Direction.DESC) Pageable pageable) {
		log.info("Document Requested!");
		model.addAttribute("page", messageService.findAllMessagesByUser(currentUser, pageable));
		Notification.getNotifications(model, currentUser, userService, messageRecipientService,
				committeeAndCommitteeMembersService);
		return "ISAG-Constitution";
	}

	@RequestMapping(value = { "/document/ISAG-Registration" }, method = RequestMethod.GET)
	public String getISAGRegistration(@CurrentUser User currentUser, Model model,
			@SortDefault(sort = "date", direction = Direction.DESC) Pageable pageable) {
		log.info("Document Requested!");
		model.addAttribute("page", messageService.findAllMessagesByUser(currentUser, pageable));
		Notification.getNotifications(model, currentUser, userService, messageRecipientService,
				committeeAndCommitteeMembersService);
		return "ISAG-Registration";
	}

	@RequestMapping(value = { "/document/Citizenship-Act" }, method = RequestMethod.GET)
	public String getCitizenshipAct(@CurrentUser User currentUser, Model model,
			@SortDefault(sort = "date", direction = Direction.DESC) Pageable pageable) {
		log.info("Document Requested!");
		model.addAttribute("page", messageService.findAllMessagesByUser(currentUser, pageable));
		Notification.getNotifications(model, currentUser, userService, messageRecipientService,
				committeeAndCommitteeMembersService);
		return "Citizenship-Act";
	}

	@RequestMapping(value = { "/document/Immigration-Act" }, method = RequestMethod.GET)
	public String getImmigrationAct(@CurrentUser User currentUser, Model model,
			@SortDefault(sort = "date", direction = Direction.DESC) Pageable pageable) {
		log.info("Document Requested!");
		model.addAttribute("page", messageService.findAllMessagesByUser(currentUser, pageable));
		Notification.getNotifications(model, currentUser, userService, messageRecipientService,
				committeeAndCommitteeMembersService);
		return "Immigration-Act";
	}

	@RequestMapping(value = { "/document/Immigration-Requirements" }, method = RequestMethod.GET)
	public String getImmigrationRequirements(@CurrentUser User currentUser, Model model,
			@SortDefault(sort = "date", direction = Direction.DESC) Pageable pageable) {
		log.info("Document Requested!");
		model.addAttribute("page", messageService.findAllMessagesByUser(currentUser, pageable));
		Notification.getNotifications(model, currentUser, userService, messageRecipientService,
				committeeAndCommitteeMembersService);
		return "Immigration-Requirements";
	}

	@RequestMapping(value = { "/document/Property-Rights-Of-Spouses" }, method = RequestMethod.GET)
	public String getPropertyRightsOfSpouses(@CurrentUser User currentUser, Model model,
			@SortDefault(sort = "date", direction = Direction.DESC) Pageable pageable) {
		log.info("Document Requested!");
		model.addAttribute("page", messageService.findAllMessagesByUser(currentUser, pageable));
		Notification.getNotifications(model, currentUser, userService, messageRecipientService,
				committeeAndCommitteeMembersService);
		return "Property-Rights-Of-Spouses";
	}

	@RequestMapping(value = { "/document/Intestate-Succession-Bill" }, method = RequestMethod.GET)
	public String getIntestateSuccessionBill(@CurrentUser User currentUser, Model model,
			@SortDefault(sort = "date", direction = Direction.DESC) Pageable pageable) {
		log.info("Document Requested!");
		model.addAttribute("page", messageService.findAllMessagesByUser(currentUser, pageable));
		Notification.getNotifications(model, currentUser, userService, messageRecipientService,
				committeeAndCommitteeMembersService);
		return "Intestate-Succession-Bill";
	}

	@RequestMapping(value = { "/document/Domestic-Violence-Act" }, method = RequestMethod.GET)
	public String getDomesticViolenceAct(@CurrentUser User currentUser, Model model,
			@SortDefault(sort = "date", direction = Direction.DESC) Pageable pageable) {
		log.info("Document Requested!");
		model.addAttribute("page", messageService.findAllMessagesByUser(currentUser, pageable));
		Notification.getNotifications(model, currentUser, userService, messageRecipientService,
				committeeAndCommitteeMembersService);
		return "Domestic-Violence-Act";
	}

	@RequestMapping(value = { "/document/minutes/{committeeId}" }, method = RequestMethod.GET)
	public String getCommitteeMinutes(@CurrentUser User currentUser, Model model,
			@SortDefault(sort = "date", direction = Direction.DESC) Pageable pageable,
			@PathVariable("committeeId") Long committeeId) {

		if (committeeId != null && committeeId > 2) {

			model.addAttribute("page", committeeAndDocumentsService
					.findAllMinutesByCommittee(committeeService.findById(committeeId), pageable));

			model.addAttribute("committeeId", committeeId);

		} else {

			model.addAttribute("page",
					committeeAndDocumentsService.findAllMinutesByCommittee(committeeService.findById(2l), pageable));

			model.addAttribute("committeeId", 2);
		}

		Notification.getNotifications(model, currentUser, userService, messageRecipientService,
				committeeAndCommitteeMembersService);

		return "minutes";
	}

	@RequestMapping(value = { "/document/minute/{committeeId},{minuteId}" }, method = RequestMethod.GET)
	public String getCommitteeMinute(@CurrentUser User currentUser, Model model,
			@PathVariable("committeeId") Long committeeId, @PathVariable("minuteId") Long minuteId) {

		model.addAttribute("document", committeeAndDocumentsService.findById(minuteId).getDocument());

		Notification.getNotifications(model, currentUser, userService, messageRecipientService,
				committeeAndCommitteeMembersService);

		return "minute";
	}

	@RequestMapping(value = { "/document/reports/{committeeId}" }, method = RequestMethod.GET)
	public String getCommitteeReports(@CurrentUser User currentUser, Model model,
			@SortDefault(sort = "date", direction = Direction.DESC) Pageable pageable,
			@PathVariable("committeeId") Long committeeId) {

		if (committeeId != null && committeeId > 2) {

			model.addAttribute("page", committeeAndDocumentsService
					.findAllReportsByCommittee(committeeService.findById(committeeId), pageable));

			model.addAttribute("committeeId", committeeId);

		} else {

			model.addAttribute("page",
					committeeAndDocumentsService.findAllReportsByCommittee(committeeService.findById(2l), pageable));

			model.addAttribute("committeeId", 2);
		}

		Notification.getNotifications(model, currentUser, userService, messageRecipientService,
				committeeAndCommitteeMembersService);

		return "reports";
	}

	@RequestMapping(value = { "/document/report/{committeeId},{minuteId}" }, method = RequestMethod.GET)
	public String getCommitteetReport(@CurrentUser User currentUser, Model model,
			@PathVariable("committeeId") Long committeeId, @PathVariable("minuteId") Long minuteId) {

		model.addAttribute("document", committeeAndDocumentsService.findById(minuteId).getDocument());

		Notification.getNotifications(model, currentUser, userService, messageRecipientService,
				committeeAndCommitteeMembersService);

		return "report";
	}
}
