package com.example.demo.controller;

import com.example.demo.configuration.ConvertToDto;
import com.example.demo.dto.DocumentsDto;
import com.example.demo.entity.Documents;
import com.example.demo.exception.ElementNotFoundException;
import com.example.demo.service.DocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/documents")
public class DocumentController {
	private final DocumentService documentService;
	private final ConvertToDto convertToDto;


	public DocumentController(DocumentService documentService, ConvertToDto convertToDto) {
		this.documentService = documentService;
		this.convertToDto = convertToDto;
	}

	@PostMapping("/create")
	public ResponseEntity<DocumentsDto> createDocument(@RequestBody Documents documents) {
		Documents documents1 = documentService.createDocument(documents);
		 DocumentsDto documentsDto =convertToDto.convertDocumentsToDto(documents1);
		System.out.println(documentsDto);
		return new ResponseEntity<>(documentsDto, HttpStatus.OK);
	}

	@GetMapping("/find{id}")
	public ResponseEntity<DocumentsDto> findDocumentById(@PathVariable("id") Long id) throws Exception {
		Documents documentsById = documentService.findDocumentById(id);
		DocumentsDto documentsDto = convertToDto.convertDocumentsToDto(documentsById);
		return new ResponseEntity<>(documentsDto, HttpStatus.OK);

	}

	@GetMapping("/all")
	public ResponseEntity<List<DocumentsDto>> allDocument() {
		List<Documents> documentsAll = documentService.findDocumentAll();
		List<DocumentsDto> documentsDtoAll = documentsAll.stream().map(convertToDto::convertDocumentsToDto).collect(Collectors.toList());
		return new ResponseEntity<>(documentsDtoAll, HttpStatus.OK);
	}

	@PutMapping("/patch{id}")
	public ResponseEntity<DocumentsDto> patchDocument(@PathVariable("id") Long id, @RequestBody Documents documents) throws ElementNotFoundException {
		Documents documents1 = documentService.updateDocument(documents,id);
		DocumentsDto documentsDto = convertToDto.convertDocumentsToDto(documents1);
		return new ResponseEntity<>(documentsDto, HttpStatus.OK);
	}
	@DeleteMapping("/remove{id}")
	public ResponseEntity<String> deleteDocument(@PathVariable("id")Long id) throws ElementNotFoundException {
		return new ResponseEntity<>(documentService.deleteDocument(id), HttpStatus.OK);
	}
}