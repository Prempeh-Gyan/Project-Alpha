package org.isag_ghana.alpha.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.isag_ghana.alpha.model.Committee;

import lombok.Data;

@Data
public class EditCommitteeFormDTO {

	@NotEmpty(message = "Committee name must be entered")
	@NotNull(message = "Committee name cannot be null")
	private String name;
	
	@NotEmpty(message = "Committee description must be entered")
	@NotNull(message = "Committee description cannot be null")
	private String description;

	public Committee editCommittee(Committee committee) {
		committee.setName(name.toUpperCase());
		committee.setDescription(description);
		return committee;
	}

}
