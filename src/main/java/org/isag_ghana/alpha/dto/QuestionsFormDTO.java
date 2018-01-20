package org.isag_ghana.alpha.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.isag_ghana.alpha.model.Question;

import lombok.Data;

@Data
public class QuestionsFormDTO {

	@NotEmpty(message = "Question cannot be empty")
	@NotNull(message = "Question is required")
	@Length(max = 2000, min = 3, message = "Question is required and must be between 3 and 2000 characters")
	private String question;

	public Question creatQuestion() {
		Question question = new Question();
		question.setQuestion(this.question);
		return question;
	}
}
