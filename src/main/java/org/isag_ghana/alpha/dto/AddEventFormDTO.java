package org.isag_ghana.alpha.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.isag_ghana.alpha.model.Event;

import lombok.Data;

@Data
public class AddEventFormDTO {

	@NotEmpty(message = "Event must have a name")
	@NotNull(message = "Event name cannot be null")
	private String name;

	private String theme;

	@NotEmpty(message = "The Venue cannot be empty")
	@NotNull(message = "The Venue cannot be null")
	private String venue;

	private String map;

	private Long eventType;

	@NotEmpty(message = "Event Description cannot be empty")
	@NotNull(message = "Event Description is required")
	@Length(max = 2000, min = 3, message = "Event description is required and must be between 3 and 2000 characters")
	private String description;

	@NotEmpty(message = "Event Start Date cannot be empty")
	@NotNull(message = "Event Start Date is required")
	private String startTime;

	@NotEmpty(message = "Event End Date cannot be empty")
	@NotNull(message = "Event End Date is required")
	private String endTime;

	private LocalDateTime startDateTime;

	private LocalDateTime endDateTime;

	@AssertTrue(message = "Invalid Event date(s)!")
	public boolean isDatesValid() {

		try {

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a");

			this.startDateTime = LocalDateTime.parse(startTime, formatter);

			this.endDateTime = LocalDateTime.parse(endTime, formatter);

		} catch (DateTimeParseException e) {
			return false;
		}

		return true;
	}

	@AssertTrue(message = "Please select Event Type!")
	public boolean isValidEventType() {

		if (eventType == null || eventType == 0) {

			return false;

		} else {

			return true;

		}
	}

	@AssertTrue(message = "Map Error! Please copy embeded map text from Google Maps and paste here")
	public boolean isValidMap() {

		if (this.map == null || this.map == "" || this.map.isEmpty()) {
			return true;
		} else {
			if (this.map.startsWith("<iframe src=\"https://www.google.com/maps/embed")) {
				this.map = this.map.replace(
						this.map.substring(this.map.indexOf("width="), this.map.indexOf("frameborder=") - 1),
						"width=\"680\" height=\"450\"");
				return true;
			} else {
				return false;
			}
		}
	}

	public Event createEvent() {

		Event event = new Event();
		event.setName(name);
		event.setVenue(venue);
		event.setDescription(description);
		event.setStartTime(startDateTime);
		event.setEndTime(endDateTime);

		if (eventType == 1) {
			event.setIsISAGEvent(true);
		} else if (eventType == 2) {
			event.setIsMembersEvent(true);
		}

		if (this.theme != null && this.theme != "" && !this.theme.isEmpty()) {
			event.setTheme(theme);
		}

		if (this.map != null && this.map != "" && !this.map.isEmpty()) {
			event.setMap(map);
		}

		return event;
	}

	public Event editEvent(Event event) {

		event.setName(name);
		event.setVenue(venue);
		event.setDescription(description);
		event.setStartTime(startDateTime);
		event.setEndTime(endDateTime);

		if (eventType == 1) {
			event.setIsISAGEvent(true);
		} else if (eventType == 2) {
			event.setIsMembersEvent(true);
		}

		if (this.theme != null && !this.theme.isEmpty()) {
			event.setTheme(theme);
		}

		if (this.map != null && !this.map.isEmpty()) {
			event.setMap(map);
		}

		return event;
	}

}
