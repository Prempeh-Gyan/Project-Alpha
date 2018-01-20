package org.isag_ghana.alpha.service;

import java.util.List;

import org.isag_ghana.alpha.model.QuestionType;

public interface QuestionTypeService {

	public List<QuestionType> findAll();

	public QuestionType findOne(long questionTypeId);

	public QuestionType findOne(String questionTypeName);

	public void save(QuestionType questionType);

}