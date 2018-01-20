package org.isag_ghana.alpha.serviceImpl;

import java.util.List;

import org.isag_ghana.alpha.model.Event;
import org.isag_ghana.alpha.model.EventAndPhotos;
import org.isag_ghana.alpha.repository.EventAndPhotosRepository;
import org.isag_ghana.alpha.service.EventAndPhotosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EventAndPhotosServiceImpl implements EventAndPhotosService {

	private final EventAndPhotosRepository eventAndPhotosRepository;

	@Autowired
	public EventAndPhotosServiceImpl(EventAndPhotosRepository eventAndPhotosRepository) {
		this.eventAndPhotosRepository = eventAndPhotosRepository;
	}

	@Override
	public List<EventAndPhotos> findAllByEvent(Event event) {
		return eventAndPhotosRepository.findAllByEvent(event);
	}

	@Override
	public List<EventAndPhotos> findByEventAndIsCoverPicture(Event event) {
		return eventAndPhotosRepository.findByEventAndIsCoverPicture(event);
	}

	@Override
	public EventAndPhotos findById(Long eventId) {
		return eventAndPhotosRepository.findOne(eventId);
	}

	@Override
	public void save(EventAndPhotos eventAndPhotos) {
		eventAndPhotosRepository.save(eventAndPhotos);
	}

	@Override
	public void save(List<EventAndPhotos> eventAndPhotos) {
		eventAndPhotosRepository.save(eventAndPhotos);
	}

	@Override
	public Page<EventAndPhotos> findAllByEvent(Event event, Pageable pageable) {
		return eventAndPhotosRepository.findAllByEvent(event, pageable);
	}

}
