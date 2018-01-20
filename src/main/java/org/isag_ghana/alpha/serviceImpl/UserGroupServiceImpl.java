package org.isag_ghana.alpha.serviceImpl;

import java.util.List;

import org.isag_ghana.alpha.model.Gruup;
import org.isag_ghana.alpha.model.User;
import org.isag_ghana.alpha.model.User_Group;
import org.isag_ghana.alpha.repository.UserGroupRepository;
import org.isag_ghana.alpha.service.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserGroupServiceImpl implements UserGroupService {

	private final UserGroupRepository userGroupRepository;

	@Autowired
	public UserGroupServiceImpl(UserGroupRepository userGroupRepository) {
		this.userGroupRepository = userGroupRepository;
	}

	@Override
	public List<User_Group> findByUser(User user) {
		return userGroupRepository.findByUser(user);
	}

	@Override
	public List<User_Group> findByGroup(Gruup group) {
		return userGroupRepository.findByGroup(group);
	}

	@Override
	public List<User_Group> findAll() {
		return userGroupRepository.findAll();
	}

	@Override
	public User_Group findById(Long userGroupId) {
		return userGroupRepository.findOne(userGroupId);
	}

	@Override
	public void save(User_Group userGroup) {
		userGroupRepository.save(userGroup);
	}

	@Override
	public void delete(User_Group userGroup) {
		userGroupRepository.delete(userGroup);
	}

	@Override
	public List<User_Group> findByGroupAndUser(Gruup group, User user) {
		return userGroupRepository.findByGroupAndUser(group, user);
	}
}