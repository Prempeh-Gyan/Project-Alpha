package org.isag_ghana.alpha.service;

import java.util.List;

import org.isag_ghana.alpha.model.ProductType;

public interface ProductTypeService {

	public List<ProductType> findAll();

	public ProductType findOne(long productTypeId);
	
	public ProductType findOne(String productTypeName);

	public void save(ProductType productType);

}