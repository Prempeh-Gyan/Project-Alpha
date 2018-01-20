package org.isag_ghana.alpha.service;

import java.util.List;

import org.isag_ghana.alpha.model.Gruup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GroupService {

	public List<Gruup> findAll();

	public Page<Gruup> findAll(Pageable pageable);

	public Gruup findById(Long groupId);

	public Gruup findByName(String groupName);

	public void save(Gruup group);

}