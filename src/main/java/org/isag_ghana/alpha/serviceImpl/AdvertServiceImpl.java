package org.isag_ghana.alpha.serviceImpl;

import java.util.List;

import org.isag_ghana.alpha.model.AdvertisingCompany;
import org.isag_ghana.alpha.repository.AdvertRepository;
import org.isag_ghana.alpha.service.AdvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdvertServiceImpl implements AdvertService {

	private final AdvertRepository advertRepository;

	@Autowired
	public AdvertServiceImpl(AdvertRepository advertRepository) {
		this.advertRepository = advertRepository;
	}

	@Override
	public void saveAdvert(AdvertisingCompany company) {
		advertRepository.save(company);
	}

	@Override
	public List<AdvertisingCompany> findAll() {
		return advertRepository.findAll();
	}

	@Override
	public List<AdvertisingCompany> findByIsNonExpired(Boolean isNonExpired) {
		return advertRepository.findByIsNonExpired(isNonExpired);
	}

	@Override
	public List<AdvertisingCompany> findByCompanyName(String companyName) {
		return advertRepository.findByCompanyName(companyName);
	}

	@Override
	public List<AdvertisingCompany> findByAdvertCode(String advertCode) {
		return advertRepository.findByAdvertCode(advertCode);
	}

}
