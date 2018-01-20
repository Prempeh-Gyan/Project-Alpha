package org.isag_ghana.alpha.service;

import java.util.List;

import org.isag_ghana.alpha.model.Business;
import org.isag_ghana.alpha.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BusinessService {

	public List<Business> findAll();

	public List<Business> findAllByUser(User user);

	public Page<Business> findAll(Pageable pageable);

	public Business findById(Long businessId);

	public Business findByName(String companyName);

	public void save(Business business);
	
	public void delete(Business business);

}