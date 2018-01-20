package org.isag_ghana.alpha.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Position {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(unique = true, nullable = false)
	private String name;
	
	private String description;
	
	private Boolean isExecutivePosition = false;

	private Boolean isCommitteePosition = false;

	private Boolean isHeadPosition = false;

	private LocalDateTime createdDate = LocalDateTime.now();

}
