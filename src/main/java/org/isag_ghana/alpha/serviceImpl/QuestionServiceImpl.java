package org.isag_ghana.alpha.serviceImpl;

import java.util.List;

import org.isag_ghana.alpha.model.Question;
import org.isag_ghana.alpha.model.User;
import org.isag_ghana.alpha.repository.QuestionRepository;
import org.isag_ghana.alpha.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {

	private final QuestionRepository questionRepository;

	@Autowired
	public QuestionServiceImpl(QuestionRepository questionRepository) {
		this.questionRepository = questionRepository;
	}

	@Override
	public Question findOne(long questionId) {
		return questionRepository.findOne(questionId);
	}

	@Override
	public void save(Question question) {
		questionRepository.save(question);
	}

	@Override
	public Page<Question> findAll(Pageable pageable) {
		return questionRepository.findAll(pageable);
	}

	@Override
	public Page<Question> findAllQuestionsByUser(User user, Pageable pageable) {
		return questionRepository.findAllQuestionsByUser(user, pageable);
	}

	@Override
	public List<Question> findAllQuestionsByUser(User user) {
		return questionRepository.findAllQuestionsByUser(user);
	}

	@Override
	public void delete(Question question) {
		questionRepository.delete(question);
	}

	@Override
	public void delete(Long questionId) {
		questionRepository.delete(questionId);
	}

}
