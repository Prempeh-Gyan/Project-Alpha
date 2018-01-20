package org.isag_ghana.alpha.controller.publik;

import javax.validation.constraints.NotNull;

import org.isag_ghana.alpha.model.User;
import org.isag_ghana.alpha.service.CommitteeAndCommitteeMembersService;
import org.isag_ghana.alpha.service.MessageRecipientService;
import org.isag_ghana.alpha.service.UserService;
import org.isag_ghana.alpha.util.CurrentUser;
import org.isag_ghana.alpha.util.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor(onConstructor = @__({ @Autowired }))
public class HomeController {

	private final @NotNull MessageRecipientService messageRecipientService;
	private final @NotNull UserService userService;
	private final @NotNull CommitteeAndCommitteeMembersService committeeAndCommitteeMembersService;

	@RequestMapping(value = { "/public/home" }, method = RequestMethod.GET)
	public String indexPage(@CurrentUser User currentUser, Model model) {

		log.info("Home page fired...");
		if (currentUser != null) {
			Notification.getNotifications(model, currentUser, userService, messageRecipientService,
					committeeAndCommitteeMembersService);
		} else {
			model.addAttribute("currentUser", currentUser);
		}

		return "home";
	}
}
