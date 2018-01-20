package org.isag_ghana.alpha.controller.publik;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.isag_ghana.alpha.controller.registration.OnRegistrationCompleteEvent;
import org.isag_ghana.alpha.dto.SignUpFormDTO;
import org.isag_ghana.alpha.model.User;
import org.isag_ghana.alpha.service.CommitteeAndCommitteeMembersService;
import org.isag_ghana.alpha.service.MessageRecipientService;
import org.isag_ghana.alpha.service.UserService;
import org.isag_ghana.alpha.util.CurrentUser;
import org.isag_ghana.alpha.util.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor(onConstructor = @__({ @Autowired }))
public class JoinUsController {

	private final @NotNull UserService userService;
	private final @NotNull MessageRecipientService messageRecipientService;
	private final @NotNull CommitteeAndCommitteeMembersService committeeAndCommitteeMembersService;

	@Autowired
	ApplicationEventPublisher eventPublisher;

	@RequestMapping(value = "/public/joinus", method = RequestMethod.GET)
	public String getSignUp(@CurrentUser User currentUser, Model model) {
		model.addAttribute("signUPFormDTO", new SignUpFormDTO());

		if (currentUser != null) {
			Notification.getNotifications(model, currentUser, userService, messageRecipientService,
					committeeAndCommitteeMembersService);
		} else {
			model.addAttribute("currentUser", currentUser);
		}
		return "joinus";
	}

	@RequestMapping(value = "/public/joinus", method = RequestMethod.POST)
	public String postSignUp(@Valid @ModelAttribute("signUPFormDTO") SignUpFormDTO signUpFormDTO,
			BindingResult bindingResult, Model model, @CurrentUser User currentUser, WebRequest request,
			RedirectAttributes redirectAttributes) {

		log.info("Join us post fired...");

		if (bindingResult.hasErrors()) {
			log.info("Errors in signup forms...");
			model.addAttribute("signUPFormDTO", signUpFormDTO);
			if (currentUser != null) {
				Notification.getNotifications(model, currentUser, userService, messageRecipientService,
						committeeAndCommitteeMembersService);
			} else {
				model.addAttribute("currentUser", currentUser);
			}
			return "joinus";
		}

		log.info("No error in signup forms, creating user...");

		User user = signUpFormDTO.createUser(new User());

		log.info("User created, SignUpDTO called, name is {}", user.getProfile().getFirstName());

		if (isDuplicateMember(user, userService.findByEmail(user.getEmail()))) {
			log.info("Duplicate member!");
			model.addAttribute("signUPFormDTO", signUpFormDTO);
			model.addAttribute("duplicateUser", "User already exist!");
			if (currentUser != null) {
				Notification.getNotifications(model, currentUser, userService, messageRecipientService,
						committeeAndCommitteeMembersService);
			} else {
				model.addAttribute("currentUser", currentUser);
			}
			return "joinus";
		}

		userService.saveUser(user);

		String appUrl = request.getContextPath();
		eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, request.getLocale(), appUrl));

		redirectAttributes.addFlashAttribute("userAddedSuccessfully",
				"Success! Please login to your email account and confirm your account");
		return "redirect:/public/joinus";
	}

	private boolean isDuplicateMember(User user, User dbUser) {

		if (user != null && dbUser != null) {
			log.info("User exist as {}", user.getProfile().getFirstName());
			if (user.getProfile().getFirstName().equalsIgnoreCase(dbUser.getProfile().getFirstName())
					&& user.getProfile().getLastName().equalsIgnoreCase(dbUser.getProfile().getLastName())
					&& user.getProfile().getEmail().equalsIgnoreCase(dbUser.getProfile().getEmail())) {
				return true;
			}
		}
		return false;
	}
}
