package org.isag_ghana.alpha.service;

import org.isag_ghana.alpha.model.Video;

public interface VideoService {

	public Video findOne(long videoId);

	public Video findOne(String name);

	public void save(Video video);

}