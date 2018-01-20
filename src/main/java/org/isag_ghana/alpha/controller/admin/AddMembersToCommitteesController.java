package org.isag_ghana.alpha.controller.admin;

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
public class AddMembersToCommitteesController {

	private final @NotNull MessageRecipientService messageRecipientService;
	private final @NotNull UserService userService;
	private final @NotNull RoleService roleService;
	private final @NotNull UserGroupService userGroupService;
	private final @NotNull GroupService groupService;
	private final @NotNull CommitteeService committeeService;
	private final @NotNull CommitteeAndCommitteeMembersService committeeAndCommitteeMembersService;

	@RequestMapping(value = { "/admin/assign-committee-to-member" }, method = RequestMethod.GET)
	public String getNewMembers(@CurrentUser User currentUser, Model model,
			@SortDefault(sort = "userCreationDate", direction = Direction.ASC) Pageable pageable) {
		log.info("New members approval page requested");
		model.addAttribute("page", userService.findAllMembers(pageable));
		Notification.getNotifications(model, currentUser, userService, messageRecipientService,
				committeeAndCommitteeMembersService);
		model.addAttribute("committees", committeeService.findAll());
		return "assign-committee-to-member";
	}

	@RequestMapping(value = { "/admin/add-member-to-committee" }, method = RequestMethod.POST)
	public String approveMember(@CurrentUser User currentUser, Model model, @RequestParam("userId") Long userId,
			@RequestParam("committeeId") Long committeeId, RedirectAttributes redirectAttributes) {

		User user = userService.findOne(userId);

		if (committeeId == -1) {
			redirectAttributes.addFlashAttribute("error", "Error! You did not select any committee for "
					+ user.getProfile().getFirstName() + " " + user.getProfile().getLastName());
			return "redirect:/admin/assign-committee-to-member";
		}

		Committee committee = committeeService.findById(committeeId);

		List<CommitteeAndCommitteeMember> committeeAndCommitteeMembers = committeeAndCommitteeMembersService
				.findCommitteeByUserAndCommittee(user, committee);

		if (!committeeAndCommitteeMembers.isEmpty()) {

			redirectAttributes.addFlashAttribute("error", "Error! " + user.getProfile().getFirstName() + " "
					+ user.getProfile().getLastName() + " is already a member of the " + committee.getName());
			return "redirect:/admin/assign-committee-to-member";
		}

		CommitteeAndCommitteeMember ccm = new CommitteeAndCommitteeMember();
		ccm.setCommittee(committee);
		ccm.setCommitteeMember(user);

		User_Group user_Group = new User_Group();
		user_Group.setUser(user);

		if (committee.getName().contains("ADVOCACY")) {

			ccm.setIsAdvocacyCommittee(true);

			Gruup newGroupToCheck = groupService.findById(8l);

			if (userGroupService.findByGroupAndUser(newGroupToCheck, user).isEmpty()) {

				user_Group.setGroup(newGroupToCheck);
			}

		} else if (committee.getName().contains("EVENTS")) {

			ccm.setIsEventsCommittee(true);

			Gruup newGroupToCheck = groupService.findById(9l);

			if (userGroupService.findByGroupAndUser(newGroupToCheck, user).isEmpty()) {

				user_Group.setGroup(newGroupToCheck);
			}

		} else if (committee.getName().contains("MEMBERSHIP")) {

			ccm.setIsMembershipCommittee(true);

			Gruup newGroupToCheck = groupService.findById(10l);

			if (userGroupService.findByGroupAndUser(newGroupToCheck, user).isEmpty()) {

				user_Group.setGroup(newGroupToCheck);
			}

		} else if (committee.getName().contains("ADMINISTRATION")) {

			ccm.setIsAdministrationCommittee(true);
			Gruup newGroupToCheck = groupService.findById(11l);

			if (userGroupService.findByGroupAndUser(newGroupToCheck, user).isEmpty()) {

				user_Group.setGroup(newGroupToCheck);
			}

		} else if (committee.getName().contains("WEBSITE")) {

			ccm.setIsWebsiteCommittee(true);

			Gruup newGroupToCheck = groupService.findById(12l);

			if (userGroupService.findByGroupAndUser(newGroupToCheck, user).isEmpty()) {

				user_Group.setGroup(newGroupToCheck);
			}

		} else if (committee.getName().contains("ARCHIVES")) {

			ccm.setIsArchivesCommittee(true);

			Gruup newGroupToCheck = groupService.findById(13l);

			if (userGroupService.findByGroupAndUser(newGroupToCheck, user).isEmpty()) {

				user_Group.setGroup(newGroupToCheck);
			}

		} else if (committee.getName().contains("BAATSOONA")) {

			ccm.setIsBaatsoonaLandCommittee(true);

			Gruup newGroupToCheck = groupService.findById(14l);

			if (userGroupService.findByGroupAndUser(newGroupToCheck, user).isEmpty()) {

				user_Group.setGroup(newGroupToCheck);
			}

		}

		committeeAndCommitteeMembersService.save(ccm);

		if (user_Group.getGroup() != null) {

			userGroupService.save(user_Group);
		}

		long roleId = user.getRole() != null ? user.getRole().getId() : 200l;

		if (roleId > 4) {

			user.setRole(roleService.findOne(4l));
		}

		userService.saveUser(user);

		redirectAttributes.addFlashAttribute("success",
				"You Have Successfully Added " + user.getProfile().getFirstName() + " "
						+ user.getProfile().getLastName() + " to the " + committee.getName());
		return "redirect:/admin/assign-committee-to-member";
	}

}