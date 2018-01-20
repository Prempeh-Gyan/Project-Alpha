
package org.isag_ghana.alpha.repository;

import java.util.List;

import org.isag_ghana.alpha.model.Business;
import org.isag_ghana.alpha.model.BusinessAndPhotos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessAndPhotosRepository extends JpaRepository<BusinessAndPhotos, Long> {

	@Query("SELECT bp FROM BusinessAndPhotos bp WHERE bp.business = :business ORDER BY bp.date DESC")
	public List<BusinessAndPhotos> findAllByBusiness(@Param("business") Business business);

	@Query("SELECT bp FROM BusinessAndPhotos bp WHERE bp.business = :business ORDER BY bp.date DESC")
	public Page<BusinessAndPhotos> findAllByBusiness(@Param("business") Business business, Pageable pageable);

	@Query("SELECT bp FROM BusinessAndPhotos bp WHERE bp.business = :business AND bp.isCoverPicture = true")
	public List<BusinessAndPhotos> findBusinessAndBusinessPhotoByBusinessAndIsCoverPicture(
			@Param("business") Business business);

	@Query("SELECT bp FROM BusinessAndPhotos bp WHERE bp.business = :business AND bp.isCoverPicture = false")
	public List<BusinessAndPhotos> findBusinessAndBusinessPhotoByBusinessAndIsNotCoverPicture(
			@Param("business") Business business);

	@Query("DELETE FROM BusinessAndPhotos bp WHERE bp.business = :business")
	public List<BusinessAndPhotos> deleteAllByBusiness(@Param("business") Business business);

}
