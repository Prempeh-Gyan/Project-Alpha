package org.isag_ghana.alpha.service;

import java.util.List;

import org.isag_ghana.alpha.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventService {

	public void save(Event event);

	public List<Event> findAll();

	public Event findById(Long eventId);

	public Page<Event> findAll(Pageable pageable);

	public List<Event> findAllISAGEvents();

	public List<Event> findAllMembersEvents();

	public List<Event> findAllUpComingEvents();

	public List<Event> findAllUpComingISAGEvents();

	public List<Event> findAllUpComingMembersEvents();

	public List<Event> findNextISAGEvent();

	public List<Event> findJustEndedISAGEvent();

	public List<Event> findNextMembersEvent();

	public Page<Event> findAllISAGEvents(Pageable pageable);

	public Page<Event> findAllEventsSorted(Pageable pageable);

}