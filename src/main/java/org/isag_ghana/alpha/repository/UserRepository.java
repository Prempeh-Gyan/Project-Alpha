
package org.isag_ghana.alpha.repository;

import java.util.List;

import org.isag_ghana.alpha.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	public User findByEmail(String email);

	@Query("SELECT u FROM User u WHERE u.isEnabled = :isEnabled")
	public List<User> findByIsEnabled(@Param("isEnabled") boolean isEnabled);

	@Query("SELECT u FROM User u WHERE u.isEmailConfirmed = true AND u.isEnabled = false AND u.isAccountNonLocked = false AND u.isAccountNonExpired = false AND u.isCredentialsNonExpired = false")
	public Page<User> findAllNewMembers(Pageable pageable);

	@Query("SELECT u FROM User u WHERE u.isEmailConfirmed = true AND u.isEnabled = false AND u.isAccountNonLocked = false AND u.isAccountNonExpired = false AND u.isCredentialsNonExpired = false")
	public List<User> findAllNewMembers();

	@Query("SELECT u FROM User u WHERE u.isEnabled = true AND u.isAccountNonLocked = true AND u.isAccountNonExpired = true AND u.isCredentialsNonExpired = true AND u.role < 6")
	public Page<User> findAllMembers(Pageable pageable);

	@Query("SELECT u FROM User u WHERE u.isEnabled = true AND u.isAccountNonLocked = true AND u.isAccountNonExpired = true AND u.isCredentialsNonExpired = true")
	public List<User> findAllMembers();

	public User findOne(long id);

	public void delete(User user);

	public Page<User> findAll(Pageable pageable);
}