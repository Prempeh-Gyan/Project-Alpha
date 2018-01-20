package org.isag_ghana.alpha.serviceImpl;

import org.isag_ghana.alpha.model.Photo;
import org.isag_ghana.alpha.repository.PhotoRepository;
import org.isag_ghana.alpha.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PhotoServiceImpl implements PhotoService {

	private final PhotoRepository phototRepository;

	@Autowired
	public PhotoServiceImpl(PhotoRepository phototRepository) {
		this.phototRepository = phototRepository;
	}

	@Override
	public Photo findOne(long photoId) {
		return phototRepository.findOne(photoId);
	}

	@Override
	public void save(Photo photo) {
		phototRepository.save(photo);
	}

	@Override
	public Photo findOne(String name) {
		return phototRepository.findOne(name);
	}
}
