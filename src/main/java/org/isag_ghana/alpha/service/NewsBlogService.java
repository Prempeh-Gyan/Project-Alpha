package org.isag_ghana.alpha.service;

import java.time.LocalDate;
import java.util.List;

import org.isag_ghana.alpha.model.NewsBlog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NewsBlogService {

	public void saveBlog(NewsBlog blog);

	public List<NewsBlog> findAll();

	public List<NewsBlog> findByStatus(boolean isApproved);

	public List<NewsBlog> findByDate(LocalDate eventDate);

	public List<NewsBlog> findByDateOrderByDateDesc();

	public Page<NewsBlog> findAll(Pageable pageable);
	
	public NewsBlog findById(Long id);

}