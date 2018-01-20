
package org.isag_ghana.alpha.repository;

import java.util.List;

import org.isag_ghana.alpha.model.Event;
import org.isag_ghana.alpha.model.EventAndVideos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventAndVideosRepository extends JpaRepository<EventAndVideos, Long> {

	@Query("SELECT ev FROM EventAndVideos ev WHERE ev.event = :event ORDER BY ev.date DESC")
	public List<EventAndVideos> findAllByEvent(@Param("event") Event event);

	@Query("SELECT ev FROM EventAndVideos ev WHERE ev.event = :event AND ev.isPromotionalVideo = true ORDER BY ev.date DESC")
	public List<EventAndVideos> findByEventAndIsPromotionalVideo(@Param("event") Event event);

	@Query("SELECT ev FROM EventAndVideos ev WHERE ev.event = :event ORDER BY ev.date DESC")
	public Page<EventAndVideos> findAllByEvent(@Param("event") Event event, Pageable pageable);

}
