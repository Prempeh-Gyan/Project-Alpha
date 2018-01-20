package org.isag_ghana.alpha.controller.publik;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.isag_ghana.alpha.model.Gruup;
import org.isag_ghana.alpha.model.Message;
import org.isag_ghana.alpha.model.MessageAndRecipient;
import org.isag_ghana.alpha.model.User;
import org.isag_ghana.alpha.model.User_Group;
import org.isag_ghana.alpha.service.GroupService;
import org.isag_ghana.alpha.service.MessageRecipientService;
import org.isag_ghana.alpha.service.MessageService;
import org.isag_ghana.alpha.service.UserGroupService;
import org.isag_ghana.alpha.service.UserService;
import org.isag_ghana.alpha.util.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
public class MessageController {
	private final @NotNull MessageService messageService;
	private final @NotNull MessageRecipientService messageRecipientService;
	private final @NotNull UserService userService;
	private final @NotNull GroupService groupService;
	private final @NotNull UserGroupService userGroupService;

	@RequestMapping(value = { "/public/updateMessageRecipientStatus" }, method = RequestMethod.GET)
	public @ResponseBody String updateMessageStatus(@RequestParam("messageRecipientId") Long messageRecipientId) {
		MessageAndRecipient messageRecipient = messageRecipientService.findById(messageRecipientId);
		messageRecipient.setIsRead(true);
		messageRecipientService.save(messageRecipient);
		return "success!";
	}

	@RequestMapping(value = { "/public/sendMessage" }, method = RequestMethod.GET)
	public @ResponseBody String sendMessage(@CurrentUser User currentUser, @RequestParam("flag") String flag,
			@RequestParam("recipientsIdList[]") List<Long> recipientsIdList,
			@RequestParam("recipientGroupList[]") List<Long> recipientGroupIdList,
			@RequestParam("subject") String subject, @RequestParam("message") String messageContent,
			@RequestParam("parentMessageId") Long parentMessageId) {

		log.info(
				"flag: {} \n recipientsIdList: {} \n recipientGroupList: {} \n subject: {} \n parentMessageId: {} \n message: {} \n ",
				flag, recipientsIdList, recipientGroupIdList, subject, parentMessageId, messageContent);

		Message message = new Message();
		Set<User> recipients = new HashSet<User>();

		if (flag == null || flag == "" || flag.isEmpty()) {
			return "error";
		} else if (flag.equalsIgnoreCase("compose")) {
			if (((recipientsIdList == null) && (recipientGroupIdList == null))
					|| ((recipientsIdList.isEmpty()) && (recipientGroupIdList.isEmpty()))
					|| ((recipientsIdList.get(0) == -1) && (recipientGroupIdList.get(0) == -1))
					|| (messageContent == null || messageContent == "" || messageContent.isEmpty())
					|| messageContent == "error") {
				return "error";
			} else {
				message.setMessage(messageContent);
				message.setSubject(subject);
			}

		} else if (flag.equalsIgnoreCase("reply")) {
			if ((messageContent == null || messageContent == "" || messageContent.isEmpty()
					|| messageContent == "error") || (parentMessageId == null || parentMessageId == -1)) {
				return "error";
			} else {
				Message parentMessage = messageService.findById(parentMessageId);
				message.setParentMessage(parentMessage);
				message.setSubject(parentMessage.getSubject().startsWith("Re: ") ? parentMessage.getSubject()
						: parentMessage.getSubject().contains("Re: ")
								? "Re: " + parentMessage.getSubject().replaceAll("Re: ", "")
								: "Re: " + parentMessage.getSubject());
				message.setMessage(messageContent);
				recipients.add(parentMessage.getSender());
			}

		} else if (flag.equalsIgnoreCase("forward")) {
			if (((recipientsIdList == null) && (recipientGroupIdList == null))
					|| ((recipientsIdList.isEmpty()) && (recipientGroupIdList.isEmpty()))
					|| ((recipientsIdList.get(0) == -1) && (recipientGroupIdList.get(0) == -1))
					|| (parentMessageId == null || parentMessageId == -1)) {
				return "error";
			} else {
				Message parentMessage = messageService.findById(parentMessageId);
				message.setParentMessage(parentMessage);
				message.setSubject(parentMessage.getSubject().startsWith("Fwd: ") ? parentMessage.getSubject()
						: parentMessage.getSubject().contains("Fwd: ")
								? "Fwd: " + parentMessage.getSubject().replaceAll("Fwd: ", "")
								: "Fwd: " + parentMessage.getSubject());
				message.setMessage(messageContent);
			}

		} else {
			return "error";
		}

		log.info("Sending message");

		message.setSender(currentUser);
		message.setSendersName(currentUser.getProfile().getFirstName() + " " + currentUser.getProfile().getLastName());
		message.setSendersEmail(currentUser.getProfile().getEmail());
		message.setSendersPhoneNumber(currentUser.getProfile().getPhoneNumber());

		messageService.save(message);

		if (recipientsIdList != null && !recipientsIdList.isEmpty()) {
			for (Long recipientId : recipientsIdList) {
				if (recipientId != null && recipientId > 0) {
					User recipient = userService.findOne(recipientId);
					if (recipient != null) {
						recipients.add(recipient);
					}
				}
			}
		}

		if (recipientGroupIdList != null && !recipientGroupIdList.isEmpty()) {
			for (Long recipientGroupId : recipientGroupIdList) {
				if (recipientGroupId != null && recipientGroupId > 0) {
					Gruup group = groupService.findById(recipientGroupId);
					if (group != null) {
						List<User_Group> userGroups = userGroupService.findByGroup(group);
						if (!userGroups.isEmpty()) {
							for (User_Group ug : userGroups) {
								if (ug != null) {
									User u = ug.getUser();
									if (u != null) {
										recipients.add(u);
									}
								}
							}
						}
					}
				}
			}
		}

		if (!recipients.isEmpty()) {

			recipients.parallelStream().forEach((User rp) -> {
				log.info("Current User Id is: {}", currentUser.getId());

				log.info("Recipient Id is: {}", rp.getId());

				log.info("Is Current User same as Recipient? Ans: {}", rp.getId() == currentUser.getId());

				if (rp.getId() != currentUser.getId()) {
					log.info("Saving Recipient with Id: {}", rp.getId());
					MessageAndRecipient messageRecipient = new MessageAndRecipient();
					messageRecipient.setMessage(message);
					messageRecipient.setRecipient(rp);
					messageRecipientService.save(messageRecipient);
				}
			});
		}

		return "success";
	}

