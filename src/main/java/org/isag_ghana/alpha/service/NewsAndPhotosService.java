package org.isag_ghana.alpha.service;

import java.util.List;

import org.isag_ghana.alpha.model.NewsAndPhotos;
import org.isag_ghana.alpha.model.NewsBlog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NewsAndPhotosService {

	public List<NewsAndPhotos> findAllByNews(NewsBlog news);

	public NewsAndPhotos findById(Long newsId);

	public void save(NewsAndPhotos newsAndPhotos);

	public void save(List<NewsAndPhotos> newsAndPhotos);

	public Page<NewsAndPhotos> findAllByNews(NewsBlog news, Pageable pageable);

	public List<NewsAndPhotos> findByNewsAndIsCoverPicture(NewsBlog blog);

}