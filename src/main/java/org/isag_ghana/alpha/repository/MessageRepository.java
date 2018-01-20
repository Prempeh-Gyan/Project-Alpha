
package org.isag_ghana.alpha.repository;

import java.util.List;

import org.isag_ghana.alpha.model.Message;
import org.isag_ghana.alpha.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

	@Query("SELECT m FROM Message m WHERE m.sender = :user ORDER BY m.date DESC")
	public List<Message> findAllMessagesByUser(@Param("user") User user);

	@Query("SELECT m FROM Message m WHERE m.sender = :user ORDER BY m.date DESC")
	public Page<Message> findAllMessagesByUser(@Param("user") User user, Pageable pageable);

}
