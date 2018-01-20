package org.isag_ghana.alpha.repository;

import java.util.List;

import org.isag_ghana.alpha.model.NewsBlog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsBlogRepository extends JpaRepository<NewsBlog, Long> {

	@Query("SELECT n FROM NewsBlog n ORDER BY n.date DESC")
	public List<NewsBlog> findByDateOrderByDateDesc();

	public Page<NewsBlog> findAll(Pageable pageable);

	public NewsBlog findById(Long id);
}
