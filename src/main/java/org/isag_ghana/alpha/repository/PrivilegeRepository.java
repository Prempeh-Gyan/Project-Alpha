
package org.isag_ghana.alpha.repository;

import org.isag_ghana.alpha.model.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

	@Query("SELECT p FROM Privilege p WHERE p.name = :name")
	public Privilege fineOneByName(@Param("name") String name);
}
