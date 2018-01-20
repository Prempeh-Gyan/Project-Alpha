package org.isag_ghana.alpha.serviceImpl;

import java.util.List;

import org.isag_ghana.alpha.model.Event;
import org.isag_ghana.alpha.repository.EventRepository;
import org.isag_ghana.alpha.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EventServiceImpl implements EventService {

	private final EventRepository eventRepository;

	@Autowired
	public EventServiceImpl(EventRepository eventRepository) {
		this.eventRepository = eventRepository;
	}

	@Override
	public void save(Event event) {
		eventRepository.save(event);
	}

	@Override
	public List<Event> findAll() {
		return eventRepository.findAll();
	}

	@Override
	public Event findById(Long eventId) {
		return eventRepository.findOne(eventId);
	}

	@Override
	public Page<Event> findAll(Pageable pageable) {
		return eventRepository.findAll(pageable);
	}

	@Override
	public List<Event> findAllISAGEvents() {
		return eventRepository.findAllISAGEvents();
	}

	@Override
	public List<Event> findAllMembersEvents() {
		return eventRepository.findAllMembersEvents();
	}

	@Override
	public List<Event> findAllUpComingEvents() {
		return eventRepository.findAllUpComingEvents();
	}

	@Override
	public List<Event> findAllUpComingISAGEvents() {
		return eventRepository.findAllUpComingISAGEvents();
	}

	@Override
	public List<Event> findAllUpComingMembersEvents() {
		return eventRepository.findAllUpComingMembersEvents();
	}

	@Override
	public List<Event> findNextISAGEvent() {
		return eventRepository.findNextISAGEvent();
	}

	@Override
	public List<Event> findNextMembersEvent() {
		return eventRepository.findNextMembersEvent();
	}

	@Override
	public List<Event> findJustEndedISAGEvent() {
		return eventRepository.findJustEndedISAGEvent();
	}

	@Override
	public Page<Event> findAllISAGEvents(Pageable pageable) {
		return eventRepository.findAllISAGEvents(pageable);
	}

	@Override
	public Page<Event> findAllEventsSorted(Pageable pageable) {
		return eventRepository.findAllEventsSorted(pageable);
	}

}
