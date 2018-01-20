package org.isag_ghana.alpha.service;

import org.isag_ghana.alpha.model.Document;

public interface DocumentService {

	public Document findOne(long photoId);

	public Document findOne(String name);

	public void save(Document photo);

}