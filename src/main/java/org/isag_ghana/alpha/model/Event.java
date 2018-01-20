package org.isag_ghana.alpha.model;

import java.time.LocalDateTime;

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
public class Event {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String name;

	private String theme;

	private String venue;

	private String description;

	private String summary;

	private String map;

	private String liveFeed;

	private Boolean isISAGEvent = false;

	private Boolean isMembersEvent = false;

	private Boolean isCancelled = false;

	private Boolean isPassed = false;

	private Boolean hasPhoto = false;

	private Boolean hasCoverPicture = false;

	private Boolean hasVideo = false;

	private Boolean hasPromotionalVideo = false;

	private LocalDateTime startTime;

	private LocalDateTime endTime;

	private LocalDateTime date = LocalDateTime.now();

}
