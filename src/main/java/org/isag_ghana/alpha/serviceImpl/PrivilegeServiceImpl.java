package org.isag_ghana.alpha.serviceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.isag_ghana.alpha.model.Privilege;
import org.isag_ghana.alpha.repository.PrivilegeRepository;
import org.isag_ghana.alpha.service.PrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PrivilegeServiceImpl implements PrivilegeService {

	private final PrivilegeRepository privilegeRepository;

	@Autowired
	public PrivilegeServiceImpl(PrivilegeRepository privilegeRepository) {
		this.privilegeRepository = privilegeRepository;
	}

	@Override
	public Set<Privilege> findAll() {
		return new HashSet<>(privilegeRepository.findAll());
	}

	@Override
	public void savePrivilege(Privilege privilege) {
		privilegeRepository.save(privilege);
	}

	@Override
	public void savePrivileges(Set<Privilege> privileges) {
		privilegeRepository.save(privileges);
	}

	@Override
	public Privilege fineOneByName(String name) {
		return privilegeRepository.fineOneByName(name);
	}

	@Override
	public void savePrivileges(List<Privilege> privileges) {
		privilegeRepository.save(privileges);

	}

}
