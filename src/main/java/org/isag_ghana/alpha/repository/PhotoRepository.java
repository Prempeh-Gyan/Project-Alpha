
package org.isag_ghana.alpha.repository;

import org.isag_ghana.alpha.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

	@Query("SELECT p FROM Photo p WHERE p.name = :name")
	public Photo findOne(@Param("name") String name);
}
