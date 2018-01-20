package org.isag_ghana.alpha.serviceImpl;

import java.util.List;

import org.isag_ghana.alpha.model.NewsAndPhotos;
import org.isag_ghana.alpha.model.NewsBlog;
import org.isag_ghana.alpha.repository.NewsAndPhotosRepository;
import org.isag_ghana.alpha.service.NewsAndPhotosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NewsAndPhotosServiceImpl implements NewsAndPhotosService {

	private final NewsAndPhotosRepository newsAndPhotosRepository;

	@Autowired
	public NewsAndPhotosServiceImpl(NewsAndPhotosRepository newsAndPhotosRepository) {
		this.newsAndPhotosRepository = newsAndPhotosRepository;
	}

	@Override
	public List<NewsAndPhotos> findAllByNews(NewsBlog news) {
		return newsAndPhotosRepository.findAllByNews(news);
	}

	@Override
	public NewsAndPhotos findById(Long newsId) {
		return newsAndPhotosRepository.findOne(newsId);
	}

	@Override
	public void save(NewsAndPhotos newsAndPhotos) {
		newsAndPhotosRepository.save(newsAndPhotos);
	}

	@Override
	public void save(List<NewsAndPhotos> newsAndPhotos) {
		newsAndPhotosRepository.save(newsAndPhotos);
	}

	@Override
	public Page<NewsAndPhotos> findAllByNews(NewsBlog news, Pageable pageable) {
		return newsAndPhotosRepository.findAllByNews(news, pageable);
	}

	@Override
	public List<NewsAndPhotos> findByNewsAndIsCoverPicture(NewsBlog blog) {
		return newsAndPhotosRepository.findByNewsAndIsCoverPicture(blog);
	}
}
