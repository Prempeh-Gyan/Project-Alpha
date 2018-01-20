package org.isag_ghana.alpha.service;

import java.util.List;

import org.isag_ghana.alpha.model.AdvertisingCompany;

public interface AdvertService {

	public void saveAdvert(AdvertisingCompany company);

	public List<AdvertisingCompany> findAll();

	public List<AdvertisingCompany> findByIsNonExpired(Boolean isNonExpired);

	public List<AdvertisingCompany> findByCompanyName(String companyName);

	public List<AdvertisingCompany> findByAdvertCode(String advertCode);
}