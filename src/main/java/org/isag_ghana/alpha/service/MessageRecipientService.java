package org.isag_ghana.alpha.service;

import java.util.List;

import org.isag_ghana.alpha.model.MessageAndRecipient;
import org.isag_ghana.alpha.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageRecipientService {

	public List<MessageAndRecipient> findAllMessagesByUser(User user);

	public List<MessageAndRecipient> findAllUnreadMessagesByUser(User user);

	public MessageAndRecipient findById(Long messageRecipientId);

	public void save(MessageAndRecipient messageRecipient);

	public void save(List<MessageAndRecipient> messageRecipients);

	public Page<MessageAndRecipient> findAllMessagesByUser(User user, Pageable pageable);

}