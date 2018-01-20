package org.isag_ghana.alpha.controller.admin;

import javax.validation.constraints.NotNull;

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
public class ApproveNewMembersController {

	private final @NotNull MessageRecipientService messageRecipientService;
	private final @NotNull UserService userService;
	private final @NotNull RoleService roleService;
	private final @NotNull UserGroupService userGroupService;
	private final @NotNull GroupService groupService;
	private final @NotNull CommitteeAndCommitteeMembersService committeeAndCommitteeMembersService;

	@RequestMapping(value = { "/admin/approve-new-members" }, method = RequestMethod.GET)
	public String getNewMembers(@CurrentUser User currentUser, Model model,
			@SortDefault(sort = "userCreationDate", direction = Direction.ASC) Pageable pageable) {
		log.info("New members approval page requested");
		model.addAttribute("page", userService.findAllNewMembers(pageable));
		model.addAttribute("roles", roleService.findAll());
		Notification.getNotifications(model, currentUser, userService, messageRecipientService,
				committeeAndCommitteeMembersService);
		return "approve-new-members";
	}

	@RequestMapping(value = { "/admin/process-new-members" }, params = { "approve",
			"!decline" }, method = RequestMethod.POST)
	public String approveMember(@CurrentUser User currentUser, Model model, @RequestParam("userId") Long userId,
			@RequestParam("roleId") Long roleId,
			@SortDefault(sort = "userCreationDate", direction = Direction.ASC) Pageable pageable,
			RedirectAttributes redirectAttributes) {

		User user = userService.findOne(userId);

		if (roleId == -1) {
			redirectAttributes.addFlashAttribute("error", "Error! You did not select any privilege level for "
					+ user.getProfile().getFirstName() + " " + user.getProfile().getLastName());
			return "redirect:/admin/approve-new-members";
		}

		log.info("Approving: {}", user.getProfile().getFirstName());
		user.setIsAccountNonExpired(true);
		user.setIsAccountNonLocked(true);
		user.setIsCredentialsNonExpired(true);
		user.setIsEnabled(true);
		user.setRole(roleService.findOne(roleId));
		userService.saveUser(user);

		long groupId = roleId;

		User_Group userGroup = new User_Group();
		userGroup.setGroup(groupService.findById(groupId));
		userGroup.setUser(user);
		userGroupService.save(userGroup);

		redirectAttributes.addFlashAttribute("success", "You Have Successfully Approved "
				+ user.getProfile().getFirstName() + " " + user.getProfile().getLastName());
		return "redirect:/admin/approve-new-members";
	}

	@RequestMapping(value = { "/admin/process-new-members" }, params = { "decline",
			"!approve" }, method = RequestMethod.POST)
	public String deleteMember(@CurrentUser User currentUser, Model model, @RequestParam("userId") Long userId,
			@SortDefault(sort = "userCreationDate", direction = Direction.ASC) Pageable pageable,
			RedirectAttributes redirectAttributes) {

		User user = userService.findOne(userId);

		userService.delete(user);

		redirectAttributes.addFlashAttribute("success",
				"You Have Successfully Deleted " + user.getProfile().getFirstName() + " "
						+ user.getProfile().getLastName() + "'s request from the system!");
		return "redirect:/admin/approve-new-members";
	}
}