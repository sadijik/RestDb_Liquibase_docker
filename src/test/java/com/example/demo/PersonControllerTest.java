package com.example.demo;

import com.example.demo.entity.Documents;
import com.example.demo.entity.Hobbies;
import com.example.demo.entity.Person;
import com.example.demo.respository.DocumentRepository;
import com.example.demo.respository.HobbiesRepository;
import com.example.demo.respository.PersonRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class PersonControllerTest extends RestDbTests {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private DocumentRepository documentRepository;
	@Autowired
	private HobbiesRepository hobbiesRepository;

	@AfterEach
	public void removeALl() {
		personRepository.deleteAll();
	}

	private Person createPerson(String firstName, String secondName, int age) {
		Person person = Person.builder()
				.firstName(firstName)
				.secondName(secondName)
				.age(age).build();
		return personRepository.save(person);
	}

	@Test
	public void createPersonTest() throws Exception {
		//Given
		Person person = Person.builder()
				.firstName("Ivan")
				.secondName("Ivanov")
				.age(26).build();
		//when
		ResultActions resultActions = mockMvc.perform(post("/person/create")
				.content(objectMapper.writeValueAsBytes(person))
				.contentType(MediaType.APPLICATION_JSON));
		//Then
		resultActions.andExpect(status().isOk());
		resultActions.andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.age").value(26))
				.andExpect(jsonPath("$.firstname").value("Ivan"))
				.andExpect(jsonPath("$.secondname").value("Ivanov"))
				.andDo(print());
	}

	@Test
	public void createPersonExceptionValidationTest() throws Exception {
		//Given
		Person build = Person.builder()
				.firstName(null)
				.secondName(null)
				.age(0).build();
		//when
		ResultActions resultActions = mockMvc.perform(post("/person/create")
				.content(objectMapper.writeValueAsBytes(build))
				.contentType(MediaType.APPLICATION_JSON));
		//Then
		resultActions.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.age").value("1<age<130"))
				.andExpect(jsonPath("$.firstName").value("field is empty"))
				.andExpect(jsonPath("$.secondName").value("field is empty"))
				.andDo(print());
	}


	@Test
	public void findPersonByIdTest() throws Exception {
		//Given
		Person person = createPerson("Denis", "Maloi", 27);
		Long id = person.getId();
		//When
		ResultActions resultActions = mockMvc.perform(get("/person/find{id}", id));
		//Then
		resultActions.andExpect(status().isOk())
				.andExpect(jsonPath("$.firstname").value("Denis"))
				.andExpect((jsonPath("$.secondname").value("Maloi")))
				.andExpect(jsonPath("$.age").value(27))
				.andDo(print());
	}

	@Test
	public void findPersonByIdNotFoundExceptionTest() throws Exception {
		//Given
		Long id = 0L;
		//When
		ResultActions resultActions = mockMvc.perform(get("/person/find{id}", id));
		//Then
		resultActions.andExpect(status().isBadRequest())
				.andExpect(result -> assertEquals("element id=0 not found", result.getResolvedException().getMessage()))
				.andDo(print());
	}

	@Test
	public void findAllPersonTest() throws Exception {
		//Given
		Person person1 = createPerson("Misha", "Sergeev", 8);
		Person person2 = createPerson("Lesha", "Kapustin", 10);
		//When
		ResultActions resultActions = mockMvc.perform(get("/person/all"));
		//Then
		resultActions.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].firstname").value("Misha"))
				.andExpect((jsonPath("$[0].secondname").value("Sergeev")))
				.andExpect(jsonPath("$[0].age").value(8))
				.andExpect(jsonPath("$[1].firstname").value("Lesha"))
				.andExpect((jsonPath("$[1].secondname").value("Kapustin")))
				.andExpect(jsonPath("$[1].age").value(10))
				.andDo(print());
	}

	@Test
	public void patchPersonTest() throws Exception {
		//Given
		Person person = createPerson("Ivan", "Groznui", 62);
		Person person2 = Person.builder()
				.id(person.getId())
				.firstName("Lena")
				.secondName("Kyzeva")
				.age(31).build();

		Long id = person.getId();
		//When
		ResultActions resultActions = mockMvc.perform(put("/person/patch{id}", id)
				.content(objectMapper.writeValueAsString(person2))
				.contentType(MediaType.APPLICATION_JSON));
		//Then
		resultActions.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	public void deletePersonTest() throws Exception {
		//Given
		Person person = createPerson("Vano", "Serui", 96);
		Long id = person.getId();
		//When
		ResultActions resultActions = mockMvc.perform(delete("/person/remove{id}", id));
		//Then
		resultActions.andExpect(status().isOk())
				.andDo(print());

		assertEquals(String.format("element id=%d remove", id), resultActions.andReturn().getResponse().getContentAsString());
	}

	@Test
	public void addDocToPersonTest() throws Exception {
		//Given
		Documents documents = Documents.builder()
				.docName("passport")
				.number(234456454).build();
		documentRepository.save(documents);
		Long idDoc = documents.getId();

		Person person = createPerson("Igor", "White", 76);
		Long idPerson = person.getId();
		//When
		ResultActions resultActions = mockMvc.perform(post("/person/addDoc{idDoc}toPerson{idPerson}", idDoc, idPerson)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(person))
				.content(objectMapper.writeValueAsBytes(documents)));
		//Then
		resultActions.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.firstname").value("Igor"))
				.andExpect((jsonPath("$.secondname").value("White")))
				.andExpect(jsonPath("$.age").value(76))
				.andExpect(jsonPath("$.documentsDto.id").isNumber())
				.andExpect(jsonPath("$.documentsDto.docName").value("passport"))
				.andExpect(jsonPath("$.documentsDto.number").value(234456454))
				.andDo(print());
	}

	@Test
	public void addHobToPersonTest() throws Exception {
		//Given
		Hobbies hobbies = Hobbies.builder()
				.name("football").build();
		hobbiesRepository.save(hobbies);
		Long idHob = hobbies.getId();

		Person person = createPerson("Kate", "Gorkun", 18);
		Long idPerson = person.getId();
		//When
		ResultActions resultActions = mockMvc.perform(post("/person/addHob{idHob}toPerson{idPerson}", idHob, idPerson)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(person))
				.content(objectMapper.writeValueAsBytes(hobbies)));
		//Then
		resultActions.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.firstname").value("Kate"))
				.andExpect((jsonPath("$.secondname").value("Gorkun")))
				.andExpect(jsonPath("$.age").value(18))
				.andExpect(jsonPath("$.hobbiesDto[0].id").isNumber())
				.andExpect(jsonPath("$.hobbiesDto[0].name").value("football"))
				.andDo(print());
	}
}
