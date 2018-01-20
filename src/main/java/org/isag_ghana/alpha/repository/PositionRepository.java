
package org.isag_ghana.alpha.repository;

import org.isag_ghana.alpha.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
	@Query("SELECT p FROM Position p WHERE p.name = :positionName")
	public Position findByName(@Param("positionName") String positionName);
}
