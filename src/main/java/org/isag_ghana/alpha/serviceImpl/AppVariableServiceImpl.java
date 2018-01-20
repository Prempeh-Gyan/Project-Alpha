package org.isag_ghana.alpha.serviceImpl;

import java.util.List;

import org.isag_ghana.alpha.model.AppVariable;
import org.isag_ghana.alpha.repository.AppVariableRepository;
import org.isag_ghana.alpha.service.AppVariableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AppVariableServiceImpl implements AppVariableService {

	private final AppVariableRepository appVariableRepository;

	@Autowired
	public AppVariableServiceImpl(AppVariableRepository appVariableRepository) {
		this.appVariableRepository = appVariableRepository;
	}

	@Override
	public void save(AppVariable variable) {
		appVariableRepository.save(variable);
	}

	@Override
	public AppVariable findByName(String name) {
		return appVariableRepository.findByName(name);
	}

	@Override
	public List<AppVariable> findAll() {
		return appVariableRepository.findAll();
	}
}
