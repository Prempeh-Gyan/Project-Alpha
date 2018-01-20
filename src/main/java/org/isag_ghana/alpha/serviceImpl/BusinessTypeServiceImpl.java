package org.isag_ghana.alpha.serviceImpl;

import java.util.List;

import org.isag_ghana.alpha.model.BusinessType;
import org.isag_ghana.alpha.repository.BusinessTypeRepository;
import org.isag_ghana.alpha.service.BusinessTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BusinessTypeServiceImpl implements BusinessTypeService {

	private final BusinessTypeRepository businessTypeRepository;

	@Autowired
	public BusinessTypeServiceImpl(BusinessTypeRepository businessTypeRepository) {
		this.businessTypeRepository = businessTypeRepository;
	}

	@Override
	public List<BusinessType> findAll() {
		return businessTypeRepository.findAll();
	}

	@Override
	public BusinessType findOne(long businessTypeId) {
		return businessTypeRepository.findOne(businessTypeId);
	}

	@Override
	public BusinessType findOne(String businessTypeName) {
		return businessTypeRepository.findOne(businessTypeName);
	}

	@Override
	public void save(BusinessType businessType) {
		businessTypeRepository.save(businessType);
	}

}
