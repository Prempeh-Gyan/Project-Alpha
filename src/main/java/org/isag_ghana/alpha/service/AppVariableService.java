package org.isag_ghana.alpha.service;

import java.util.List;

import org.isag_ghana.alpha.model.AppVariable;

public interface AppVariableService {

	public void save(AppVariable variable);

	public AppVariable findByName(String name);

	public List<AppVariable> findAll();
}