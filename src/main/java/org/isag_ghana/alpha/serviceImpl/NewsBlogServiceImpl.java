package org.isag_ghana.alpha.serviceImpl;

import java.time.LocalDate;
import java.util.List;

import org.isag_ghana.alpha.model.NewsBlog;
import org.isag_ghana.alpha.repository.NewsBlogRepository;
import org.isag_ghana.alpha.service.NewsBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NewsBlogServiceImpl implements NewsBlogService {

	private final NewsBlogRepository blogRepository;

	@Autowired
	public NewsBlogServiceImpl(NewsBlogRepository blogRepository) {
		this.blogRepository = blogRepository;
	}

	@Override
	public void saveBlog(NewsBlog blog) {
		blogRepository.save(blog);
	}

	@Override
	public List<NewsBlog> findAll() {
		return blogRepository.findAll();
	}

	@Override
	public List<NewsBlog> findByStatus(boolean isApproved) {
		return null;
	}

	@Override
	public List<NewsBlog> findByDate(LocalDate eventDate) {
		return null;
	}

	@Override
	public List<NewsBlog> findByDateOrderByDateDesc() {
		return blogRepository.findByDateOrderByDateDesc();
	}

	@Override
	public Page<NewsBlog> findAll(Pageable pageable) {
		return blogRepository.findAll(pageable);
	}

	@Override
	public NewsBlog findById(Long id) {
		return blogRepository.findById(id);
	}

}
