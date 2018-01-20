package org.isag_ghana.alpha.service;

import java.util.List;

import org.isag_ghana.alpha.model.NewsAndVideos;
import org.isag_ghana.alpha.model.NewsBlog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NewsAndVideosService {

	public List<NewsAndVideos> findAllByNews(NewsBlog news);

	public NewsAndVideos findById(Long newsId);

	public void save(NewsAndVideos newsAndVideos);

	public void save(List<NewsAndVideos> newsAndVideos);

	public Page<NewsAndVideos> findAllByNews(NewsBlog news, Pageable pageable);

}