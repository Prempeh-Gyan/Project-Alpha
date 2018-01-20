package org.isag_ghana.alpha.controller.admin;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import org.isag_ghana.alpha.model.Committee;
import org.isag_ghana.alpha.model.CommitteeAndCommitteeMember;
import org.isag_ghana.alpha.model.Gruup;
import org.isag_ghana.alpha.model.User;
import org.isag_ghana.alpha.model.User_Group;
import org.isag_ghana.alpha.service.CommitteeAndCommitteeMembersService;
import org.isag_ghana.alpha.service.GroupService;
import org.isag_ghana.alpha.service.MessageRecipientService;
import org.isag_ghana.alpha.service.RoleService;
import org.isag_ghana.alpha.service.UserGroupService;
import org.isag_ghana.alpha.service.UserService;
import org.isag_ghana.alpha.util.CurrentUser;
import org.isag_ghana.alpha.util.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor(onConstructor = @__({ @Autowired }))
public class ApproveCommitteeRequestsController {

	private final @NotNull MessageRecipientService messageRecipientService;
	private final @NotNull UserService userService;
	private final @NotNull RoleService roleService;
	private final @NotNull UserGroupService userGroupService;
	private final @NotNull GroupService groupService;
	private final @NotNull CommitteeAndCommitteeMembersService committeeAndCommitteeMembersService;

	@RequestMapping(value = { "/admin/approve-committee-requests" }, method = RequestMethod.GET)
	public String getNewMembers(@CurrentUser User currentUser, Model model,
			@SortDefault(sort = "committee", direction = Direction.ASC) Pageable pageable) {
		log.info("New members approval page requested");
		model.addAttribute("page", committeeAndCommitteeMembersService.findAllJoinCommitteeRequests(pageable));
		Notification.getNotifications(model, currentUser, userService, messageRecipientService,
				committeeAndCommitteeMembersService);
		return "approve-committee-requests";
	}

	@RequestMapping(value = { "/admin/process-requests" }, params = { "approve",
			"!decline" }, method = RequestMethod.POST)
	public String approveMember(@CurrentUser User currentUser, Model model,
			@RequestParam("committeeAndCommitteeMemberId") Long committeeAndCommitteeMemberId,
			RedirectAttributes redirectAttributes) {

		CommitteeAndCommitteeMember committeeAndCommitteeMember = committeeAndCommitteeMembersService
				.findById(committeeAndCommitteeMemberId);

		Committee committee = committeeAndCommitteeMember.getCommittee();

		User user = committeeAndCommitteeMember.getCommitteeMember();

		committeeAndCommitteeMember.setMemberSince(LocalDateTime.now());
		committeeAndCommitteeMember.setRequestedToJoin(false);

		User_Group user_Group = new User_Group();
		user_Group.setUser(user);

		if (committee.getIsAdvocacyCommittee()) {

			Gruup newGroupToCheck = groupService.findById(8l);

			if (userGroupService.findByGroupAndUser(newGroupToCheck, user).isEmpty()) {

				user_Group.setGroup(newGroupToCheck);
			}

		} else if (committee.getIsEventsCommittee()) {

			Gruup newGroupToCheck = groupService.findById(9l);

			if (userGroupService.findByGroupAndUser(newGroupToCheck, user).isEmpty()) {

				user_Group.setGroup(newGroupToCheck);
			}

		} else if (committee.getIsMembershipCommittee()) {

			Gruup newGroupToCheck = groupService.findById(10l);

			if (userGroupService.findByGroupAndUser(newGroupToCheck, user).isEmpty()) {

				user_Group.setGroup(newGroupToCheck);
			}

		} else if (committee.getIsAdministrationCommittee()) {

			Gruup newGroupToCheck = groupService.findById(11l);

			if (userGroupService.findByGroupAndUser(newGroupToCheck, user).isEmpty()) {

				user_Group.setGroup(newGroupToCheck);
			}

		} else if (committee.getIsWebsiteCommittee()) {

			Gruup newGroupToCheck = groupService.findById(12l);

			if (userGroupService.findByGroupAndUser(newGroupToCheck, user).isEmpty()) {

				user_Group.setGroup(newGroupToCheck);
			}

		} else if (committee.getIsArchivesCommittee()) {

			Gruup newGroupToCheck = groupService.findById(13l);

			if (userGroupService.findByGroupAndUser(newGroupToCheck, user).isEmpty()) {

				user_Group.setGroup(newGroupToCheck);
			}

		} else if (committee.getIsBaatsoonaLandCommittee()) {

			Gruup newGroupToCheck = groupService.findById(14l);

			if (userGroupService.findByGroupAndUser(newGroupToCheck, user).isEmpty()) {

				user_Group.setGroup(newGroupToCheck);
			}

		}

		committeeAndCommitteeMembersService.save(committeeAndCommitteeMember);

		if (user_Group.getGroup() != null) {

			userGroupService.save(user_Group);
		}

		long roleId = user.getRole() != null ? user.getRole().getId() : 200l;

		if (roleId > 4) {

			user.setRole(roleService.findOne(4l));
		}

		userService.saveUser(user);

		redirectAttributes.addFlashAttribute("success", "You Have Successfully Approved "
				+ user.getProfile().getFirstName() + " " + user.getProfile().getLastName());
		return "redirect:/admin/approve-committee-requests";
	}

	@RequestMapping(value = { "/admin/process-requests" }, params = { "decline",
			"!approve" }, method = RequestMethod.POST)
	public String deleteMember(@CurrentUser User currentUser, Model model,
			@RequestParam("committeeAndCommitteeMemberId") Long committeeAndCommitteeMemberId,
			RedirectAttributes redirectAttributes) {

		CommitteeAndCommitteeMember ccm = committeeAndCommitteeMembersService.findById(committeeAndCommitteeMemberId);

		User user = ccm.getCommitteeMember();

		committeeAndCommitteeMembersService.delete(ccm);

		redirectAttributes.addFlashAttribute("success",
				"You Have Successfully Deleted " + user.getProfile().getFirstName() + " "
						+ user.getProfile().getLastName() + "'s request from the system!");
		return "redirect:/admin/approve-committee-requests";
	}
}