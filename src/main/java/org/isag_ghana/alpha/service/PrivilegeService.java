package org.isag_ghana.alpha.service;

import java.util.List;
import java.util.Set;

import org.isag_ghana.alpha.model.Privilege;

public interface PrivilegeService {

	public Set<Privilege> findAll();

	public Privilege fineOneByName(String name);

	public void savePrivilege(Privilege privilege);

	public void savePrivileges(Set<Privilege> privileges);
	
	public void savePrivileges(List<Privilege> privileges);

}
