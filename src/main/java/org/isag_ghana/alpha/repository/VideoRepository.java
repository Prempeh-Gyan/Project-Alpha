
package org.isag_ghana.alpha.repository;

import org.isag_ghana.alpha.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

	@Query("SELECT v FROM Video v WHERE v.name = :name")
	public Video findOne(@Param("name") String name);
}
