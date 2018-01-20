package org.isag_ghana.alpha.serviceImpl;

import java.util.List;

import org.isag_ghana.alpha.model.Business;
import org.isag_ghana.alpha.model.BusinessAndPhotos;
import org.isag_ghana.alpha.repository.BusinessAndPhotosRepository;
import org.isag_ghana.alpha.service.BusinessAndPhotosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BusinessAndPhotosServiceImpl implements BusinessAndPhotosService {

	private final BusinessAndPhotosRepository businessAndPhotosRepository;

	@Autowired
	public BusinessAndPhotosServiceImpl(BusinessAndPhotosRepository businessAndPhotosRepository) {
		this.businessAndPhotosRepository = businessAndPhotosRepository;
	}

	@Override
	public List<BusinessAndPhotos> findAllByBusiness(Business business) {
		return businessAndPhotosRepository.findAllByBusiness(business);
	}

	@Override
	public BusinessAndPhotos findById(Long businessId) {
		return businessAndPhotosRepository.findOne(businessId);
	}

	@Override
	public void save(BusinessAndPhotos businessAndPhotos) {
		businessAndPhotosRepository.save(businessAndPhotos);
	}

	@Override
	public void save(List<BusinessAndPhotos> businessAndPhotos) {
		businessAndPhotosRepository.save(businessAndPhotos);
	}

	@Override
	public Page<BusinessAndPhotos> findAllByBusiness(Business business, Pageable pageable) {
		return businessAndPhotosRepository.findAllByBusiness(business, pageable);
	}

	@Override
	public List<BusinessAndPhotos> findBusinessAndBusinessPhotoByBusinessAndIsCoverPicture(Business business) {
		return businessAndPhotosRepository.findBusinessAndBusinessPhotoByBusinessAndIsCoverPicture(business);
	}

	@Override
	public List<BusinessAndPhotos> findBusinessAndBusinessPhotoByBusinessAndIsNotCoverPicture(Business business) {
		return businessAndPhotosRepository.findBusinessAndBusinessPhotoByBusinessAndIsNotCoverPicture(business);
	}

	@Override
	public void delete(Long businessAndPhotosId) {
		businessAndPhotosRepository.delete(businessAndPhotosId);
	}

	@Override
	public void delete(BusinessAndPhotos businessAndPhoto) {
		businessAndPhotosRepository.delete(businessAndPhoto);
	}

	@Override
	public List<BusinessAndPhotos> deleteAllByBusiness(Business business) {
		return businessAndPhotosRepository.deleteAllByBusiness(business);
	}
}
