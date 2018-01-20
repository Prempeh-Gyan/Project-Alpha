package org.isag_ghana.alpha.service;

import java.util.List;

import org.isag_ghana.alpha.model.Event;
import org.isag_ghana.alpha.model.EventAndPhotos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventAndPhotosService {

	public List<EventAndPhotos> findAllByEvent(Event event);

	public List<EventAndPhotos> findByEventAndIsCoverPicture(Event event);

	public EventAndPhotos findById(Long eventId);

	public void save(EventAndPhotos eventAndPhotos);

	public void save(List<EventAndPhotos> eventAndPhotos);

	public Page<EventAndPhotos> findAllByEvent(Event event, Pageable pageable);

}