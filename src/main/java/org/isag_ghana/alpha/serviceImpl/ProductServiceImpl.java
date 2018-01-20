package org.isag_ghana.alpha.serviceImpl;

import java.util.List;

import org.isag_ghana.alpha.model.Product;
import org.isag_ghana.alpha.model.User;
import org.isag_ghana.alpha.repository.ProductRepository;
import org.isag_ghana.alpha.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;

	@Autowired
	public ProductServiceImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Override
	public List<Product> findAllAvailableProducts(boolean isAvailable) {
		return productRepository.findAllAvailableProducts(isAvailable);
	}

	@Override
	public Product findOne(long productId) {
		return productRepository.findOne(productId);
	}

	@Override
	public void save(Product product) {
		productRepository.save(product);
	}

	@Override
	public Page<Product> findAll(Pageable pageable) {
		return productRepository.findAll(pageable);
	}

	@Override
	public Page<Product> findAllProductsByUser(User user, Pageable pageable) {
		return productRepository.findAllProductsByUser(user, pageable);
	}

	@Override
	public List<Product> findAllProductsByUser(User user) {
		return productRepository.findAllProductsByUser(user);
	}

	@Override
	public void delete(Product product) {
		productRepository.delete(product);
	}

	@Override
	public void delete(Long productId) {
		productRepository.delete(productId);
	}

}
