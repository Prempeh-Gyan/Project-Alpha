package org.isag_ghana.alpha.util;

import org.isag_ghana.alpha.model.User;
import org.isag_ghana.alpha.service.CommitteeAndCommitteeMembersService;
import org.isag_ghana.alpha.service.MessageRecipientService;
import org.isag_ghana.alpha.service.UserService;
import org.springframework.ui.Model;

public class Notification {

	public static Model getNotifications(Model model, User currentUser, UserService userService,
			MessageRecipientService messageRecipientService,
			CommitteeAndCommitteeMembersService committeeAndCommitteeMembersService) {

		int numberOfNewMembers = 0;
		int numberOfJoinCommitteeRequests = 0;
		int numberOfLeaveCommitteeRequests = 0;

		model.addAttribute("numberOfNewMembers", userService.findAllNewMembers().isEmpty() ? 0
				: (numberOfNewMembers = userService.findAllNewMembers().size()));
		model.addAttribute("numberOfJoinCommitteeRequests",
				committeeAndCommitteeMembersService.findAllJoinCommitteeRequests().isEmpty() ? 0
						: (numberOfJoinCommitteeRequests = committeeAndCommitteeMembersService
								.findAllJoinCommitteeRequests().size()));
		model.addAttribute("numberOfLeaveCommitteeRequests",
				committeeAndCommitteeMembersService.findAllLeaveCommitteeRequests().isEmpty() ? 0
						: (numberOfLeaveCommitteeRequests = committeeAndCommitteeMembersService
								.findAllLeaveCommitteeRequests().size()));
		model.addAttribute("totalNumberOfNotifications",
				numberOfNewMembers + numberOfJoinCommitteeRequests + numberOfLeaveCommitteeRequests);
		model.addAttribute("currentUsersMessageRecipients",
				messageRecipientService.findAllUnreadMessagesByUser(currentUser));
		model.addAttribute("currentUser", currentUser);

		return model;
	}

}
