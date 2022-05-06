package com.example.demo;

import com.example.demo.entity.Documents;
import com.example.demo.respository.DocumentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DocumentControllerTest extends RestDbTests{
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private DocumentRepository documentRepository;


	@AfterEach
	public void removeALl() {
		documentRepository.deleteAll();
	}

	public Documents createDocuments(String docName, Long number) {
		Documents documents = Documents.builder()
				.docName(docName)
				.number(number).build();
		return documentRepository.save(documents);
	}

	@Test
	public void createDocuments() throws Exception {
		//Given
		Documents documents = Documents.builder()
				.docName("passport")
				.number(887654331).build();
		//When
		ResultActions resultActions = mockMvc.perform(post("/documents/create")
				.content(objectMapper.writeValueAsBytes(documents))
				.contentType(MediaType.APPLICATION_JSON));
		//Then
		resultActions.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.docName").value("passport"))
				.andExpect(jsonPath("$.number").value(887654331))
				.andDo(print());
	}

	@Test
	public void findDocumentsByIdTest() throws Exception {
		//Given
		Documents documents = createDocuments("passport", 121321312L);
		Long id = documents.getId();
		//When
		ResultActions resultActions = mockMvc.perform(get("/documents/find{id}", id));
		//Then
		resultActions.andExpect(status().isOk())
				.andExpect(jsonPath("$.docName").value("passport"))
				.andExpect(jsonPath("$.number").value(121321312L))
				.andDo(print());
	}

	@Test
	public void findDocumentsByIdNotFoundExceptionTest() throws Exception {
		//Given
		Long id = 0L;
		//When
		ResultActions resultActions = mockMvc.perform(get("/documents/find{id}", id));
		//Then
		resultActions.andExpect(status().isBadRequest())
				.andExpect(result -> assertEquals("element id=0 not found", result.getResolvedException().getMessage()))
				.andDo(print());
	}

	@Test
	public void findAllDocumentsTest() throws Exception {
		//Given
		Documents documents = createDocuments("passport1", 10000001L);
		Documents documents1 = createDocuments("passport2", 900000009L);

		//When
		ResultActions resultActions = mockMvc.perform(get("/documents/all"));
		//Then
		resultActions.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].docName").value("passport1"))
				.andExpect(jsonPath("$[0].number").value(10000001L))
				.andExpect(jsonPath("$[1].docName").value("passport2"))
				.andExpect(jsonPath("$[1].number").value(900000009L))
				.andDo(print());
	}

	@Test
	public void patchDocumentsTest() throws Exception {
		//Given
		Documents documents = createDocuments("passport", 131312312L);
		Documents documents2 = Documents.builder()
				.docName("passport№2")
				.number(5555555555L).build();

		Long id = documents.getId();
		//When
		ResultActions resultActions = mockMvc.perform(put("/documents/patch{id}", id)
				.content(objectMapper.writeValueAsString(documents2))
				.contentType(MediaType.APPLICATION_JSON));
		//Then
		resultActions.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	public void deleteDocumentsTest() throws Exception {
		//Given
		Documents documents = createDocuments("passport", 953453453L);

		Long id = documents.getId();
		//When
		ResultActions resultActions = mockMvc.perform(delete("/documents/remove{id}", id));
		//Then
		resultActions.andExpect(status().isOk())
				.andDo(print());

		assertEquals(String.format("element id=%d remove", id), resultActions.andReturn().getResponse().getContentAsString());
	}
}
