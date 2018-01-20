package org.isag_ghana.alpha.controller.admin;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.isag_ghana.alpha.model.Committee;
import org.isag_ghana.alpha.model.CommitteeAndCommitteeMember;
import org.isag_ghana.alpha.model.Gruup;
import org.isag_ghana.alpha.model.User;
import org.isag_ghana.alpha.model.User_Group;
import org.isag_ghana.alpha.service.CommitteeAndCommitteeMembersService;
import org.isag_ghana.alpha.service.CommitteeService;
import org.isag_ghana.alpha.service.GroupService;
import org.isag_ghana.alpha.service.MessageRecipientService;
import org.isag_ghana.alpha.service.PositionAndPositionHoldersService;
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
public class TerminateCommitteeMembershipController {

	private final @NotNull MessageRecipientService messageRecipientService;
	private final @NotNull UserService userService;
	private final @NotNull RoleService roleService;
	private final @NotNull GroupService groupService;
	private final @NotNull UserGroupService userGroupService;
	private final @NotNull CommitteeService committeeService;
	private final @NotNull CommitteeAndCommitteeMembersService committeeAndCommitteeMembersService;
	private final @NotNull PositionAndPositionHoldersService positionAndPositionHoldersService;

	@RequestMapping(value = { "/admin/terminate-committee-membership" }, method = RequestMethod.GET)
	public String getAllMembers(@CurrentUser User currentUser, Model model,
			@SortDefault(sort = { "committee", "memberSince" }, direction = Direction.DESC) Pageable pageable) {
		log.info("Terminate committee membership page requested");
		model.addAttribute("committeeAndCommitteeMembers",
				committeeAndCommitteeMembersService.findAllCurrentCommitteesAndMembers(pageable));
		Notification.getNotifications(model, currentUser, userService, messageRecipientService,
				committeeAndCommitteeMembersService);
		return "terminate-committee-membership";
	}

	@RequestMapping(value = { "/admin/terminate-committee-member" }, params = "terminate", method = RequestMethod.POST)
	public String assignMembershipRole(@CurrentUser User currentUser, Model model,
			@RequestParam("committeeAndCommitteeMemberId") Long committeeAndCommitteeMemberId,
			RedirectAttributes redirectAttributes) {

		CommitteeAndCommitteeMember committeeAndCommitteeMember = committeeAndCommitteeMembersService
				.findById(committeeAndCommitteeMemberId);

		User user = committeeAndCommitteeMember.getCommitteeMember();

		Committee committee = committeeAndCommitteeMember.getCommittee();

		if (!committeeAndCommitteeMember.getIsMemberRemovable()) {

			redirectAttributes.addFlashAttribute("error",
					"Error! " + user.getProfile().getFirstName() + " " + user.getProfile().getLastName()
							+ " is a committee leader, terminate this role before removing from committee");
			return "redirect:/admin/terminate-committee-membership";
		}

		committeeAndCommitteeMember.setIsStillAMember(false);
		committeeAndCommitteeMember.setEndDate(LocalDateTime.now());
		committeeAndCommitteeMember.setRemovedByAdmin(true);
		committeeAndCommitteeMembersService.save(committeeAndCommitteeMember);

		if (committee.getIsAdvocacyCommittee()) {

			Gruup newGroupToCheck = groupService.findById(8l);

			List<User_Group> userGroups = userGroupService.findByGroupAndUser(newGroupToCheck, user);

			if (!userGroups.isEmpty()) {

				userGroupService.delete(userGroups.get(0));
			}

		} else if (committee.getIsEventsCommittee()) {

			Gruup newGroupToCheck = groupService.findById(9l);

			List<User_Group> userGroups = userGroupService.findByGroupAndUser(newGroupToCheck, user);

			if (!userGroups.isEmpty()) {

				userGroupService.delete(userGroups.get(0));
			}

		} else if (committee.getIsMembershipCommittee()) {

			Gruup newGroupToCheck = groupService.findById(10l);

			List<User_Group> userGroups = userGroupService.findByGroupAndUser(newGroupToCheck, user);

			if (!userGroups.isEmpty()) {

				userGroupService.delete(userGroups.get(0));
			}

		} else if (committee.getIsAdministrationCommittee()) {

			Gruup newGroupToCheck = groupService.findById(11l);

			List<User_Group> userGroups = userGroupService.findByGroupAndUser(newGroupToCheck, user);

			if (!userGroups.isEmpty()) {

				userGroupService.delete(userGroups.get(0));
			}

		} else if (committee.getIsWebsiteCommittee()) {

			Gruup newGroupToCheck = groupService.findById(12l);

			List<User_Group> userGroups = userGroupService.findByGroupAndUser(newGroupToCheck, user);

			if (!userGroups.isEmpty()) {

				userGroupService.delete(userGroups.get(0));
			}

		} else if (committee.getIsArchivesCommittee()) {

			Gruup newGroupToCheck = groupService.findById(13l);

			List<User_Group> userGroups = userGroupService.findByGroupAndUser(newGroupToCheck, user);

			if (!userGroups.isEmpty()) {

				userGroupService.delete(userGroups.get(0));
			}

		} else if (committee.getIsBaatsoonaLandCommittee()) {

			Gruup newGroupToCheck = groupService.findById(14l);

			List<User_Group> userGroups = userGroupService.findByGroupAndUser(newGroupToCheck, user);

			if (!userGroups.isEmpty()) {

				userGroupService.delete(userGroups.get(0));
			}

		}

		List<CommitteeAndCommitteeMember> ccm = committeeAndCommitteeMembersService
				.findAllCurrentCommitteesByUser(user);

		if (ccm.isEmpty()) {
			// set role to isag member
			user.setRole(roleService.findOne(5l));
			userService.saveUser(user);
		}

		redirectAttributes.addFlashAttribute("success",
				"Success! You have successfully Removed " + user.getProfile().getFirstName() + " "
						+ user.getProfile().getLastName() + " from the " + committee.getName());
		return "redirect:/admin/terminate-committee-membership";

	}
}