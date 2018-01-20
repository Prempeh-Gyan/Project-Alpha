
package org.isag_ghana.alpha.repository;

import java.util.List;

import org.isag_ghana.alpha.model.Committee;
import org.isag_ghana.alpha.model.CommitteeAndVideos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommitteeAndVideosRepository extends JpaRepository<CommitteeAndVideos, Long> {

	@Query("SELECT c FROM CommitteeAndVideos c WHERE c.committee = :committee ORDER BY c.date DESC")
	public List<CommitteeAndVideos> findAllByCommittee(@Param("committee") Committee committee);

	@Query("SELECT c FROM CommitteeAndVideos c WHERE c.committee = :committee ORDER BY c.date DESC")
	public Page<CommitteeAndVideos> findAllByCommittee(@Param("committee") Committee committee, Pageable pageable);

}
