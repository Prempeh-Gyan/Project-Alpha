
package org.isag_ghana.alpha.repository;

import java.util.List;

import org.isag_ghana.alpha.model.Business;
import org.isag_ghana.alpha.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {
	@Query("SELECT b FROM Business b WHERE b.companyName = :companyName")
	public Business findByName(@Param("companyName") String companyName);

	@Query("SELECT b FROM Business b WHERE b.owner = :user")
	public List<Business> findAllByUser(@Param("user") User user);
}
