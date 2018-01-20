
package org.isag_ghana.alpha.repository;

import java.util.List;

import org.isag_ghana.alpha.model.Business;
import org.isag_ghana.alpha.model.BusinessAndVideos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessAndVideosRepository extends JpaRepository<BusinessAndVideos, Long> {

	@Query("SELECT bv FROM BusinessAndVideos bv WHERE bv.business = :business ORDER BY bv.date DESC")
	public List<BusinessAndVideos> findAllByBusiness(@Param("business") Business business);

	@Query("SELECT bv FROM BusinessAndVideos bv WHERE bv.business = :business ORDER BY bv.date DESC")
	public Page<BusinessAndVideos> findAllByBusiness(@Param("business") Business business, Pageable pageable);

	@Query("DELETE FROM BusinessAndVideos bv WHERE bv.business = :business")
	public List<BusinessAndVideos> deleteAllByBusiness(@Param("business") Business business);

}
