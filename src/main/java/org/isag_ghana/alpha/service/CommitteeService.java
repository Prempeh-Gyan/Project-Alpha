package org.isag_ghana.alpha.service;

import java.util.List;

import org.isag_ghana.alpha.model.Committee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommitteeService {

	List<Committee> findAll();

	Page<Committee> findAll(Pageable pageable);

	Committee findById(Long id);

	Committee findByName(String name);

	void save(Committee t);

}