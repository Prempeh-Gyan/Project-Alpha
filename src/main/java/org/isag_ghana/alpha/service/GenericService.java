package org.isag_ghana.alpha.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GenericService {

	public <T> List<T> findAll();

	public <T> Page<T> findAll(Pageable pageable);

	public <T> T findById(Long id);

	public <T> T findByName(String name);

	<T> void save(T t);

}