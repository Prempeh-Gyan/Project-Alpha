package org.isag_ghana.alpha.controller.report;

import javax.validation.constraints.NotNull;

import org.isag_ghana.alpha.model.User;
import org.isag_ghana.alpha.service.MessageRecipientService;
import org.isag_ghana.alpha.util.CurrentUser;
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
public class UserReportController {
	private final @NotNull MessageRecipientService messageRecipientService;

	@RequestMapping(value = { "/public/aboutus" }, method = RequestMethod.GET)
	public String indexPage(@CurrentUser User currentUser, Model model) {
		log.info("aboutus fired...");
		model.addAttribute("currentUser", currentUser);
		if (currentUser != null) {
			model.addAttribute("currentUsersMessageRecipients",
					messageRecipientService.findAllUnreadMessagesByUser(currentUser));
		}
		return "aboutus";
	}
}