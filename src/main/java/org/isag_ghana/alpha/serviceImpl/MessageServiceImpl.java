package org.isag_ghana.alpha.serviceImpl;

import java.util.List;

import org.isag_ghana.alpha.model.Message;
import org.isag_ghana.alpha.model.User;
import org.isag_ghana.alpha.repository.MessageRepository;
import org.isag_ghana.alpha.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {

	private final MessageRepository messageRepository;

	@Autowired
	public MessageServiceImpl(MessageRepository messageRepository) {
		this.messageRepository = messageRepository;
	}

	@Override
	public void save(Message message) {
		messageRepository.save(message);
	}

	@Override
	public List<Message> findAllMessagesByUser(User user) {
		return messageRepository.findAllMessagesByUser(user);
	}

	@Override
	public Message findById(Long messageId) {
		return messageRepository.findOne(messageId);
	}

	@Override
	public Page<Message> findAllMessagesByUser(User user, Pageable pageable) {
		return messageRepository.findAllMessagesByUser(user, pageable);
	}
}
