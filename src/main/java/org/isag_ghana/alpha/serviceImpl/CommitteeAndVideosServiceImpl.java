package org.isag_ghana.alpha.serviceImpl;

import java.util.List;

import org.isag_ghana.alpha.model.Committee;
import org.isag_ghana.alpha.model.CommitteeAndVideos;
import org.isag_ghana.alpha.repository.CommitteeAndVideosRepository;
import org.isag_ghana.alpha.service.CommitteeAndVideosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommitteeAndVideosServiceImpl implements CommitteeAndVideosService {

	private final CommitteeAndVideosRepository committeeAndVideosRepository;

	@Autowired
	public CommitteeAndVideosServiceImpl(CommitteeAndVideosRepository committeeAndVideosRepository) {
		this.committeeAndVideosRepository = committeeAndVideosRepository;
	}

	@Override
	public List<CommitteeAndVideos> findAllByCommittee(Committee committee) {
		return committeeAndVideosRepository.findAllByCommittee(committee);
	}

	@Override
	public CommitteeAndVideos findById(Long committeeId) {
		return committeeAndVideosRepository.findOne(committeeId);
	}

	@Override
	public void save(CommitteeAndVideos committeeAndVideos) {
		committeeAndVideosRepository.save(committeeAndVideos);
	}

	@Override
	public void save(List<CommitteeAndVideos> committeeAndVideos) {
		committeeAndVideosRepository.save(committeeAndVideos);
	}

	@Override
	public Page<CommitteeAndVideos> findAllByCommittee(Committee committee, Pageable pageable) {
		return committeeAndVideosRepository.findAllByCommittee(committee, pageable);
	}
}
