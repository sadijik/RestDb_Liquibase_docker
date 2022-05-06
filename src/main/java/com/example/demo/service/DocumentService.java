package com.example.demo.service;

import com.example.demo.entity.Documents;
import com.example.demo.exception.ElementNotFoundException;
import com.example.demo.respository.DocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentService {
	private final DocumentRepository documentRepository;

	public DocumentService(DocumentRepository documentRepository) {
		this.documentRepository = documentRepository;
	}


	@Transactional
	public Documents createDocument(Documents documents) {
		return documentRepository.save(documents);
	}

	@Transactional
	public Documents findDocumentById(Long id) throws ElementNotFoundException {
		return documentRepository.findById(id).orElseThrow(() -> new ElementNotFoundException(String.format("element id=%d not found", id)));
	}

	@Transactional
	public List<Documents> findDocumentAll() {
		Iterable<Documents> all = documentRepository.findAll();
		List<Documents> list = new ArrayList<>();
		all.forEach(list::add);
		return list;
	}

	@Transactional
	public Documents updateDocument(Documents documents, Long id) throws ElementNotFoundException {
		Documents documents1 = documentRepository.findById(id).orElseThrow(() -> new ElementNotFoundException(String.format("element id=%d not found", id)));
		documents1.setDocName(documents.getDocName());
		return documents1;
	}

	@Transactional
	public String deleteDocument(Long id) throws ElementNotFoundException {
		Documents documents = documentRepository.findById(id).orElseThrow(() -> new ElementNotFoundException(String.format("element id=%d not found", id)));
		documentRepository.delete(documents);
		return String.format("element id=%d remove", id);
	}
}

