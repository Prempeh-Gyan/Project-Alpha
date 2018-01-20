package org.isag_ghana.alpha.controller.admin;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.isag_ghana.alpha.model.Committee;
import org.isag_ghana.alpha.model.CommitteeAndCommitteeMember;
import org.isag_ghana.alpha.model.Gruup;
import org.isag_ghana.alpha.model.Position;
import org.isag_ghana.alpha.model.PositionAndPositionHolder;
import org.isag_ghana.alpha.model.User;
import org.isag_ghana.alpha.model.User_Group;
import org.isag_ghana.alpha.service.CommitteeAndCommitteeMembersService;
import org.isag_ghana.alpha.service.CommitteeService;
import org.isag_ghana.alpha.service.GroupService;
import org.isag_ghana.alpha.service.MessageRecipientService;
import org.isag_ghana.alpha.service.PositionAndPositionHoldersService;
import org.isag_ghana.alpha.service.PositionService;
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
public class AssignMembershipRolesController {

	private final @NotNull MessageRecipientService messageRecipientService;
	private final @NotNull UserService userService;
	private final @NotNull RoleService roleService;
	private final @NotNull GroupService groupService;
	private final @NotNull CommitteeService committeeService;
	private final @NotNull PositionService positionService;
	private final @NotNull UserGroupService userGroupService;
	private final @NotNull PositionAndPositionHoldersService positionAndPositionHoldersService;
	private final @NotNull CommitteeAndCommitteeMembersService committeeAndCommitteeMembersService;

	@RequestMapping(value = { "/admin/assign-membership-roles" }, method = RequestMethod.GET)
	public String getAllMembers(@CurrentUser User currentUser, Model model,
			@SortDefault(sort = "userCreationDate", direction = Direction.ASC) Pageable pageable) {
		log.info("New members approval page requested");
		model.addAttribute("page", userService.findAllMembers(pageable));
		model.addAttribute("positions", positionService.findAllPositions());
		Notification.getNotifications(model, currentUser, userService, messageRecipientService,
				committeeAndCommitteeMembersService);
		return "assign-membership-roles";
	}

