package org.isag_ghana.alpha.service;

import java.util.List;

import org.isag_ghana.alpha.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

	public void saveUser(User user);

	public List<User> findByIsEnabled(boolean isEnabled);

	public List<User> findAll();

	public User findOne(long id);

	public User findByEmail(String email);

	public void delete(User user);

	public Page<User> findAll(Pageable pageable);

	public Page<User> findAllNewMembers(Pageable pageable);

	public Page<User> findAllMembers(Pageable pageable);

	public List<User> findAllMembers();

	public List<User> findAllNewMembers();

}