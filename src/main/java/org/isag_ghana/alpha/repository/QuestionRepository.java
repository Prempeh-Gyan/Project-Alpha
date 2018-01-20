
package org.isag_ghana.alpha.repository;

import java.util.List;

import org.isag_ghana.alpha.model.Question;
import org.isag_ghana.alpha.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

	@Query("SELECT q FROM Question q WHERE q.owner = :user ORDER BY q.date ASC")
	public Page<Question> findAllQuestionsByUser(@Param("user") User user, Pageable pageable);

	@Query("SELECT q FROM Question q WHERE q.owner = :user ORDER BY q.date ASC")
	public List<Question> findAllQuestionsByUser(@Param("user") User user);
}
