
package org.isag_ghana.alpha.repository;

import org.isag_ghana.alpha.model.Committee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommitteeRepository extends JpaRepository<Committee, Long> {
	@Query("SELECT c FROM Committee c WHERE c.name = :committeeName")
	public Committee findByName(@Param("committeeName") String committeeName);
}
