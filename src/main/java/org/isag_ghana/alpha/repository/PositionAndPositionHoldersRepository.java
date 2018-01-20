
package org.isag_ghana.alpha.repository;

import java.util.List;

import org.isag_ghana.alpha.model.Position;
import org.isag_ghana.alpha.model.PositionAndPositionHolder;
import org.isag_ghana.alpha.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionAndPositionHoldersRepository extends JpaRepository<PositionAndPositionHolder, Long> {

	@Query("SELECT ph FROM PositionAndPositionHolder ph WHERE ph.positionHolder = :user ORDER BY ph.startDate DESC")
	public List<PositionAndPositionHolder> findAllPositionsByUser(@Param("user") User user);

	@Query("SELECT ph FROM PositionAndPositionHolder ph WHERE ph.positionHolder = :user AND ph.isTenureExpired = false ORDER BY ph.startDate DESC")
	public List<PositionAndPositionHolder> findAllCurrentPositionsByUser(@Param("user") User user);

	@Query("SELECT ph FROM PositionAndPositionHolder ph WHERE ph.isTenureExpired = false")
	public List<PositionAndPositionHolder> findAllCurrentPositions();

	@Query("SELECT ph FROM PositionAndPositionHolder ph WHERE ph.isTenureExpired = false")
	public Page<PositionAndPositionHolder> findAllCurrentPositions(Pageable pageable);

	@Query("SELECT ph FROM PositionAndPositionHolder ph")
	public List<PositionAndPositionHolder> findAllPositions();

	@Query("SELECT ph FROM PositionAndPositionHolder ph WHERE ph.isExecutivePosition = true AND ph.isTenureExpired = false")
	public List<PositionAndPositionHolder> findAllCurrentExecutivePositions();

	@Query("SELECT ph FROM PositionAndPositionHolder ph WHERE ph.isCommitteePosition = true AND ph.isHeadPosition = true AND ph.isTenureExpired = false")
	public List<PositionAndPositionHolder> findAllCurrentCommitteeLeaderPositions();

	@Query("SELECT ph FROM PositionAndPositionHolder ph WHERE ph.position = :position")
	public List<PositionAndPositionHolder> findAllByPosition(@Param("position") Position position);

	@Query("SELECT ph FROM PositionAndPositionHolder ph WHERE ph.position = :position AND ph.isTenureExpired = false")
	public List<PositionAndPositionHolder> findCurrentPositionHolderByPosition(@Param("position") Position position);

	@Query("SELECT ph FROM PositionAndPositionHolder ph WHERE ph.position = :position AND ph.isTenureExpired = false")
	public List<PositionAndPositionHolder> findAllCurrentPositionAndPositionHoldersForPosition(
			@Param("position") Position position);
	
	@Query("SELECT ph FROM PositionAndPositionHolder ph WHERE ph.position.name = :positionName AND ph.isTenureExpired = false")
	public PositionAndPositionHolder findByPositionName(@Param("positionName") String positionName);

}
