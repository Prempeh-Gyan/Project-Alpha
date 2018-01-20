package org.isag_ghana.alpha.service;

import java.util.List;

import org.isag_ghana.alpha.model.Committee;
import org.isag_ghana.alpha.model.CommitteeAndDocuments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommitteeAndDocumentsService {

	public List<CommitteeAndDocuments> findAllByCommittee(Committee committee);

	public CommitteeAndDocuments findById(Long committeeId);

	public void save(CommitteeAndDocuments committeeAndDocuments);

	public void save(List<CommitteeAndDocuments> committeeAndDocuments);

	public Page<CommitteeAndDocuments> findAllByCommittee(Committee committee, Pageable pageable);

	public Page<CommitteeAndDocuments> findAllConfidentialByCommittee(Committee committee, Pageable pageable);

	public Page<CommitteeAndDocuments> findAllNonConfidentialByCommittee(Committee committee, Pageable pageable);

	public Page<CommitteeAndDocuments> findAllMinutesByCommittee(Committee committee, Pageable pageable);

	public Page<CommitteeAndDocuments> findAllReportsByCommittee(Committee committee, Pageable pageable);

}