package org.isag_ghana.alpha.service;

import java.util.List;

import org.isag_ghana.alpha.model.Position;
import org.isag_ghana.alpha.model.PositionAndPositionHolder;
import org.isag_ghana.alpha.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PositionAndPositionHoldersService {

	public PositionAndPositionHolder findById(Long id);

	public void save(PositionAndPositionHolder positionAndPositionHolder);

	public void save(List<PositionAndPositionHolder> positionAndPositionHolders);

	public List<PositionAndPositionHolder> findAllPositionsByUser(User user);

	public PositionAndPositionHolder findByPositionName(String positionName);

	public List<PositionAndPositionHolder> findAllCurrentPositionsByUser(User user);

	public List<PositionAndPositionHolder> findAllCurrentPositions();

	public Page<PositionAndPositionHolder> findAllCurrentPositions(Pageable pageable);

	public List<PositionAndPositionHolder> findAllPositions();

	public List<PositionAndPositionHolder> findAllCurrentExecutivePositions();

	public List<PositionAndPositionHolder> findAllCurrentCommitteeLeaderPositions();

	public List<PositionAndPositionHolder> findAllByPosition(Position position);

	public List<PositionAndPositionHolder> findCurrentPositionHolderByPosition(Position position);

	public List<PositionAndPositionHolder> findAllCurrentPositionAndPositionHoldersForPosition(Position position);

}