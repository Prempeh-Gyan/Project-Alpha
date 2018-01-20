package org.isag_ghana.alpha.controller.admin;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.isag_ghana.alpha.model.Committee;
import org.isag_ghana.alpha.model.CommitteeAndCommitteeMember;
import org.isag_ghana.alpha.model.Gruup;
import org.isag_ghana.alpha.model.PositionAndPositionHolder;
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
public class TerminateMembershipRolesController {

	private final @NotNull MessageRecipientService messageRecipientService;
	private final @NotNull UserService userService;
	private final @NotNull RoleService roleService;
	private final @NotNull GroupService groupService;
	private final @NotNull UserGroupService userGroupService;
	private final @NotNull CommitteeService committeeService;
	private final @NotNull PositionAndPositionHoldersService positionAndPositionHoldersService;
	private final @NotNull CommitteeAndCommitteeMembersService committeeAndCommitteeMembersService;

	@RequestMapping(value = { "/admin/terminate-membership-roles" }, method = RequestMethod.GET)
	public String getAllMembers(@CurrentUser User currentUser, Model model, @SortDefault(sort = { "isExecutivePosition",
			"isHeadPosition", "isCommitteePosition" }, direction = Direction.DESC) Pageable pageable) {
		log.info("Terminate members role page requested");
		model.addAttribute("positionAndPositionHolders",
				positionAndPositionHoldersService.findAllCurrentPositions(pageable));
		Notification.getNotifications(model, currentUser, userService, messageRecipientService,
				committeeAndCommitteeMembersService);
		return "terminate-membership-roles";
	}

