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
public class CommitteeAndCommitteeMember {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne
	private User committeeMember;

	@ManyToOne
	private Committee committee;

	private LocalDateTime memberSince = LocalDateTime.now();
	
	private LocalDateTime endDate;

	private Boolean isStillAMember = true;
	
	private Boolean isMemberRemovable = true;
	
	private Boolean requestedToJoin = false;
	
	private Boolean requestedToLeave = false;
	
	private Boolean removedByAdmin = false;
	
	
	private Boolean isExecutiveCommittee = false;
	
	private Boolean isAdvocacyCommittee = false;
	
	private Boolean isEventsCommittee = false;
	
	private Boolean isMembershipCommittee = false;
	
	private Boolean isAdministrationCommittee = false;
	
	private Boolean isWebsiteCommittee = false;
	
	private Boolean isArchivesCommittee = false;
	
	private Boolean isBaatsoonaLandCommittee = false;

}
