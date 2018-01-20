package org.isag_ghana.alpha.serviceImpl;

import java.util.List;

import org.isag_ghana.alpha.model.Position;
import org.isag_ghana.alpha.repository.PositionRepository;
import org.isag_ghana.alpha.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PositionServiceImpl implements PositionService {

	private final PositionRepository positionRepository;

	@Autowired
	public PositionServiceImpl(PositionRepository positionRepository) {
		this.positionRepository = positionRepository;
	}

	@Override
	public Position findById(Long id) {
		return positionRepository.findOne(id);
	}

	@Override
	public Position findByName(String positionName) {
		return positionRepository.findByName(positionName);
	}

	@Override
	public void save(Position position) {
		positionRepository.save(position);
	}

	@Override
	public List<Position> findAllPositions() {
		return positionRepository.findAll();
	}

}
