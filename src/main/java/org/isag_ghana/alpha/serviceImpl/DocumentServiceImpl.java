package org.isag_ghana.alpha.serviceImpl;

import org.isag_ghana.alpha.model.Document;
import org.isag_ghana.alpha.repository.DocumentRepository;
import org.isag_ghana.alpha.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DocumentServiceImpl implements DocumentService {

	private final DocumentRepository documentRepository;

	@Autowired
	public DocumentServiceImpl(DocumentRepository documentRepository) {
		this.documentRepository = documentRepository;
	}

	@Override
	public Document findOne(long documentId) {
		return documentRepository.findOne(documentId);
	}

	@Override
	public void save(Document document) {
		documentRepository.save(document);
	}

	@Override
	public Document findOne(String name) {
		return documentRepository.findOne(name);
	}
}
