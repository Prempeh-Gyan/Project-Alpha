package org.isag_ghana.alpha.service;

import java.util.List;

import org.isag_ghana.alpha.model.BusinessType;

public interface BusinessTypeService {

	public List<BusinessType> findAll();

	public BusinessType findOne(long businessTypeId);

	public BusinessType findOne(String businessTypeName);

	public void save(BusinessType businessType);

}