package org.isag_ghana.alpha.service;

import java.util.List;

import org.isag_ghana.alpha.model.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProfileService {

	public void save(Profile profile);

	public List<Profile> findAll();

	public Profile findOne(long id);

	public void delete(Profile profile);

	public Page<Profile> findAll(Pageable pageable);

}