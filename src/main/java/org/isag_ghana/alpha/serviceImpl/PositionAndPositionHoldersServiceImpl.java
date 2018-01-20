package org.isag_ghana.alpha.serviceImpl;

import java.util.List;

import org.isag_ghana.alpha.model.Position;
import org.isag_ghana.alpha.model.PositionAndPositionHolder;
import org.isag_ghana.alpha.model.User;
import org.isag_ghana.alpha.repository.PositionAndPositionHoldersRepository;
import org.isag_ghana.alpha.service.PositionAndPositionHoldersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PositionAndPositionHoldersServiceImpl implements PositionAndPositionHoldersService {

	private final PositionAndPositionHoldersRepository positionAndPositionHoldersRepository;

	@Autowired
	public PositionAndPositionHoldersServiceImpl(
			PositionAndPositionHoldersRepository positionAndPositionHoldersRepository) {
		this.positionAndPositionHoldersRepository = positionAndPositionHoldersRepository;
	}

	@Override
	public PositionAndPositionHolder findById(Long id) {
		return positionAndPositionHoldersRepository.findOne(id);
	}

	@Override
	public void save(PositionAndPositionHolder positionAndPositionHolder) {
		positionAndPositionHoldersRepository.save(positionAndPositionHolder);
	}

	@Override
	public void save(List<PositionAndPositionHolder> positionAndPositionHolders) {
		positionAndPositionHoldersRepository.save(positionAndPositionHolders);
	}

	@Override
	public List<PositionAndPositionHolder> findAllPositionsByUser(User user) {
		return positionAndPositionHoldersRepository.findAllPositionsByUser(user);
	}

	@Override
	public List<PositionAndPositionHolder> findAllCurrentPositionsByUser(User user) {
		return positionAndPositionHoldersRepository.findAllCurrentPositionsByUser(user);
	}

	@Override
	public List<PositionAndPositionHolder> findAllCurrentPositions() {
		return positionAndPositionHoldersRepository.findAllCurrentPositions();
	}

	@Override
	public List<PositionAndPositionHolder> findAllPositions() {
		return positionAndPositionHoldersRepository.findAllPositions();
	}

	@Override
	public List<PositionAndPositionHolder> findAllCurrentExecutivePositions() {
		return positionAndPositionHoldersRepository.findAllCurrentExecutivePositions();
	}

	@Override
	public List<PositionAndPositionHolder> findAllCurrentCommitteeLeaderPositions() {
		return positionAndPositionHoldersRepository.findAllCurrentCommitteeLeaderPositions();
	}

	@Override
	public List<PositionAndPositionHolder> findAllByPosition(Position position) {
		return positionAndPositionHoldersRepository.findAllByPosition(position);
	}

	@Override
	public Page<PositionAndPositionHolder> findAllCurrentPositions(Pageable pageable) {
		return positionAndPositionHoldersRepository.findAllCurrentPositions(pageable);
	}

	@Override
	public List<PositionAndPositionHolder> findAllCurrentPositionAndPositionHoldersForPosition(Position position) {
		return positionAndPositionHoldersRepository.findAllCurrentPositionAndPositionHoldersForPosition(position);
	}

	@Override
	public List<PositionAndPositionHolder> findCurrentPositionHolderByPosition(Position position) {
		return positionAndPositionHoldersRepository.findCurrentPositionHolderByPosition(position);
	}

	@Override
	public PositionAndPositionHolder findByPositionName(String positionName) {
		return positionAndPositionHoldersRepository.findByPositionName(positionName);
	}

}
