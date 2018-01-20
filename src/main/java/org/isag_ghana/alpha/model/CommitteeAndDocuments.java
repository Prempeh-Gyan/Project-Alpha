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
public class CommitteeAndDocuments {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne
	private Committee committee;

	@ManyToOne
	private Document document;

	private LocalDateTime date = LocalDateTime.now();

	private Boolean isReport = false;
	
	private Boolean isMinute = false;
	
	private Boolean isLegal = false;
	
	private Boolean isAdmin = false;
	
	private Boolean isConfidential = false;
	
}
