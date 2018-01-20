
package org.isag_ghana.alpha.repository;

import org.isag_ghana.alpha.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

	@Query("SELECT d FROM Document d WHERE d.name = :name")
	public Document findOne(@Param("name") String name);
}
