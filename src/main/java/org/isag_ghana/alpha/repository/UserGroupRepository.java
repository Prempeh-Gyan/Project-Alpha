
package org.isag_ghana.alpha.repository;

import java.util.List;

import org.isag_ghana.alpha.model.Gruup;
import org.isag_ghana.alpha.model.User;
import org.isag_ghana.alpha.model.User_Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGroupRepository extends JpaRepository<User_Group, Long> {

	@Query("SELECT g FROM User_Group g WHERE g.user = :user")
	public List<User_Group> findByUser(@Param("user") User user);

	@Query("SELECT g FROM User_Group g WHERE g.group = :group")
	public List<User_Group> findByGroup(@Param("group") Gruup group);

	@Query("SELECT g FROM User_Group g WHERE g.group = :group AND g.user = :user")
	public List<User_Group> findByGroupAndUser(@Param("group") Gruup group, @Param("user") User user);
}
