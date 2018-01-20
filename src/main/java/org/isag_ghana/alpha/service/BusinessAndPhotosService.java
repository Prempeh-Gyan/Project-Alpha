package org.isag_ghana.alpha.service;

import java.util.List;

import org.isag_ghana.alpha.model.Business;
import org.isag_ghana.alpha.model.BusinessAndPhotos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BusinessAndPhotosService {

	public List<BusinessAndPhotos> findAllByBusiness(Business business);

	public List<BusinessAndPhotos> findBusinessAndBusinessPhotoByBusinessAndIsCoverPicture(Business business);

	public List<BusinessAndPhotos> findBusinessAndBusinessPhotoByBusinessAndIsNotCoverPicture(Business business);

	public BusinessAndPhotos findById(Long businessId);

	public void save(BusinessAndPhotos businessAndPhotos);

	public void delete(Long businessAndPhotosId);

	public void delete(BusinessAndPhotos businessAndPhoto);

	public List<BusinessAndPhotos> deleteAllByBusiness(Business business);

	public void save(List<BusinessAndPhotos> businessAndPhotos);

	public Page<BusinessAndPhotos> findAllByBusiness(Business business, Pageable pageable);

}