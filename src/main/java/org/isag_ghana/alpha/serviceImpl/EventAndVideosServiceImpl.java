package org.isag_ghana.alpha.serviceImpl;

import java.util.List;

import org.isag_ghana.alpha.model.Event;
import org.isag_ghana.alpha.model.EventAndVideos;
import org.isag_ghana.alpha.repository.EventAndVideosRepository;
import org.isag_ghana.alpha.service.EventAndVideosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EventAndVideosServiceImpl implements EventAndVideosService {

	private final EventAndVideosRepository eventAndVideosRepository;

	@Autowired
	public EventAndVideosServiceImpl(EventAndVideosRepository eventAndVideosRepository) {
		this.eventAndVideosRepository = eventAndVideosRepository;
	}

	@Override
	public List<EventAndVideos> findAllByEvent(Event event) {
		return eventAndVideosRepository.findAllByEvent(event);
	}

	@Override
	public EventAndVideos findById(Long eventId) {
		return eventAndVideosRepository.findOne(eventId);
	}

	@Override
	public void save(EventAndVideos eventAndVideos) {
		eventAndVideosRepository.save(eventAndVideos);
	}

	@Override
	public void save(List<EventAndVideos> eventAndVideos) {
		eventAndVideosRepository.save(eventAndVideos);
	}

	@Override
	public Page<EventAndVideos> findAllByEvent(Event event, Pageable pageable) {
		return eventAndVideosRepository.findAllByEvent(event, pageable);
	}

	@Override
	public List<EventAndVideos> findByEventAndIsPromotionalVideo(Event event) {
		return eventAndVideosRepository.findByEventAndIsPromotionalVideo(event);
	}

}
