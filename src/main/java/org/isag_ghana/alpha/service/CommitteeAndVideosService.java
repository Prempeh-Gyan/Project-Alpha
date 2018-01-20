package org.isag_ghana.alpha.service;

import java.util.List;

import org.isag_ghana.alpha.model.Committee;
import org.isag_ghana.alpha.model.CommitteeAndVideos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommitteeAndVideosService {

	public List<CommitteeAndVideos> findAllByCommittee(Committee committee);

	public CommitteeAndVideos findById(Long committeeId);

	public void save(CommitteeAndVideos committeeAndVideos);

	public void save(List<CommitteeAndVideos> committeeAndVideos);

	public Page<CommitteeAndVideos> findAllByCommittee(Committee committee, Pageable pageable);

}