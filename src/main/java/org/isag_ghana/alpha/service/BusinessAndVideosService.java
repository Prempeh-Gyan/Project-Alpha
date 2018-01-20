package org.isag_ghana.alpha.service;

import java.util.List;

import org.isag_ghana.alpha.model.Business;
import org.isag_ghana.alpha.model.BusinessAndVideos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BusinessAndVideosService {

	public List<BusinessAndVideos> findAllByBusiness(Business business);

	public BusinessAndVideos findById(Long businessId);

	public void save(BusinessAndVideos businessAndVideos);

	public void delete(Long businessAndVideosId);

	public void delete(BusinessAndVideos businessAndVideosId);

	public List<BusinessAndVideos> deleteAllByBusiness(Business business);

	public void save(List<BusinessAndVideos> businessAndVideos);

	public Page<BusinessAndVideos> findAllByBusiness(Business business, Pageable pageable);

}