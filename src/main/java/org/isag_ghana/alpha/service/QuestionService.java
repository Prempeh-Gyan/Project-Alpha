package org.isag_ghana.alpha.service;

import java.util.List;

import org.isag_ghana.alpha.model.Question;
import org.isag_ghana.alpha.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuestionService {

	public Question findOne(long questionId);

	public void save(Question question);
	
	public void delete(Question question);
	
	public void delete(Long questionId);

	public Page<Question> findAllQuestionsByUser(User user, Pageable pageable);

	public List<Question> findAllQuestionsByUser(User user);

	public Page<Question> findAll(Pageable pageable);

}