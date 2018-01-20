package org.isag_ghana.alpha.serviceImpl;

import java.util.List;

import org.isag_ghana.alpha.model.Business;
import org.isag_ghana.alpha.model.BusinessAndVideos;
import org.isag_ghana.alpha.repository.BusinessAndVideosRepository;
import org.isag_ghana.alpha.service.BusinessAndVideosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BusinessAndVideosServiceImpl implements BusinessAndVideosService {

	private final BusinessAndVideosRepository businessAndVideosRepository;

	@Autowired
	public BusinessAndVideosServiceImpl(BusinessAndVideosRepository businessAndVideosRepository) {
		this.businessAndVideosRepository = businessAndVideosRepository;
	}

	@Override
	public List<BusinessAndVideos> findAllByBusiness(Business business) {
		return businessAndVideosRepository.findAllByBusiness(business);
	}

	@Override
	public BusinessAndVideos findById(Long businessId) {
		return businessAndVideosRepository.findOne(businessId);
	}

	@Override
	public void save(BusinessAndVideos businessAndVideos) {
		businessAndVideosRepository.save(businessAndVideos);
	}

	@Override
	public void save(List<BusinessAndVideos> businessAndVideos) {
		businessAndVideosRepository.save(businessAndVideos);
	}

	@Override
	public Page<BusinessAndVideos> findAllByBusiness(Business business, Pageable pageable) {
		return businessAndVideosRepository.findAllByBusiness(business, pageable);
	}

	@Override
	public void delete(Long businessAndVideosId) {
		businessAndVideosRepository.delete(businessAndVideosId);
	}

	@Override
	public void delete(BusinessAndVideos businessAndVideosId) {
		businessAndVideosRepository.delete(businessAndVideosId);
	}

	@Override
	public List<BusinessAndVideos> deleteAllByBusiness(Business business) {
		return businessAndVideosRepository.deleteAllByBusiness(business);
	}

}
