package org.isag_ghana.alpha.controller.registration;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.isag_ghana.alpha.dto.ResetPasswordDto;
import org.isag_ghana.alpha.model.User;
import org.isag_ghana.alpha.model.VerificationToken;
import org.isag_ghana.alpha.security.ISecurityUserService;
import org.isag_ghana.alpha.service.CommitteeAndCommitteeMembersService;
import org.isag_ghana.alpha.service.IUserService;
import org.isag_ghana.alpha.service.MessageRecipientService;
import org.isag_ghana.alpha.service.UserService;
import org.isag_ghana.alpha.util.CurrentUser;
import org.isag_ghana.alpha.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor(onConstructor = @__({ @Autowired }))
public class RegistrationController {

	private final @NotNull UserService userService;
	private final @NotNull MessageRecipientService messageRecipientService;
	private final @NotNull CommitteeAndCommitteeMembersService committeeAndCommitteeMembersService;

	@Autowired
	private MessageSource messages;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private Environment env;

	@Autowired
	private ISecurityUserService securityUserService;

	@Autowired
	private IUserService iuserService;

	@RequestMapping("/forgotten-password")
	public String badUser() {
		return "forgotten-password";
	}

	@RequestMapping("/updatePassword")
	public String updatePassword() {
		return "updatePassword";
	}

	@RequestMapping(value = "/changePassword", method = RequestMethod.GET)
	public String changePassword(Model model) {
		model.addAttribute("resetPasswordDto", new ResetPasswordDto());
		return "changePassword";
	}

	@RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
	public String confirmRegistration(final Locale locale, final Model model, @RequestParam("token") final String token)
			throws UnsupportedEncodingException {
		log.info("registration confirmation triggered");
		final String result = iuserService.validateVerificationToken(token);

		if (result.equals("valid")) {
			final User user = iuserService.getUser(token);
			System.out.println(user);
			user.setIsEmailConfirmed(true);
			iuserService.saveRegisteredUser(user);
			if (user.getIsUsing2FA()) {
				model.addAttribute("qr", iuserService.generateQRUrl(user));
				return "redirect:/qrcode.html?lang=" + locale.getLanguage();
			}
			model.addAttribute("message", messages.getMessage("message.accountVerified", null, locale));
			return "redirect:/login?lang=" + locale.getLanguage();
		}

		model.addAttribute("message", messages.getMessage("auth.message." + result, null, locale));
		model.addAttribute("expired", "expired".equals(result));
		model.addAttribute("token", token);
		return "redirect:/badUser.html?lang=" + locale.getLanguage();
	}

	@RequestMapping(value = "/user/resendRegistrationToken", method = RequestMethod.GET)
	@ResponseBody
	public GenericResponse resendRegistrationToken(final HttpServletRequest request,
			@RequestParam("token") final String existingToken) {
		final VerificationToken newToken = iuserService.generateNewVerificationToken(existingToken);
		final User user = iuserService.getUser(newToken.getToken());
		mailSender.send(constructResendVerificationTokenEmail(getAppUrl(request), request.getLocale(), newToken, user));
		return new GenericResponse(messages.getMessage("message.resendToken", null, request.getLocale()));
	}

	@RequestMapping(value = "/user/resetPassword", method = RequestMethod.GET)
	@ResponseBody
	public GenericResponse resetPassword(final HttpServletRequest request,
			@RequestParam("email") final String userEmail) {
		log.info("reset password trigered");
		final User user = iuserService.findUserByEmail(userEmail);
		if (user != null) {
			final String token = UUID.randomUUID().toString();
			iuserService.createPasswordResetTokenForUser(user, token);
			mailSender.send(constructResetTokenEmail(getAppUrl(request), request.getLocale(), token, user));
		}
		return new GenericResponse(messages.getMessage("message.resetPasswordEmail", null, request.getLocale()));
	}

	@RequestMapping(value = "/user/changePassword", method = RequestMethod.GET)
	public String showChangePasswordPage(final Locale locale, final Model model, @RequestParam("id") final long id,
			@RequestParam("token") final String token, RedirectAttributes redirectAttributes) {
		final String result = securityUserService.validatePasswordResetToken(id, token);
		if (result != null) {
			redirectAttributes.addAttribute("message", messages.getMessage("auth.message." + result, null, locale));
			return "redirect:/login?lang=" + locale.getLanguage();
		}
		redirectAttributes.addFlashAttribute("resetPasswordDto", new ResetPasswordDto());
		redirectAttributes.addFlashAttribute("id", id);
		redirectAttributes.addFlashAttribute("token", token);
		return "redirect:/updatePassword.html?lang=" + locale.getLanguage();
	}

	@RequestMapping(value = "/user/saveResetPassword", method = RequestMethod.GET)
	public String saveResetPassword() {
		return "redirect:/login";
	}

