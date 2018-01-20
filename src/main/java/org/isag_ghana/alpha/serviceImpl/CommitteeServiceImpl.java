package org.isag_ghana.alpha.serviceImpl;

import java.util.List;

import org.isag_ghana.alpha.model.Committee;
import org.isag_ghana.alpha.repository.CommitteeRepository;
import org.isag_ghana.alpha.service.CommitteeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommitteeServiceImpl implements CommitteeService {

	private final CommitteeRepository committeeRepository;

	@Autowired
	public CommitteeServiceImpl(CommitteeRepository committeeRepository) {
		this.committeeRepository = committeeRepository;
	}

	@Override
	public List<Committee> findAll() {
		return committeeRepository.findAll();
	}

	@Override
	public Page<Committee> findAll(Pageable pageable) {
		return committeeRepository.findAll(pageable);
	}

	@Override
	public Committee findById(Long id) {
		return committeeRepository.findOne(id);
	}

	@Override
	public Committee findByName(String name) {
		return committeeRepository.findByName(name);
	}

	@Override
	public void save(Committee t) {
		committeeRepository.save(t);
	}

}
