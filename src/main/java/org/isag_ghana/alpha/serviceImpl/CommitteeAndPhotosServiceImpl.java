package org.isag_ghana.alpha.serviceImpl;

import java.util.List;

import org.isag_ghana.alpha.model.Committee;
import org.isag_ghana.alpha.model.CommitteeAndPhotos;
import org.isag_ghana.alpha.repository.CommitteeAndPhotosRepository;
import org.isag_ghana.alpha.service.CommitteeAndPhotosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommitteeAndPhotosServiceImpl implements CommitteeAndPhotosService {

	private final CommitteeAndPhotosRepository committeeAndPhotosRepository;

	@Autowired
	public CommitteeAndPhotosServiceImpl(CommitteeAndPhotosRepository committeeAndPhotosRepository) {
		this.committeeAndPhotosRepository = committeeAndPhotosRepository;
	}

	@Override
	public List<CommitteeAndPhotos> findAllByCommittee(Committee committee) {
		return committeeAndPhotosRepository.findAllByCommittee(committee);
	}

	@Override
	public CommitteeAndPhotos findById(Long committeeId) {
		return committeeAndPhotosRepository.findOne(committeeId);
	}

	@Override
	public void save(CommitteeAndPhotos committeeAndPhotos) {
		committeeAndPhotosRepository.save(committeeAndPhotos);
	}

	@Override
	public void save(List<CommitteeAndPhotos> committeeAndPhotos) {
		committeeAndPhotosRepository.save(committeeAndPhotos);
	}

	@Override
	public Page<CommitteeAndPhotos> findAllByCommittee(Committee committee, Pageable pageable) {
		return committeeAndPhotosRepository.findAllByCommittee(committee, pageable);
	}
}
