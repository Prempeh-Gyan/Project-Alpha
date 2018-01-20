
package org.isag_ghana.alpha.repository;

import org.isag_ghana.alpha.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	@Query("SELECT r FROM Role r WHERE r.name = :name")
	public Role findOneByName(@Param("name") String name);
}