	@RequestMapping(value = "/user/saveResetPassword", method = RequestMethod.POST)
	public String saveResetPasswordForUser(final Locale locale,
			@Valid @ModelAttribute("resetPasswordDto") ResetPasswordDto resetPasswordDto, BindingResult bindingResult,
			@RequestParam("id") final long id, @RequestParam("token") final String token,
			RedirectAttributes redirectAttributes, Model model) {
		if (bindingResult.hasErrors()) {
			log.info("errors in setting new password");
			redirectAttributes.addAttribute("message", messages.getMessage("message.passwordErrMessage", null, locale));
			return "redirect:/login?lang=" + locale.getLanguage();

		} else {
			log.info("no errors in setting new password");
			final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			iuserService.changeUserPassword(user, resetPasswordDto.getPassword());
			redirectAttributes.addAttribute("message", messages.getMessage("message.resetPasswordSuc", null, locale));
			return "redirect:/login";
		}
	}

	/*
	 * @RequestMapping(value = "/user/savePassword", method =
	 * RequestMethod.POST)
	 * 
	 * @ResponseBody public GenericResponse savePassword(final Locale
	 * locale, @Valid PasswordDto passwordDto) {
	 * log.info("savePassword trigerred"); final User user = (User)
	 * SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	 * iuserService.changeUserPassword(user, passwordDto.getNewPassword());
	 * return new
	 * GenericResponse(messages.getMessage("message.resetPasswordSuc", null,
	 * locale)); }
	 */

	// change user password
	/*
	 * @RequestMapping(value = "/user/updatePassword", method =
	 * RequestMethod.POST)
	 * 
	 * @ResponseBody public GenericResponse changeUserPassword(final Locale
	 * locale, @Valid PasswordDto passwordDto) { final User user =
	 * iuserService.findUserByEmail( ((User)
	 * SecurityContextHolder.getContext().getAuthentication().getPrincipal()).
	 * getEmail()); if (!iuserService.checkIfValidOldPassword(user,
	 * passwordDto.getOldPassword())) { throw new InvalidOldPasswordException();
	 * } iuserService.changeUserPassword(user, passwordDto.getNewPassword());
	 * return new
	 * GenericResponse(messages.getMessage("message.updatePasswordSuc", null,
	 * locale)); }
	 */

	@RequestMapping(value = "/user/updatePassword", method = RequestMethod.POST)
	public String changeMyPassword(final Locale locale, @CurrentUser User currentUser,
			@Valid @ModelAttribute("resetPasswordDto") ResetPasswordDto resetPasswordDto, BindingResult bindingResult,
			@RequestParam("oldPassword") String oldPassword, RedirectAttributes redirectAttributes) {

		if (oldPassword == null || oldPassword == "" || oldPassword.isEmpty()) {
			redirectAttributes.addAttribute("message", messages.getMessage("message.oldPasswordEmpty", null, locale));
			return "redirect:/changePassword";
		}

		if (bindingResult.hasErrors()) {
			redirectAttributes.addAttribute("message",
					messages.getMessage("message.newPasswordErrMessage", null, locale));
			return "redirect:/changePassword";

		}

		final User user = iuserService.findUserByEmail(currentUser.getEmail());
		if (!iuserService.checkIfValidOldPassword(user, oldPassword)) {
			redirectAttributes.addAttribute("message",
					messages.getMessage("message.oldPasswordErr", null, locale));
			return "redirect:/changePassword";
		}
		iuserService.changeUserPassword(user, resetPasswordDto.getPassword());
		redirectAttributes.addAttribute("message", messages.getMessage("message.updatePasswordSuc", null, locale));
		return "redirect:/login";
	}

	private SimpleMailMessage constructResendVerificationTokenEmail(final String contextPath, final Locale locale,
			final VerificationToken newToken, final User user) {
		final String confirmationUrl = contextPath + "/registrationConfirm.html?token=" + newToken.getToken();
		final String message = messages.getMessage("message.resendToken", null, locale);
		return constructEmail("New Token for ISAG Registration Confirmation", message + " \r\n" + confirmationUrl,
				user);
	}

	private SimpleMailMessage constructResetTokenEmail(final String contextPath, final Locale locale,
			final String token, final User user) {
		final String url = contextPath + "/user/changePassword?id=" + user.getId() + "&token=" + token;
		final String message = messages.getMessage("message.resetPassword", null, locale);
		return constructEmail("Reset Password", message + " \r\n" + url, user);
	}

	private SimpleMailMessage constructEmail(String subject, String body, User user) {
		final SimpleMailMessage email = new SimpleMailMessage();
		email.setSubject(subject);
		email.setText(body);
		email.setTo(user.getEmail());
		email.setFrom(env.getProperty("support.email"));
		return email;
	}

	private String getAppUrl(HttpServletRequest request) {
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}
}