	@RequestMapping(value = { "/public/sendMessageFromPublic" }, method = RequestMethod.GET)
	public @ResponseBody String sendMessage(@CurrentUser User currentUser,
			@RequestParam("recipientsId") Long recipientsId, @RequestParam("sendersName") String sendersName,
			@RequestParam("sendersEmail") String sendersEmail,
			@RequestParam("sendersPhoneNumber") String sendersPhoneNumber, @RequestParam("subject") String subject,
			@RequestParam("message") String messageContent) {

		String emailRegex = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

		String phoneRegex = "\\d+";

		if ((sendersName == null || sendersName == "") || (recipientsId == null)
				|| (sendersEmail == null || sendersEmail == "" || !sendersEmail.matches(emailRegex))
				|| (sendersPhoneNumber == null || sendersPhoneNumber == "" || !sendersPhoneNumber.matches(phoneRegex)
						|| sendersPhoneNumber.length() > 20 || sendersPhoneNumber.length() < 10)
				|| (messageContent == null || messageContent == "" || messageContent.isEmpty())
				|| (subject == null || subject == "" || subject.isEmpty())) {

			log.info("recipientId = {} \n parentMessageId = {} \n ", recipientsId);

			return "error";

		}

		log.info("Sending message");

		Message message = new Message();
		message.setSubject(subject);
		message.setMessage(messageContent);

		if (currentUser != null) {
			message.setSender(currentUser);
			message.setSendersName(
					currentUser.getProfile().getFirstName() + " " + currentUser.getProfile().getLastName());
			message.setSendersEmail(currentUser.getProfile().getEmail());
			message.setSendersPhoneNumber(currentUser.getProfile().getPhoneNumber());
		} else {
			message.setSender(null);
			message.setSendersName(sendersName);
			message.setSendersPhoneNumber(sendersPhoneNumber);
			message.setSendersEmail(sendersEmail);
		}

		messageService.save(message);

		MessageAndRecipient messageRecipient = new MessageAndRecipient();

		if (recipientsId != null) {
			User recipient = userService.findOne(recipientsId);
			messageRecipient.setRecipient(recipient);
			messageRecipient.setMessage(message);
			messageRecipientService.save(messageRecipient);
		}

		return "success";
	}

	@RequestMapping(value = { "/public/deleteMessage" }, method = RequestMethod.POST)
	public String deleteMessage(@RequestParam("messageRecipientId") Long messageRecipientId,
			RedirectAttributes redirectAttributes) {
		MessageAndRecipient messageRecipient = messageRecipientService.findById(messageRecipientId);
		log.info("Message and Recipient Id is: {} \n Sender is: {} \n Recipient is: {}",
				messageRecipient == null ? "null" : messageRecipient.getId(),
				messageRecipient == null ? "null" : messageRecipient.getMessage().getSender(),
				messageRecipient == null ? "null" : messageRecipient.getRecipient().getProfile().getFirstName());
		messageRecipient.setIsRead(true);
		messageRecipient.setIsDeleted(true);
		messageRecipientService.save(messageRecipient);
		log.info("message deleted successfully!");
		redirectAttributes.addFlashAttribute("success", "Message Deleted Successfully");
		return "redirect:/member/inbox-message/-5";
	}
}
