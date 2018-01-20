package org.isag_ghana.alpha.serviceImpl;

import java.util.List;

import org.isag_ghana.alpha.model.QuestionType;
import org.isag_ghana.alpha.repository.QuestionTypeRepository;
import org.isag_ghana.alpha.service.QuestionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class QuestionTypeServiceImpl implements QuestionTypeService {

	private final QuestionTypeRepository questionTypeRepository;

	@Autowired
	public QuestionTypeServiceImpl(QuestionTypeRepository questionTypeRepository) {
		this.questionTypeRepository = questionTypeRepository;
	}

	@Override
	public List<QuestionType> findAll() {
		return questionTypeRepository.findAll();
	}

	@Override
	public QuestionType findOne(long questionTypeId) {
		return questionTypeRepository.findOne(questionTypeId);
	}

	@Override
	public QuestionType findOne(String questionTypeName) {
		return questionTypeRepository.findOne(questionTypeName);
	}

	@Override
	public void save(QuestionType questionType) {
		questionTypeRepository.save(questionType);
	}

}
