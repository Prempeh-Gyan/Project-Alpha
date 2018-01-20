package org.isag_ghana.alpha.serviceImpl;

import java.util.List;

import org.isag_ghana.alpha.model.Gruup;
import org.isag_ghana.alpha.repository.GroupRepository;
import org.isag_ghana.alpha.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GroupServiceImpl implements GroupService {

	private final GroupRepository groupRepository;

	@Autowired
	public GroupServiceImpl(GroupRepository groupRepository) {
		this.groupRepository = groupRepository;
	}

	@Override
	public List<Gruup> findAll() {
		return groupRepository.findAll();
	}

	@Override
	public Gruup findById(Long groupId) {
		return groupRepository.findOne(groupId);
	}

	@Override
	public Gruup findByName(String groupName) {
		return groupRepository.findByName(groupName);
	}

	@Override
	public void save(Gruup group) {
		groupRepository.save(group);
	}

	@Override
	public Page<Gruup> findAll(Pageable pageable) {
		return groupRepository.findAll(pageable);
	}
}
