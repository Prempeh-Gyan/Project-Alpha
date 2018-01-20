package org.isag_ghana.alpha.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PositionAndPositionHolder {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne
	private User positionHolder;

	@ManyToOne
	private Position position;

	private LocalDateTime startDate = LocalDateTime.now();
	
	private LocalDateTime endDate;

	private Boolean isTenureExpired = false;
	
	private Boolean isExecutivePosition = false;

	private Boolean isCommitteePosition = false;

	private Boolean isHeadPosition = false;
}
