package org.isag_ghana.alpha.serviceImpl;

import java.util.List;

import org.isag_ghana.alpha.model.NewsAndVideos;
import org.isag_ghana.alpha.model.NewsBlog;
import org.isag_ghana.alpha.repository.NewsAndVideosRepository;
import org.isag_ghana.alpha.service.NewsAndVideosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NewsAndVideosServiceImpl implements NewsAndVideosService {

	private final NewsAndVideosRepository newsAndVideosRepository;

	@Autowired
	public NewsAndVideosServiceImpl(NewsAndVideosRepository newsAndVideosRepository) {
		this.newsAndVideosRepository = newsAndVideosRepository;
	}

	@Override
	public List<NewsAndVideos> findAllByNews(NewsBlog news) {
		return newsAndVideosRepository.findAllByNews(news);
	}

	@Override
	public NewsAndVideos findById(Long newsId) {
		return newsAndVideosRepository.findOne(newsId);
	}

	@Override
	public void save(NewsAndVideos newsAndVideos) {
		newsAndVideosRepository.save(newsAndVideos);
	}

	@Override
	public void save(List<NewsAndVideos> newsAndVideos) {
		newsAndVideosRepository.save(newsAndVideos);
	}

	@Override
	public Page<NewsAndVideos> findAllByNews(NewsBlog news, Pageable pageable) {
		return newsAndVideosRepository.findAllByNews(news, pageable);
	}
}
