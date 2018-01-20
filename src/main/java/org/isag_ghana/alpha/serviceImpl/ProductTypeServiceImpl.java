package org.isag_ghana.alpha.serviceImpl;

import java.util.List;

import org.isag_ghana.alpha.model.ProductType;
import org.isag_ghana.alpha.repository.ProductTypeRepository;
import org.isag_ghana.alpha.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductTypeServiceImpl implements ProductTypeService {

	private final ProductTypeRepository productTypeRepository;

	@Autowired
	public ProductTypeServiceImpl(ProductTypeRepository productTypeRepository) {
		this.productTypeRepository = productTypeRepository;
	}

	@Override
	public List<ProductType> findAll() {
		return productTypeRepository.findAll();
	}

	@Override
	public ProductType findOne(long productTypeId) {
		return productTypeRepository.findOne(productTypeId);
	}

	@Override
	public ProductType findOne(String productTypeName) {
		return productTypeRepository.findOne(productTypeName);
	}

	@Override
	public void save(ProductType productType) {
		productTypeRepository.save(productType);
	}

}
