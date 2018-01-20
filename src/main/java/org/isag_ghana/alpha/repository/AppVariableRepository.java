
package org.isag_ghana.alpha.repository;

import org.isag_ghana.alpha.model.AppVariable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppVariableRepository extends JpaRepository<AppVariable, Long> {

	@Query("SELECT a FROM AppVariable a WHERE a.name = :name")
	public AppVariable findByName(@Param("name") String name);
}