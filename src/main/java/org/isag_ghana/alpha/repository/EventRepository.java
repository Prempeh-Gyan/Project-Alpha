
package org.isag_ghana.alpha.repository;

import java.util.List;

import org.isag_ghana.alpha.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

	@Query("SELECT e FROM Event e WHERE e.isISAGEvent = true ORDER BY e.startTime DESC")
	public List<Event> findAllISAGEvents();

	@Query("SELECT e FROM Event e WHERE e.isISAGEvent = true ORDER BY e.startTime DESC")
	public Page<Event> findAllISAGEvents(Pageable pageable);

	@Query("SELECT e FROM Event e ORDER BY e.startTime DESC")
	public Page<Event> findAllEventsSorted(Pageable pageable);

	@Query("SELECT e FROM Event e WHERE e.isMembersEvent = true ORDER BY e.startTime DESC")
	public List<Event> findAllMembersEvents();

	@Query("SELECT e FROM Event e WHERE e.startTime > CURRENT_TIMESTAMP() ORDER BY e.startTime ASC")
	public List<Event> findAllUpComingEvents();

	@Query("SELECT e FROM Event e WHERE e.isISAGEvent = true AND e.startTime > CURRENT_TIMESTAMP() ORDER BY e.startTime ASC")
	public List<Event> findAllUpComingISAGEvents();

	@Query("SELECT e FROM Event e WHERE e.isMembersEvent = true AND e.startTime > CURRENT_TIMESTAMP() ORDER BY e.startTime ASC")
	public List<Event> findAllUpComingMembersEvents();

	@Query("SELECT e FROM Event e WHERE e.isISAGEvent = true AND e.startTime > CURRENT_TIMESTAMP() ORDER BY e.startTime ASC")
	public List<Event> findNextISAGEvent();

	@Query("SELECT e FROM Event e WHERE e.isMembersEvent = true AND e.startTime > CURRENT_TIMESTAMP() ORDER BY e.startTime ASC")
	public List<Event> findNextMembersEvent();

	@Query("SELECT e FROM Event e WHERE e.isISAGEvent = true AND e.endTime < CURRENT_TIMESTAMP() ORDER BY e.endTime DESC")
	public List<Event> findJustEndedISAGEvent();

}
