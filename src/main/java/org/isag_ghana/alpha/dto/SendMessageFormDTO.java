package org.isag_ghana.alpha.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.isag_ghana.alpha.model.Message;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class SendMessageFormDTO {

	@NotEmpty(message = "You must have a name")
	@NotNull(message = "Your name cannot be null")
	private String sendersName;

	@Email(message = "Email format is incorrect", regexp = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
	private String sendersEmail;

	@NotNull(message = "Phonenumber cannot be null")
	@NotEmpty(message = "Phonenumber cannot be Empty")
	@Size(min = 10, max = 20, message = "Phonenumber is expected to be between 10 and 20 digits")
	@Pattern(regexp = "\\d+", message = "Phonenumber must be digits only")
	private String sendersPhoneNumber;

	@NotEmpty(message = "Message cannot be empty")
	@NotNull(message = "Message is required")
	@Length(max = 200, min = 3, message = "Message cannot exceed 200 characters")
	private String messageContent;

	@NotEmpty(message = "Subject cannot be empty")
	@NotNull(message = "Subject is required")
	@Length(max = 60, min = 3, message = "Subject cannot exceed 60 characters")
	private String subject;

	public Message createMessage() {
		log.info("creating message object");
		Message message = new Message();
		message.setSendersName(sendersName);
		message.setSendersEmail(sendersEmail);
		message.setSendersPhoneNumber(sendersPhoneNumber);
		message.setSubject(subject);
		message.setMessage(messageContent);
		return message;
	}

}
