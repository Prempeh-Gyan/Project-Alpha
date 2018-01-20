package org.isag_ghana.alpha.controller.publik;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.isag_ghana.alpha.model.Committee;
import org.isag_ghana.alpha.model.Position;
import org.isag_ghana.alpha.model.PositionAndPositionHolder;
import org.isag_ghana.alpha.model.User;
import org.isag_ghana.alpha.service.CommitteeAndCommitteeMembersService;
import org.isag_ghana.alpha.service.CommitteeAndPhotosService;
import org.isag_ghana.alpha.service.CommitteeAndVideosService;
import org.isag_ghana.alpha.service.CommitteeService;
import org.isag_ghana.alpha.service.MessageRecipientService;
import org.isag_ghana.alpha.service.PositionAndPositionHoldersService;
import org.isag_ghana.alpha.service.PositionService;
import org.isag_ghana.alpha.service.UserService;
import org.isag_ghana.alpha.util.CurrentUser;
import org.isag_ghana.alpha.util.Notification;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CommitteesController {
	private final @NotNull UserService userService;
	private final @NotNull MessageRecipientService messageRecipientService;
	private final @NotNull CommitteeAndCommitteeMembersService committeeAndCommitteeMembersService;
	private final @NotNull CommitteeService committeeService;
	private final @NotNull CommitteeAndPhotosService committeeAndPhotosService;
	private final @NotNull CommitteeAndVideosService committeeAndVideosService;
	private final @NotNull PositionAndPositionHoldersService positionAndPositionHoldersService;
	private final @NotNull PositionService positionService;

	@RequestMapping(value = { "/public/committee/{committeeId}" }, method = RequestMethod.GET)
	public String newsBlogList(@CurrentUser User currentUser, Model model,
			@PathVariable("committeeId") Long committeeId) {

		if (currentUser != null) {
			Notification.getNotifications(model, currentUser, userService, messageRecipientService,
					committeeAndCommitteeMembersService);
		} else {
			model.addAttribute("currentUser", currentUser);
		}

		if (committeeId <= 2 || committeeId > 8) {

			Committee committee = committeeService.findById(2l);
			model.addAttribute("committee", committee);
			model.addAttribute("committeePhotos", committeeAndPhotosService.findAllByCommittee(committee));
			model.addAttribute("committeeVideos", committeeAndVideosService.findAllByCommittee(committee));

			Position position = positionService.findById(5l);

			log.info("Position is: {}", position == null ? null : position.getName());

			List<PositionAndPositionHolder> pph = positionAndPositionHoldersService
					.findCurrentPositionHolderByPosition(position);

			model.addAttribute("positionAndHolder", pph.isEmpty() ? null : pph.get(0));

			log.info("Position Holder is: {}",
					pph.isEmpty() ? null : pph.get(0).getPositionHolder().getProfile().getFirstName());

		} else {
			Committee committee = committeeService.findById(committeeId);
			model.addAttribute("committee", committee);
			model.addAttribute("committeePhotos", committeeAndPhotosService.findAllByCommittee(committee));
			model.addAttribute("committeeVideos", committeeAndVideosService.findAllByCommittee(committee));

			if (committeeId == 3) {

				Position position = positionService.findById(6l);

				log.info("Position is: {}", position == null ? null : position.getName());

				List<PositionAndPositionHolder> pph = positionAndPositionHoldersService
						.findCurrentPositionHolderByPosition(position);

				model.addAttribute("positionAndHolder", pph.isEmpty() ? null : pph.get(0));

				log.info("Position Holder is: {}",
						pph.isEmpty() ? null : pph.get(0).getPositionHolder().getProfile().getFirstName());

			} else if (committeeId == 4) {

				Position position = positionService.findById(8l);

				log.info("Position is: {}", position == null ? null : position.getName());

				List<PositionAndPositionHolder> pph = positionAndPositionHoldersService
						.findCurrentPositionHolderByPosition(position);

				model.addAttribute("positionAndHolder", pph.isEmpty() ? null : pph.get(0));

				log.info("Position Holder is: {}",
						pph.isEmpty() ? null : pph.get(0).getPositionHolder().getProfile().getFirstName());

			} else if (committeeId == 5) {

				Position position = positionService.findById(4l);

				log.info("Position is: {}", position == null ? null : position.getName());

				List<PositionAndPositionHolder> pph = positionAndPositionHoldersService
						.findCurrentPositionHolderByPosition(position);

				model.addAttribute("positionAndHolder", pph.isEmpty() ? null : pph.get(0));

				log.info("Position Holder is: {}",
						pph.isEmpty() ? null : pph.get(0).getPositionHolder().getProfile().getFirstName());

			} else if (committeeId == 6) {

				Position position = positionService.findById(10l);

				log.info("Position is: {}", position == null ? null : position.getName());

				List<PositionAndPositionHolder> pph = positionAndPositionHoldersService
						.findCurrentPositionHolderByPosition(position);

				model.addAttribute("positionAndHolder", pph.isEmpty() ? null : pph.get(0));

				log.info("Position Holder is: {}",
						pph.isEmpty() ? null : pph.get(0).getPositionHolder().getProfile().getFirstName());

			} else if (committeeId == 7) {

				Position position = positionService.findById(7l);

				log.info("Position is: {}", position == null ? null : position.getName());

				List<PositionAndPositionHolder> pph = positionAndPositionHoldersService
						.findCurrentPositionHolderByPosition(position);

				model.addAttribute("positionAndHolder", pph.isEmpty() ? null : pph.get(0));

				log.info("Position Holder is: {}",
						pph.isEmpty() ? null : pph.get(0).getPositionHolder().getProfile().getFirstName());

			} else if (committeeId == 8) {

				Position position = positionService.findById(9l);

				log.info("Position is: {}", position == null ? null : position.getName());

				List<PositionAndPositionHolder> pph = positionAndPositionHoldersService
						.findCurrentPositionHolderByPosition(position);

				model.addAttribute("positionAndHolder", pph.isEmpty() ? null : pph.get(0));

				log.info("Position Holder is: {}",
						pph.isEmpty() ? null : pph.get(0).getPositionHolder().getProfile().getFirstName());
			}
		}
		return "committee";
	}
}
