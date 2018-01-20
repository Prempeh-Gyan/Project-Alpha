package org.isag_ghana.alpha.serviceImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.isag_ghana.alpha.model.Privilege;
import org.isag_ghana.alpha.model.User;
import org.isag_ghana.alpha.repository.UserRepository;
import org.isag_ghana.alpha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findByEmail(username);

		if (user == null) {
			throw new UsernameNotFoundException("Could not find user " + username);
		} else {
			return new CustomUserDetails(user);
		}
	}

	private final static class CustomUserDetails extends User implements UserDetails {

		private CustomUserDetails(User user) {
			super(user);
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {

			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			Set<Privilege> privileges = new HashSet<>(super.getRole().getPrivileges());

			for (Privilege privilege : privileges) {
				authorities.add(new SimpleGrantedAuthority("ROLE_" + privilege.getName()));
			}
			return authorities;
		}

		@Override
		public String getUsername() {
			return getEmail();
		}

		@Override
		public boolean isAccountNonExpired() {
			return super.getIsAccountNonExpired();
		}

		@Override
		public boolean isAccountNonLocked() {
			return super.getIsAccountNonLocked();
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return super.getIsCredentialsNonExpired();
		}

		@Override
		public boolean isEnabled() {
			return super.getIsEnabled();
		}

		private static final long serialVersionUID = 5639683223516504866L;
	}

	@Override
	public void saveUser(User user) {
		userRepository.save(user);
	}

	@Override
	public List<User> findByIsEnabled(boolean isEnabled) {
		return userRepository.findByIsEnabled(isEnabled);
	}

	@Override
	public User findOne(long id) {
		return userRepository.findOne(id);
	}

	@Override
	public void delete(User user) {
		userRepository.delete(user);
	}

	@Override
	public List<User> findAll() {
		Sort sort = new Sort(Sort.Direction.ASC, "profile.firstName");
		return userRepository.findAll(sort);
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public Page<User> findAll(Pageable pageable) {
		return userRepository.findAll(pageable);
	}

	@Override
	public Page<User> findAllNewMembers(Pageable pageable) {
		return userRepository.findAllNewMembers(pageable);
	}

	@Override
	public List<User> findAllNewMembers() {
		return userRepository.findAllNewMembers();
	}

	@Override
	public Page<User> findAllMembers(Pageable pageable) {
		return userRepository.findAllMembers(pageable);
	}

	@Override
	public List<User> findAllMembers() {
		return userRepository.findAllMembers();
	}

}
