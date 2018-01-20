package org.isag_ghana.alpha.serviceImpl;

import org.isag_ghana.alpha.model.Video;
import org.isag_ghana.alpha.repository.VideoRepository;
import org.isag_ghana.alpha.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VideoServiceImpl implements VideoService {

	private final VideoRepository videoRepository;

	@Autowired
	public VideoServiceImpl(VideoRepository videoRepository) {
		this.videoRepository = videoRepository;
	}

	@Override
	public Video findOne(long videoId) {
		return videoRepository.findOne(videoId);
	}

	@Override
	public void save(Video video) {
		videoRepository.save(video);
	}

	@Override
	public Video findOne(String name) {
		return videoRepository.findOne(name);
	}
}
