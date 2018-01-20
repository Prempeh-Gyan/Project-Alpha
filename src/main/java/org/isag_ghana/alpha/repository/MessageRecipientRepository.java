
package org.isag_ghana.alpha.repository;

import java.util.List;

import org.isag_ghana.alpha.model.MessageAndRecipient;
import org.isag_ghana.alpha.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRecipientRepository extends JpaRepository<MessageAndRecipient, Long> {

	@Query("SELECT m FROM MessageAndRecipient m WHERE m.recipient = :user AND m.isDeleted = false ORDER BY m.date DESC")
	public List<MessageAndRecipient> findAllMessagesByUser(@Param("user") User user);

	@Query("SELECT m FROM MessageAndRecipient m WHERE m.recipient = :user AND m.isDeleted = false AND m.isRead = false ORDER BY m.date DESC")
	public List<MessageAndRecipient> findAllUnreadMessagesByUser(@Param("user") User user);

	@Query("SELECT m FROM MessageAndRecipient m WHERE m.recipient = :user AND m.isDeleted = false ORDER BY m.date DESC")
	public Page<MessageAndRecipient> findAllMessagesByUser(@Param("user") User user, Pageable pageable);

}
