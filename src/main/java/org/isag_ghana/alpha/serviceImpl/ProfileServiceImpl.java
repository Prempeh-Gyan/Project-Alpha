package org.isag_ghana.alpha.serviceImpl;

import java.util.List;

import org.isag_ghana.alpha.model.Profile;
import org.isag_ghana.alpha.repository.ProfileRepository;
import org.isag_ghana.alpha.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProfileServiceImpl implements ProfileService {

	private final ProfileRepository profileRepository;

	@Autowired
	public ProfileServiceImpl(ProfileRepository profileRepository) {
		this.profileRepository = profileRepository;
	}

	@Override
	public void save(Profile profile) {
		profileRepository.save(profile);
	}

	@Override
	public List<Profile> findAll() {
		return profileRepository.findAll();
	}

	@Override
	public Profile findOne(long id) {
		return profileRepository.findOne(id);
	}

	@Override
	public void delete(Profile profile) {
		profileRepository.delete(profile);
	}

	@Override
	public Page<Profile> findAll(Pageable pageable) {
		return profileRepository.findAll(pageable);
	}
}
