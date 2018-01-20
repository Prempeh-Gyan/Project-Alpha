package org.isag_ghana.alpha.service;

import java.util.List;
import java.util.Set;

import org.isag_ghana.alpha.model.Role;

public interface RoleService {

	public List<Role> findAll();

	public Role findOneByName(String name);

	public Role findOne(Long roleId);

	public void saveRole(Role role);

	public void saveRoles(Set<Role> roles);

}
