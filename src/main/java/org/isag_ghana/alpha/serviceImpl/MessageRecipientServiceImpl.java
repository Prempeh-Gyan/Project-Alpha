package org.isag_ghana.alpha.serviceImpl;

import java.util.List;

import org.isag_ghana.alpha.model.MessageAndRecipient;
import org.isag_ghana.alpha.model.User;
import org.isag_ghana.alpha.repository.MessageRecipientRepository;
import org.isag_ghana.alpha.service.MessageRecipientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MessageRecipientServiceImpl implements MessageRecipientService {

	private final MessageRecipientRepository messageRecipientRepository;

	@Autowired
	public MessageRecipientServiceImpl(MessageRecipientRepository messageRecipientRepository) {
		this.messageRecipientRepository = messageRecipientRepository;
	}

	@Override
	public List<MessageAndRecipient> findAllMessagesByUser(User user) {
		return messageRecipientRepository.findAllMessagesByUser(user);
	}

	@Override
	public MessageAndRecipient findById(Long messageRecipientId) {
		return messageRecipientRepository.findOne(messageRecipientId);
	}

	@Override
	public void save(MessageAndRecipient messageRecipient) {
		messageRecipientRepository.save(messageRecipient);
	}

	@Override
	public Page<MessageAndRecipient> findAllMessagesByUser(User user, Pageable pageable) {
		return messageRecipientRepository.findAllMessagesByUser(user, pageable);
	}

	@Override
	public void save(List<MessageAndRecipient> messageRecipients) {
		messageRecipientRepository.save(messageRecipients);
	}

	@Override
	public List<MessageAndRecipient> findAllUnreadMessagesByUser(User user) {
		return messageRecipientRepository.findAllUnreadMessagesByUser(user);
	}

}
