package org.isag_ghana.alpha.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.isag_ghana.alpha.model.NewsBlog;

import lombok.Data;

@Data
public class AddNewsFormDTO {

	@NotEmpty(message = "Title must be entered")
	@NotNull(message = "Title cannot be null")
	private String title;

	@NotEmpty(message = "The blog must have contents")
	@NotNull(message = "The blog cannot be null")
	private String content;

	public NewsBlog createBlog() {
		NewsBlog blog = new NewsBlog();
		blog.setTitle(title);
		blog.setContent(content);
		return blog;
	}

}
