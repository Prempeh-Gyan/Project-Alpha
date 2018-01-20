
package org.isag_ghana.alpha.repository;

import java.util.List;

import org.isag_ghana.alpha.model.Committee;
import org.isag_ghana.alpha.model.CommitteeAndDocuments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommitteeAndDocumentsRepository extends JpaRepository<CommitteeAndDocuments, Long> {

	@Query("SELECT c FROM CommitteeAndDocuments c WHERE c.committee = :committee ORDER BY c.date DESC")
	public List<CommitteeAndDocuments> findAllByCommittee(@Param("committee") Committee committee);

	@Query("SELECT c FROM CommitteeAndDocuments c WHERE c.committee = :committee ORDER BY c.date DESC")
	public Page<CommitteeAndDocuments> findAllByCommittee(@Param("committee") Committee committee, Pageable pageable);

	@Query("SELECT c FROM CommitteeAndDocuments c WHERE c.committee = :committee AND c.isConfidential = true ORDER BY c.date DESC")
	public Page<CommitteeAndDocuments> findAllConfidentialByCommittee(@Param("committee") Committee committee,
			Pageable pageable);

	@Query("SELECT c FROM CommitteeAndDocuments c WHERE c.committee = :committee AND c.isConfidential = false ORDER BY c.date DESC")
	public Page<CommitteeAndDocuments> findAllNonConfidentialByCommittee(@Param("committee") Committee committee,
			Pageable pageable);

	@Query("SELECT c FROM CommitteeAndDocuments c WHERE c.committee = :committee AND c.isMinute = true ORDER BY c.date DESC")
	public Page<CommitteeAndDocuments> findAllMinutesByCommittee(@Param("committee") Committee committee,
			Pageable pageable);

	@Query("SELECT c FROM CommitteeAndDocuments c WHERE c.committee = :committee AND c.isReport = true ORDER BY c.date DESC")
	public Page<CommitteeAndDocuments> findAllReportsByCommittee(@Param("committee") Committee committee,
			Pageable pageable);
}
