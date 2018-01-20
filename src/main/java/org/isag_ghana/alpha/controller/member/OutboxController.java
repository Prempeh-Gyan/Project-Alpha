package org.isag_ghana.alpha.controller.member;

import javax.validation.constraints.NotNull;

import org.isag_ghana.alpha.model.Message;
import org.isag_ghana.alpha.model.User;
import org.isag_ghana.alpha.service.CommitteeAndCommitteeMembersService;
import org.isag_ghana.alpha.service.GroupService;
import org.isag_ghana.alpha.service.MessageRecipientService;
import org.isag_ghana.alpha.service.MessageService;
import org.isag_ghana.alpha.service.PhotoService;
import org.isag_ghana.alpha.service.UserService;
import org.isag_ghana.alpha.util.CurrentUser;
import org.isag_ghana.alpha.util.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.SortDefault;
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
public class OutboxController {
	private final @NotNull PhotoService photoService;
	private final @NotNull UserService userService;
	private final @NotNull MessageRecipientService messageRecipientService;
	private final @NotNull GroupService groupService;
	private final @NotNull MessageService messageService;
	private final @NotNull CommitteeAndCommitteeMembersService committeeAndCommitteeMembersService;

	@RequestMapping(value = { "/member/outbox" }, method = RequestMethod.GET)
	public String getInbox(@CurrentUser User currentUser, Model model,
			@SortDefault(sort = "date", direction = Direction.DESC) Pageable pageable) {
		model.addAttribute("page", messageService.findAllMessagesByUser(currentUser, pageable));
		Notification.getNotifications(model, currentUser, userService, messageRecipientService,
				committeeAndCommitteeMembersService);
		return "outbox";
	}

	@RequestMapping(value = { "/member/outbox-message/{messageId}" }, method = RequestMethod.GET)
	public String getInboxMessage(@CurrentUser User currentUser, Model model, @PathVariable("messageId") Long messageId,
			@Qualifier("messagePage") @SortDefault(sort = "date", direction = Direction.DESC) Pageable pageable,
			@Qualifier("usersPage") @SortDefault(sort = "profile.firstName", direction = Direction.DESC) Pageable users,
			@Qualifier("groupsPage") @SortDefault(sort = "name", direction = Direction.DESC) Pageable groups) {
		log.info("messageId is : {}", messageId);
		Message message = messageService.findById(messageId);

		if (messageId != null) {
			model.addAttribute("actualMessage", message);
		}

		model.addAttribute("users", userService.findAll(users));
		model.addAttribute("groups", groupService.findAll(groups));
		model.addAttribute("page", messageService.findAllMessagesByUser(currentUser, pageable));
		Notification.getNotifications(model, currentUser, userService, messageRecipientService,
				committeeAndCommitteeMembersService);
		return "outbox-message";
	}
}
