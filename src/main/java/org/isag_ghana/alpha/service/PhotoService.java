package org.isag_ghana.alpha.service;

import org.isag_ghana.alpha.model.Photo;

public interface PhotoService {

	public Photo findOne(long photoId);
	
	public Photo findOne(String name);
	
	public void save(Photo photo);

}