package org.isag_ghana.alpha.serviceImpl;

import java.util.List;

import org.isag_ghana.alpha.model.Business;
import org.isag_ghana.alpha.model.User;
import org.isag_ghana.alpha.repository.BusinessRepository;
import org.isag_ghana.alpha.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BusinessServiceImpl implements BusinessService {

	private final BusinessRepository businessRepository;

	@Autowired
	public BusinessServiceImpl(BusinessRepository businessRepository) {
		this.businessRepository = businessRepository;
	}

	@Override
	public List<Business> findAll() {
		return businessRepository.findAll();
	}

	@Override
	public Page<Business> findAll(Pageable pageable) {
		return businessRepository.findAll(pageable);
	}

	@Override
	public Business findById(Long businessId) {
		return businessRepository.findOne(businessId);
	}

	@Override
	public Business findByName(String companyName) {
		return businessRepository.findByName(companyName);
	}

	@Override
	public void save(Business business) {
		businessRepository.save(business);
	}

	@Override
	public List<Business> findAllByUser(User user) {
		return businessRepository.findAllByUser(user);
	}

	@Override
	public void delete(Business business) {
		businessRepository.delete(business);
	}

}
