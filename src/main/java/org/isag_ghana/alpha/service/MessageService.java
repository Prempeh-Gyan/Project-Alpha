package org.isag_ghana.alpha.service;

import java.util.List;

import org.isag_ghana.alpha.model.Message;
import org.isag_ghana.alpha.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageService {

	public List<Message> findAllMessagesByUser(User user);

	public Message findById(Long messageId);

	public void save(Message message);

	public Page<Message> findAllMessagesByUser(User user, Pageable pageable);

}