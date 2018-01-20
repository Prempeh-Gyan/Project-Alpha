package org.isag_ghana.alpha.controller.publik;

import java.io.IOException;

import javax.validation.constraints.NotNull;

import org.isag_ghana.alpha.model.Document;
import org.isag_ghana.alpha.model.Photo;
import org.isag_ghana.alpha.model.Video;
import org.isag_ghana.alpha.service.DocumentService;
import org.isag_ghana.alpha.service.PhotoService;
import org.isag_ghana.alpha.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor(onConstructor = @__({ @Autowired }))
public class PhotoController {
	private final @NotNull PhotoService photoService;
	private final @NotNull VideoService videoService;
	private final @NotNull DocumentService documentService;

	@RequestMapping(value = "/getPicture/{photoId}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<byte[]> getImage(@PathVariable("photoId") Long photoId) throws IOException {
		log.info("PhotoId is : {}", photoId);
		Photo photo = photoService.findOne(photoId);
		byte[] imageContent = photo.getImage();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		log.info("returning image");
		return new ResponseEntity<byte[]>(imageContent, headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/getVideo/{videoId}", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
	public ResponseEntity<byte[]> getVideo(@PathVariable("videoId") Long videoId) throws IOException {
		log.info("videoId is : {}", videoId);
		Video video = videoService.findOne(videoId);
		byte[] imageContent = video.getVideo();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		log.info("returning image");
		return new ResponseEntity<byte[]>(imageContent, headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/getDocument/{documentId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> getDocument(@PathVariable("documentId") Long documentId) throws IOException {
		log.info("documentId is : {}", documentId);
		Document document = documentService.findOne(documentId);
		byte[] imageContent = document.getDocument();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		log.info("returning document");
		return new ResponseEntity<byte[]>(imageContent, headers, HttpStatus.OK);
	}
}
