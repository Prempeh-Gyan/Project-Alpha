
package org.isag_ghana.alpha.repository;

import java.util.List;

import org.isag_ghana.alpha.model.AdvertisingCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvertRepository extends JpaRepository<AdvertisingCompany, Long> {

	@Query("SELECT a FROM AdvertisingCompany a WHERE a.isNonExpired = :isNonExpired")
	public List<AdvertisingCompany> findByIsNonExpired(@Param("isNonExpired") Boolean isNonExpired);

	@Query("SELECT a FROM AdvertisingCompany a WHERE a.companyName = :companyName")
	public List<AdvertisingCompany> findByCompanyName(@Param("companyName") String companyName);

	@Query("SELECT a FROM AdvertisingCompany a WHERE a.advertCode LIKE (:advertCode) AND a.expiryDate <= NOW()")
	public List<AdvertisingCompany> findByAdvertCode(@Param("advertCode") String advertCode);
}
