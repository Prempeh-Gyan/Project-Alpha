package org.isag_ghana.alpha.service;

import java.util.List;

import org.isag_ghana.alpha.model.Product;
import org.isag_ghana.alpha.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

	public List<Product> findAllAvailableProducts(boolean isAvailable);

	public Product findOne(long productId);

	public void save(Product product);
	
	public void delete(Product product);
	
	public void delete(Long productId);

	public Page<Product> findAllProductsByUser(User user, Pageable pageable);

	public List<Product> findAllProductsByUser(User user);

	public Page<Product> findAll(Pageable pageable);

}