	@RequestMapping(value = { "/admin/terminate" }, params = "terminate", method = RequestMethod.POST)
	public String assignMembershipRole(@CurrentUser User currentUser, Model model,
			@RequestParam("positionAndPositionHolderId") Long positionAndPositionHolderId,
			RedirectAttributes redirectAttributes) {

		PositionAndPositionHolder positionAndPositionHolder = positionAndPositionHoldersService
				.findById(positionAndPositionHolderId);

		positionAndPositionHolder.setIsTenureExpired(true);
		positionAndPositionHolder.setEndDate(LocalDateTime.now());

		positionAndPositionHoldersService.save(positionAndPositionHolder);

		User user = positionAndPositionHolder.getPositionHolder();

		String positionName = positionAndPositionHolder.getPosition().getName();

		if (positionName.contains("ADVOCACY")) {

			Committee committeeToCheck = committeeService.findById(2l);

			List<CommitteeAndCommitteeMember> ccm = committeeAndCommitteeMembersService
					.findCommitteeByUserAndCommittee(user, committeeToCheck);

			if (!ccm.isEmpty()) {
				ccm.get(0).setIsMemberRemovable(true);
				committeeAndCommitteeMembersService.save(ccm);
			}

		} else if (positionName.contains("EVENTS")) {

			Committee committeeToCheck = committeeService.findById(3l);

			List<CommitteeAndCommitteeMember> ccm = committeeAndCommitteeMembersService
					.findCommitteeByUserAndCommittee(user, committeeToCheck);

			if (!ccm.isEmpty()) {
				ccm.get(0).setIsMemberRemovable(true);
				committeeAndCommitteeMembersService.save(ccm);
			}

		} else if (positionName.contains("MEMBERSHIP")) {

			Committee committeeToCheck = committeeService.findById(4l);

			List<CommitteeAndCommitteeMember> ccm = committeeAndCommitteeMembersService
					.findCommitteeByUserAndCommittee(user, committeeToCheck);

			if (!ccm.isEmpty()) {
				ccm.get(0).setIsMemberRemovable(true);
				committeeAndCommitteeMembersService.save(ccm);
			}

		} else if (positionName.contains("ADMINISTRATOR")) {

			Committee committeeToCheck = committeeService.findById(5l);

			List<CommitteeAndCommitteeMember> ccm = committeeAndCommitteeMembersService
					.findCommitteeByUserAndCommittee(user, committeeToCheck);

			if (!ccm.isEmpty()) {
				ccm.get(0).setIsMemberRemovable(true);
			}

		} else if (positionName.contains("WEBSITE")) {

			Committee committeeToCheck = committeeService.findById(6l);

			List<CommitteeAndCommitteeMember> ccm = committeeAndCommitteeMembersService
					.findCommitteeByUserAndCommittee(user, committeeToCheck);

			if (!ccm.isEmpty()) {
				ccm.get(0).setIsMemberRemovable(true);
				committeeAndCommitteeMembersService.save(ccm);
			}

		} else if (positionName.contains("ARCHIVES")) {

			Committee committeeToCheck = committeeService.findById(7l);

			List<CommitteeAndCommitteeMember> ccm = committeeAndCommitteeMembersService
					.findCommitteeByUserAndCommittee(user, committeeToCheck);

			if (!ccm.isEmpty()) {
				ccm.get(0).setIsMemberRemovable(true);
				committeeAndCommitteeMembersService.save(ccm);
			}

		} else if (positionName.contains("BAATSOONA")) {

			Committee committeeToCheck = committeeService.findById(8l);

			List<CommitteeAndCommitteeMember> ccm = committeeAndCommitteeMembersService
					.findCommitteeByUserAndCommittee(user, committeeToCheck);

			if (!ccm.isEmpty()) {
				ccm.get(0).setIsMemberRemovable(true);
				committeeAndCommitteeMembersService.save(ccm);
			}

		}

		List<PositionAndPositionHolder> remainingPositionsHeldByUser = positionAndPositionHoldersService
				.findAllCurrentPositionsByUser(user);

		if (!remainingPositionsHeldByUser.isEmpty()) {

			List<PositionAndPositionHolder> usersRemainingExecutivePositions = remainingPositionsHeldByUser
					.parallelStream().filter(posHolder -> posHolder.getPosition().getIsExecutivePosition() == true)
					.collect(Collectors.toList());

			List<PositionAndPositionHolder> usersRemainingCommitteePositions = remainingPositionsHeldByUser
					.parallelStream().filter(posHolder -> posHolder.getPosition().getIsCommitteePosition() == true)
					.collect(Collectors.toList());

			if (usersRemainingExecutivePositions.isEmpty()) {

				user.setRole(roleService.findOne(4l));

				Gruup groupToRemove = groupService.findById(2l);

				// Check if user is part of the executive committee group
				List<User_Group> userAndGroups = userGroupService.findByGroupAndUser(groupToRemove, user);

				// user is in the executive committee group so we delete him
				// from that group
				if (!userAndGroups.isEmpty()) {
					userGroupService.delete(userAndGroups.get(0));
				}

				// remove user from executive committee
				List<CommitteeAndCommitteeMember> ccm = committeeAndCommitteeMembersService
						.findCommitteeByUserAndCommittee(user, committeeService.findById(1l));

				if (!ccm.isEmpty()) {

					CommitteeAndCommitteeMember endCCM = ccm.get(0);
					endCCM.setEndDate(LocalDateTime.now());
					endCCM.setIsStillAMember(false);
					endCCM.setRemovedByAdmin(true);
					endCCM.setIsMemberRemovable(true);
					committeeAndCommitteeMembersService.save(endCCM);
				}

			}

			if (usersRemainingCommitteePositions.isEmpty()) {

				user.setRole(roleService.findOne(4l));

				Gruup groupToRemove = groupService.findById(3l);

				// Check if user is part of the committee Leaders group
				List<User_Group> userAndGroups = userGroupService.findByGroupAndUser(groupToRemove, user);

				// user is in the committee leaders group so we delete him from
				// that group
				if (!userAndGroups.isEmpty()) {
					userGroupService.delete(userAndGroups.get(0));
				}

			}

			if (!usersRemainingCommitteePositions.isEmpty()) {

				user.setRole(roleService.findOne(3l));

			}

			if (!usersRemainingExecutivePositions.isEmpty()) {

				user.setRole(roleService.findOne(2l));
			}

		} else {

			user.setRole(roleService.findOne(4l));

			Gruup groupToRemove = groupService.findById(3l);

			// Check if user is part of the committee Leaders group
			List<User_Group> userAndGroups = userGroupService.findByGroupAndUser(groupToRemove, user);

			// user is in the committee leaders group so we delete him from that
			// group
			if (!userAndGroups.isEmpty()) {
				userGroupService.delete(userAndGroups.get(0));
			}

			groupToRemove = groupService.findById(2l);

			// Check if user is part of the executive committee group
			userAndGroups = userGroupService.findByGroupAndUser(groupToRemove, user);

			// user is in the executive committee group so we delete him from
			// that group
			if (!userAndGroups.isEmpty()) {
				userGroupService.delete(userAndGroups.get(0));
			}

			// remove user from executive committee
			List<CommitteeAndCommitteeMember> cccm = committeeAndCommitteeMembersService
					.findCommitteeByUserAndCommittee(user, committeeService.findById(1l));

			if (!cccm.isEmpty()) {

				CommitteeAndCommitteeMember endCCM = cccm.get(0);
				endCCM.setEndDate(LocalDateTime.now());
				endCCM.setIsStillAMember(false);
				endCCM.setIsMemberRemovable(true);
				committeeAndCommitteeMembersService.save(endCCM);
			}

		}

		userService.saveUser(user);

		redirectAttributes.addFlashAttribute("success",
				"Success! You have successfully Terminated " + user.getProfile().getFirstName() + " "
						+ user.getProfile().getLastName() + "'s Position/Role as "
						+ positionAndPositionHolder.getPosition().getName());
		return "redirect:/admin/terminate-membership-roles";

	}
}