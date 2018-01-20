
package org.isag_ghana.alpha.repository;

import org.isag_ghana.alpha.model.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, Long> {

	@Query("SELECT p FROM ProductType p WHERE p.name = :productTypeName")
	public ProductType findOne(@Param("productTypeName") String productTypeName);

}
