package org.isag_ghana.alpha.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.isag_ghana.alpha.model.Position;

import lombok.Data;

@Data
public class EditPositionsFormDTO {

	@NotEmpty(message = "Title must be entered")
	@NotNull(message = "Title cannot be null")
	private String title;

	@NotEmpty(message = "PositionType cannot be empty")
	@NotNull(message = "PositionType cannot be null")
	private String positionType;

	public Position editPosition(Position position) {

		position.setName(title.toUpperCase());

		if (positionType == "headOfExecutiveCouncil") {
			position.setIsHeadPosition(true);
			position.setIsExecutivePosition(true);

		} else if (positionType == "partOfExecutiveCouncil") {
			position.setIsExecutivePosition(true);

		} else if (positionType == "headOfCommitteeExecutives") {
			position.setIsHeadPosition(true);
			position.setIsCommitteePosition(true);

		} else if (positionType == "partOfCommitteeExecutives") {
			position.setIsCommitteePosition(true);

		}

		return position;
	}

}
