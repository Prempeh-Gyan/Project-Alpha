package org.isag_ghana.alpha.controller.admin;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.isag_ghana.alpha.dto.CreatePositionsFormDTO;
import org.isag_ghana.alpha.dto.EditPositionsFormDTO;
import org.isag_ghana.alpha.model.Position;
import org.isag_ghana.alpha.model.User;
import org.isag_ghana.alpha.service.CommitteeAndCommitteeMembersService;
import org.isag_ghana.alpha.service.MessageRecipientService;
import org.isag_ghana.alpha.service.PositionAndPositionHoldersService;
import org.isag_ghana.alpha.service.PositionService;
import org.isag_ghana.alpha.service.UserService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor(onConstructor = @__({ @Autowired }))
public class AddEditPositionController {

	private final @NotNull PositionService positionService;
	private final @NotNull UserService userService;
	private final @NotNull MessageRecipientService messageRecipientService;
	private final @NotNull PositionAndPositionHoldersService positionAndPositionHoldersService;
	private final @NotNull CommitteeAndCommitteeMembersService committeeAndCommitteeMembersService;

	@RequestMapping(value = { "/admin/add-edit-position" }, method = RequestMethod.GET)
	public String getCreateNewPosition(@CurrentUser User currentUser, Model model) {
		log.info("rendering create-edit-position...");
		model.addAttribute("createEditPositionsFormDTO", new CreatePositionsFormDTO());
		model.addAttribute("editPositionsFormDTO", new EditPositionsFormDTO());
		model.addAttribute("currentPositions", getCurrentPositions());
		Notification.getNotifications(model, currentUser, userService, messageRecipientService,
				committeeAndCommitteeMembersService);
		return "add-edit-position";
	}

	@RequestMapping(value = { "/admin/add-edit-position" }, method = RequestMethod.POST)
	public String setCreateNewPosition(
			@Valid @ModelAttribute("createEditPositionsFormDTO") CreatePositionsFormDTO createEditPositionsFormDTO,
			BindingResult bindingResult, Model model, @CurrentUser User currentUser,
			RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("createEditPositionsFormDTO", createEditPositionsFormDTO);
			model.addAttribute("editPositionsFormDTO", new EditPositionsFormDTO());
			model.addAttribute("currentPositions", getCurrentPositions());
			Notification.getNotifications(model, currentUser, userService, messageRecipientService,
					committeeAndCommitteeMembersService);
			return "add-edit-position";
		}

		Position position = createEditPositionsFormDTO.createPosition();

		if (positionService.findByName(position.getName()) != null) {
			log.info("Duplicate Position!");
			model.addAttribute("createEditPositionsFormDTO", createEditPositionsFormDTO);
			model.addAttribute("editPositionsFormDTO", new EditPositionsFormDTO());
			model.addAttribute("currentPositions", getCurrentPositions());
			model.addAttribute("duplicate1", "This position already exist!");
			Notification.getNotifications(model, currentUser, userService, messageRecipientService,
					committeeAndCommitteeMembersService);
			return "add-edit-position";
		}

		positionService.save(position);

		redirectAttributes.addFlashAttribute("addedSuccessfully1", "You Have Successfully added a Position!");
		return "redirect:/admin/add-edit-position";

	}

	@RequestMapping(value = { "/admin/edit-position" }, method = RequestMethod.POST)
	public String setEditNewPosition(
			@Valid @ModelAttribute("editPositionsFormDTO") EditPositionsFormDTO editPositionsFormDTO,
			BindingResult bindingResult, Model model, @CurrentUser User currentUser,
			@RequestParam(name = "positionId") Long positionId, RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors() || positionId == -1) {
			model.addAttribute("editPositionsFormDTO", editPositionsFormDTO);
			model.addAttribute("createEditPositionsFormDTO", new CreatePositionsFormDTO());
			model.addAttribute("currentPositions", getCurrentPositions());
			Notification.getNotifications(model, currentUser, userService, messageRecipientService,
					committeeAndCommitteeMembersService);

			model.addAttribute("duplicate", "Please select the Position you wish to change");
			return "add-edit-position";
		}

		Position position = editPositionsFormDTO.editPosition(positionService.findById(positionId));

		if (!positionService.findById(positionId).getName().equals(position.getName())) {

			if (positionService.findByName(position.getName()) != null) {
				log.info("Duplicate Position!");
				model.addAttribute("editPositionsFormDTO", editPositionsFormDTO);
				model.addAttribute("createEditPositionsFormDTO", new CreatePositionsFormDTO());
				model.addAttribute("duplicate", "This position already exist!");
				model.addAttribute("currentPositions", getCurrentPositions());
				Notification.getNotifications(model, currentUser, userService, messageRecipientService,
						committeeAndCommitteeMembersService);
				return "add-edit-position";
			}

		}

		positionService.save(position);

		redirectAttributes.addFlashAttribute("addedSuccessfully", "You Have Successfully updated the Position!");
		return "redirect:/admin/add-edit-position";
	}

	private List<Position> getCurrentPositions() {
		return positionService.findAllPositions();
	}
}
