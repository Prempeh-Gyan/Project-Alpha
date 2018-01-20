
package org.isag_ghana.alpha.repository;

import java.util.List;

import org.isag_ghana.alpha.model.Product;
import org.isag_ghana.alpha.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	@Query("SELECT p FROM Product p WHERE p.isAvailable = :isAvailable")
	public List<Product> findAllAvailableProducts(@Param("isAvailable") boolean isAvailable);

	@Query("SELECT p FROM Product p WHERE p.isAvailable = TRUE")
	public Page<Product> findAll(Pageable pageable);

	@Query("SELECT p FROM Product p WHERE p.isAvailable = TRUE AND p.owner = :user ORDER BY p.date ASC")
	public Page<Product> findAllProductsByUser(@Param("user") User user, Pageable pageable);

	@Query("SELECT p FROM Product p WHERE p.isAvailable = TRUE AND p.owner = :user ORDER BY p.date ASC")
	public List<Product> findAllProductsByUser(@Param("user") User user);
}