	@RequestMapping(value = { "/admin/assign" }, params = "assign", method = RequestMethod.POST)
	public String assignMembershipRole(@CurrentUser User currentUser, Model model, @RequestParam("userId") Long userId,
			@RequestParam("positionId") Long positionId,
			@SortDefault(sort = "userCreationDate", direction = Direction.ASC) Pageable pageable,
			RedirectAttributes redirectAttributes) {

		User user = userService.findOne(userId);

		if (positionId == -1) {

			redirectAttributes.addFlashAttribute("error",
					"Error! You did not select any Position / Role to be assigned to "
							+ user.getProfile().getFirstName() + " " + user.getProfile().getLastName());
			return "redirect:/admin/assign-membership-roles";
		}

		// Position to be assigned
		Position newPosition = positionService.findById(positionId);

		// List of users currently holding that position
		List<PositionAndPositionHolder> pPH = positionAndPositionHoldersService
				.findCurrentPositionHolderByPosition(newPosition);

		// Position is occupied by others
		if (!pPH.isEmpty()) {

			redirectAttributes.addFlashAttribute("error",
					"Error! The position you are trying to assign to " + user.getProfile().getFirstName() + " "
							+ user.getProfile().getLastName() + " is already occupied by "
							+ pPH.get(0).getPositionHolder().getProfile().getFirstName() + " "
							+ pPH.get(0).getPositionHolder().getProfile().getLastName()
							+ ". First end the tenure for this position before assigning it to someone else.");
			return "redirect:/admin/assign-membership-roles";
		}

		// Set user as the position holder
		PositionAndPositionHolder positionHolder = new PositionAndPositionHolder();
		positionHolder.setPosition(newPosition);
		positionHolder.setPositionHolder(user);
		positionHolder.setIsExecutivePosition(newPosition.getIsExecutivePosition());
		positionHolder.setIsHeadPosition(newPosition.getIsHeadPosition());
		positionHolder.setIsCommitteePosition(newPosition.getIsCommitteePosition());
		positionAndPositionHoldersService.save(positionHolder);

		// If user has no role, a sentinel/dummy value of 200 is used. This
		// value is out of the range of role ids in the database
		long usersCurrentRoleId = (user.getRole() == null ? 200l : user.getRole().getId());

		log.info("user's current role id is: {}", usersCurrentRoleId);

		// executive positions have roleId 2 while committee positions have
		// roleId 3.
		long newRoleId = (newPosition.getIsExecutivePosition() ? 2l : 3l);

		log.info("user's new role id is: {}", newRoleId);

		// if usersCurrentRoleId == newRoleId then there is no need to update
		// the role id. For the purposes of user having multiple positions, we
		// only assign the role with the highest privileges
		if (usersCurrentRoleId > newRoleId) {

			log.info("resetting user's current role from : {}  to  : {}", usersCurrentRoleId, newRoleId);

			user.setRole(roleService.findOne(newRoleId));

		}

		if (newPosition.getIsCommitteePosition()) {

			Gruup newGroup = groupService.findById(3l);

			// Check if user is already part of the committee Leaders group
			List<User_Group> userAndGroups = userGroupService.findByGroupAndUser(newGroup, user);

			// user is not in the group so we add him to the group, there will
			// be no
			// need to put him in the group if he was already a part of it
			if (userAndGroups.isEmpty()) {
				User_Group user_Group = new User_Group();
				user_Group.setGroup(newGroup);
				user_Group.setUser(user);
				userGroupService.save(user_Group);
			}
		}

		String positionName = newPosition.getName();

		// Add the user now to the committee of which he is holding the position
		CommitteeAndCommitteeMember newCommitteeAndCommitteeMember = new CommitteeAndCommitteeMember();
		newCommitteeAndCommitteeMember.setCommitteeMember(user);

		User_Group user_Group = new User_Group();
		user_Group.setUser(user);

		if (!positionName.contains("COMMITTEE")) {

			Committee committeeToCheck = committeeService.findById(1l);

			if (committeeAndCommitteeMembersService.findCommitteeByUserAndCommittee(user, committeeToCheck).isEmpty()) {
				newCommitteeAndCommitteeMember.setCommittee(committeeToCheck);
				newCommitteeAndCommitteeMember.setIsExecutiveCommittee(true);
				newCommitteeAndCommitteeMember.setIsMemberRemovable(false);
			}

			Gruup newGroupToCheck = groupService.findById(2l);

			if (userGroupService.findByGroupAndUser(newGroupToCheck, user).isEmpty()) {

				user_Group.setGroup(newGroupToCheck);
			}

		} else if (positionName.contains("ADVOCACY")) {

			Committee committeeToCheck = committeeService.findById(2l);

			if (committeeAndCommitteeMembersService.findCommitteeByUserAndCommittee(user, committeeToCheck).isEmpty()) {
				newCommitteeAndCommitteeMember.setCommittee(committeeToCheck);
				newCommitteeAndCommitteeMember.setIsAdvocacyCommittee(true);
				newCommitteeAndCommitteeMember.setIsMemberRemovable(false);
			}

			Gruup newGroupToCheck = groupService.findById(8l);

			if (userGroupService.findByGroupAndUser(newGroupToCheck, user).isEmpty()) {

				user_Group.setGroup(newGroupToCheck);
			}

		} else if (positionName.contains("EVENTS")) {

			Committee committeeToCheck = committeeService.findById(3l);

			if (committeeAndCommitteeMembersService.findCommitteeByUserAndCommittee(user, committeeToCheck).isEmpty()) {
				newCommitteeAndCommitteeMember.setCommittee(committeeToCheck);
				newCommitteeAndCommitteeMember.setIsEventsCommittee(true);
				newCommitteeAndCommitteeMember.setIsMemberRemovable(false);
			}

			Gruup newGroupToCheck = groupService.findById(9l);

			if (userGroupService.findByGroupAndUser(newGroupToCheck, user).isEmpty()) {

				user_Group.setGroup(newGroupToCheck);
			}

		} else if (positionName.contains("MEMBERSHIP")) {

			Committee committeeToCheck = committeeService.findById(4l);

			if (committeeAndCommitteeMembersService.findCommitteeByUserAndCommittee(user, committeeToCheck).isEmpty()) {
				newCommitteeAndCommitteeMember.setCommittee(committeeToCheck);
				newCommitteeAndCommitteeMember.setIsMembershipCommittee(true);
				newCommitteeAndCommitteeMember.setIsMemberRemovable(false);
			}

			Gruup newGroupToCheck = groupService.findById(10l);

			if (userGroupService.findByGroupAndUser(newGroupToCheck, user).isEmpty()) {

				user_Group.setGroup(newGroupToCheck);
			}

		} else if (positionName.contains("ADMINISTRATOR")) {

			Committee committeeToCheck = committeeService.findById(5l);

			if (committeeAndCommitteeMembersService.findCommitteeByUserAndCommittee(user, committeeToCheck).isEmpty()) {
				newCommitteeAndCommitteeMember.setCommittee(committeeToCheck);
				newCommitteeAndCommitteeMember.setIsAdministrationCommittee(true);
				newCommitteeAndCommitteeMember.setIsMemberRemovable(false);
			}

			Gruup newGroupToCheck = groupService.findById(11l);

			if (userGroupService.findByGroupAndUser(newGroupToCheck, user).isEmpty()) {

				user_Group.setGroup(newGroupToCheck);
			}

		} else if (positionName.contains("WEBSITE")) {

			Committee committeeToCheck = committeeService.findById(6l);

			if (committeeAndCommitteeMembersService.findCommitteeByUserAndCommittee(user, committeeToCheck).isEmpty()) {
				newCommitteeAndCommitteeMember.setCommittee(committeeToCheck);
				newCommitteeAndCommitteeMember.setIsWebsiteCommittee(true);
				newCommitteeAndCommitteeMember.setIsMemberRemovable(false);
			}

			Gruup newGroupToCheck = groupService.findById(12l);

			if (userGroupService.findByGroupAndUser(newGroupToCheck, user).isEmpty()) {

				user_Group.setGroup(newGroupToCheck);
			}

		} else if (positionName.contains("ARCHIVES")) {

			Committee committeeToCheck = committeeService.findById(7l);

			if (committeeAndCommitteeMembersService.findCommitteeByUserAndCommittee(user, committeeToCheck).isEmpty()) {
				newCommitteeAndCommitteeMember.setCommittee(committeeToCheck);
				newCommitteeAndCommitteeMember.setIsArchivesCommittee(true);
				newCommitteeAndCommitteeMember.setIsMemberRemovable(false);
			}

			Gruup newGroupToCheck = groupService.findById(13l);

			if (userGroupService.findByGroupAndUser(newGroupToCheck, user).isEmpty()) {

				user_Group.setGroup(newGroupToCheck);
			}

		} else if (positionName.contains("BAATSOONA")) {

			Committee committeeToCheck = committeeService.findById(8l);

			if (committeeAndCommitteeMembersService.findCommitteeByUserAndCommittee(user, committeeToCheck).isEmpty()) {
				newCommitteeAndCommitteeMember.setCommittee(committeeToCheck);
				newCommitteeAndCommitteeMember.setIsBaatsoonaLandCommittee(true);
				newCommitteeAndCommitteeMember.setIsMemberRemovable(false);
			}

			Gruup newGroupToCheck = groupService.findById(14l);

			if (userGroupService.findByGroupAndUser(newGroupToCheck, user).isEmpty()) {

				user_Group.setGroup(newGroupToCheck);
			}

		}

		if (newCommitteeAndCommitteeMember.getCommittee() != null) {

			committeeAndCommitteeMembersService.save(newCommitteeAndCommitteeMember);

		}

		if (user_Group.getGroup() != null) {

			userGroupService.save(user_Group);
		}

		userService.saveUser(user);

		redirectAttributes.addFlashAttribute("success", "You Have Successfully Assigned Role(s) to "
				+ user.getProfile().getFirstName() + " " + user.getProfile().getLastName());
		return "redirect:/admin/assign-membership-roles";

	}
}