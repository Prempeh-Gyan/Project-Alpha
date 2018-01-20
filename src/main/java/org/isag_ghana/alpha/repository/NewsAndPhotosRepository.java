
package org.isag_ghana.alpha.repository;

import java.util.List;

import org.isag_ghana.alpha.model.NewsAndPhotos;
import org.isag_ghana.alpha.model.NewsBlog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsAndPhotosRepository extends JpaRepository<NewsAndPhotos, Long> {

	@Query("SELECT n FROM NewsAndPhotos n WHERE n.news = :news ORDER BY n.date DESC")
	public List<NewsAndPhotos> findAllByNews(@Param("news") NewsBlog news);

	@Query("SELECT n FROM NewsAndPhotos n WHERE n.news = :news ORDER BY n.date DESC")
	public Page<NewsAndPhotos> findAllByNews(@Param("news") NewsBlog news, Pageable pageable);
	
	@Query("SELECT n FROM NewsAndPhotos n WHERE n.news = :news AND n.isCoverPicture = true ORDER BY n.date DESC")
	public List<NewsAndPhotos> findByNewsAndIsCoverPicture(@Param("news") NewsBlog blog);

}
