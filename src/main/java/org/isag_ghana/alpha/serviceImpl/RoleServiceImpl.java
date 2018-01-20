package org.isag_ghana.alpha.serviceImpl;

import java.util.List;
import java.util.Set;

import org.isag_ghana.alpha.model.Role;
import org.isag_ghana.alpha.repository.RoleRepository;
import org.isag_ghana.alpha.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

	private final RoleRepository roleRepository;

	@Autowired
	public RoleServiceImpl(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Override
	public List<Role> findAll() {
		return roleRepository.findAll();
	}

	@Override
	public void saveRole(Role role) {
		roleRepository.save(role);
	}

	@Override
	public void saveRoles(Set<Role> roles) {
		roleRepository.save(roles);
	}

	@Override
	public Role findOneByName(String name) {
		return roleRepository.findOneByName(name);
	}

	@Override
	public Role findOne(Long roleId) {
		return roleRepository.findOne(roleId);
	}

}
