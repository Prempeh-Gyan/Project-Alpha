package org.isag_ghana.alpha.service;

import java.util.List;

import org.isag_ghana.alpha.model.Committee;
import org.isag_ghana.alpha.model.CommitteeAndPhotos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommitteeAndPhotosService {

	public List<CommitteeAndPhotos> findAllByCommittee(Committee committee);

	public CommitteeAndPhotos findById(Long committeeId);

	public void save(CommitteeAndPhotos committeeAndPhotos);

	public void save(List<CommitteeAndPhotos> committeeAndPhotos);

	public Page<CommitteeAndPhotos> findAllByCommittee(Committee committee, Pageable pageable);

}