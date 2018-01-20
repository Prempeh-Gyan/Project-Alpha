package org.isag_ghana.alpha.service;

import java.util.List;

import org.isag_ghana.alpha.model.Gruup;
import org.isag_ghana.alpha.model.User;
import org.isag_ghana.alpha.model.User_Group;

public interface UserGroupService {

	public List<User_Group> findByUser(User user);

	public List<User_Group> findByGroup(Gruup group);
	
	public List<User_Group> findByGroupAndUser(Gruup group, User user);
	
	public List<User_Group> findAll();

	public User_Group findById(Long userGroupId);

	public void save(User_Group userGroup);

	public void delete(User_Group userGroup);
	
}