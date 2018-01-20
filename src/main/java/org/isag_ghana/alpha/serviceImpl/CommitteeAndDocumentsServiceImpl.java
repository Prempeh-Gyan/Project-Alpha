package org.isag_ghana.alpha.serviceImpl;

import java.util.List;

import org.isag_ghana.alpha.model.Committee;
import org.isag_ghana.alpha.model.CommitteeAndDocuments;
import org.isag_ghana.alpha.repository.CommitteeAndDocumentsRepository;
import org.isag_ghana.alpha.service.CommitteeAndDocumentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommitteeAndDocumentsServiceImpl implements CommitteeAndDocumentsService {

	private final CommitteeAndDocumentsRepository committeeAndDocumentsRepository;

	@Autowired
	public CommitteeAndDocumentsServiceImpl(CommitteeAndDocumentsRepository committeeAndDocumentsRepository) {
		this.committeeAndDocumentsRepository = committeeAndDocumentsRepository;
	}

	@Override
	public List<CommitteeAndDocuments> findAllByCommittee(Committee committee) {
		return committeeAndDocumentsRepository.findAllByCommittee(committee);
	}

	@Override
	public CommitteeAndDocuments findById(Long committeeId) {
		return committeeAndDocumentsRepository.findOne(committeeId);
	}

	@Override
	public void save(CommitteeAndDocuments committeeAndDocuments) {
		committeeAndDocumentsRepository.save(committeeAndDocuments);
	}

	@Override
	public void save(List<CommitteeAndDocuments> committeeAndDocuments) {
		committeeAndDocumentsRepository.save(committeeAndDocuments);
	}

	@Override
	public Page<CommitteeAndDocuments> findAllByCommittee(Committee committee, Pageable pageable) {
		return committeeAndDocumentsRepository.findAllByCommittee(committee, pageable);
	}

	@Override
	public Page<CommitteeAndDocuments> findAllConfidentialByCommittee(Committee committee, Pageable pageable) {
		return committeeAndDocumentsRepository.findAllConfidentialByCommittee(committee, pageable);
	}

	@Override
	public Page<CommitteeAndDocuments> findAllNonConfidentialByCommittee(Committee committee, Pageable pageable) {
		return committeeAndDocumentsRepository.findAllNonConfidentialByCommittee(committee, pageable);
	}

	@Override
	public Page<CommitteeAndDocuments> findAllMinutesByCommittee(Committee committee, Pageable pageable) {
		return committeeAndDocumentsRepository.findAllMinutesByCommittee(committee, pageable);
	}

	@Override
	public Page<CommitteeAndDocuments> findAllReportsByCommittee(Committee committee, Pageable pageable) {
		return committeeAndDocumentsRepository.findAllReportsByCommittee(committee, pageable);
	}
}
