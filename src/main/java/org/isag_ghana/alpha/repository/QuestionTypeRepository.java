
package org.isag_ghana.alpha.repository;

import org.isag_ghana.alpha.model.QuestionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionTypeRepository extends JpaRepository<QuestionType, Long> {

	@Query("SELECT q FROM QuestionType q WHERE q.name = :questionTypeName")
	public QuestionType findOne(@Param("questionTypeName") String questionTypeName);

}
