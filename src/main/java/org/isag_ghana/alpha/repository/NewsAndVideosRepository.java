
package org.isag_ghana.alpha.repository;

import java.util.List;

import org.isag_ghana.alpha.model.NewsAndVideos;
import org.isag_ghana.alpha.model.NewsBlog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsAndVideosRepository extends JpaRepository<NewsAndVideos, Long> {

	@Query("SELECT n FROM NewsAndVideos n WHERE n.news = :news ORDER BY n.date DESC")
	public List<NewsAndVideos> findAllByNews(@Param("news") NewsBlog news);

	@Query("SELECT n FROM NewsAndVideos n WHERE n.news = :news ORDER BY n.date DESC")
	public Page<NewsAndVideos> findAllByNews(@Param("news") NewsBlog news, Pageable pageable);

}
