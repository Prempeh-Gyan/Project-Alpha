
package org.isag_ghana.alpha.repository;

import java.util.List;

import org.isag_ghana.alpha.model.Event;
import org.isag_ghana.alpha.model.EventAndPhotos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventAndPhotosRepository extends JpaRepository<EventAndPhotos, Long> {

	@Query("SELECT ep FROM EventAndPhotos ep WHERE ep.event = :event ORDER BY ep.date DESC")
	public List<EventAndPhotos> findAllByEvent(@Param("event") Event event);

	@Query("SELECT ep FROM EventAndPhotos ep WHERE ep.event = :event ORDER BY ep.date DESC")
	public Page<EventAndPhotos> findAllByEvent(@Param("event") Event event, Pageable pageable);

	@Query("SELECT ep FROM EventAndPhotos ep WHERE ep.event = :event AND ep.isCoverPicture = true")
	public List<EventAndPhotos> findByEventAndIsCoverPicture(@Param("event") Event event);

}
