package org.isag_ghana.alpha.service;

import java.util.List;

import org.isag_ghana.alpha.model.Event;
import org.isag_ghana.alpha.model.EventAndVideos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventAndVideosService {

	public List<EventAndVideos> findAllByEvent(Event event);
	
	public List<EventAndVideos> findByEventAndIsPromotionalVideo(Event event);

	public EventAndVideos findById(Long eventId);

	public void save(EventAndVideos eventAndVideos);

	public void save(List<EventAndVideos> eventAndVideos);

	public Page<EventAndVideos> findAllByEvent(Event event, Pageable pageable);

}