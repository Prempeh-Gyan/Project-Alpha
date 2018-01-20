
package org.isag_ghana.alpha.repository;

import org.isag_ghana.alpha.model.Gruup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Gruup, Long> {
	@Query("SELECT g FROM Gruup g WHERE g.name = :groupName")
	public Gruup findByName(@Param("groupName") String groupName);
}
