package org.isag_ghana.alpha.service;

import java.util.List;

import org.isag_ghana.alpha.model.Position;

public interface PositionService {

	Position findById(Long id);

	List<Position> findAllPositions();

	Position findByName(String positionName);

	void save(Position position);

}