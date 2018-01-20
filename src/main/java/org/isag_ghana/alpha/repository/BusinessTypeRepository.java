
package org.isag_ghana.alpha.repository;

import org.isag_ghana.alpha.model.BusinessType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessTypeRepository extends JpaRepository<BusinessType, Long> {

	@Query("SELECT b FROM BusinessType b WHERE b.name = :businessTypeName")
	public BusinessType findOne(@Param("businessTypeName") String businessTypeName);

}
