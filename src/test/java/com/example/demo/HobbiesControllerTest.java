package com.example.demo;

import com.example.demo.entity.Hobbies;
import com.example.demo.respository.HobbiesRepository;
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

public class HobbiesControllerTest extends RestDbTests{
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private HobbiesRepository hobbiesRepository;


	@AfterEach
	public void removeALl() {
		hobbiesRepository.deleteAll();
	}

	public Hobbies createHobbies(String name){
		Hobbies hobbies=Hobbies.builder()
				.name(name).build();
		return hobbiesRepository.save(hobbies);
	}
	@Test
	public void createHobbies() throws Exception {
		//Given
		Hobbies hobbies=Hobbies.builder()
				.name("football").build();
		//When
		ResultActions resultActions = mockMvc.perform(post("/hobbies/create")
				.content(objectMapper.writeValueAsBytes(hobbies))
				.contentType(MediaType.APPLICATION_JSON));
		//Then
		resultActions.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.name").value("football"))
				.andDo(print());
	}
	@Test
	public void findHobbiesByIdTest() throws Exception {
		//Given
		Hobbies hobbies = createHobbies("basketball");
		Long id = hobbies.getId();
		//When
		ResultActions resultActions = mockMvc.perform(get("/hobbies/find{id}", id));
		//Then
		resultActions.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("basketball"))
				.andDo(print());
	}

	@Test
	public void findHobbiesByIdNotFoundExceptionTest() throws Exception {
		//Given
		Long id = 0L;
		//When
		ResultActions resultActions = mockMvc.perform(get("/hobbies/find{id}", id));
		//Then
		resultActions.andExpect(status().isBadRequest())
				.andExpect(result -> assertEquals("element id=0 not found", result.getResolvedException().getMessage()))
				.andDo(print());
	}

	@Test
	public void findAllHobbiesTest() throws Exception {
		//Given
		Hobbies hobbies = createHobbies("tennis");
		Hobbies hobbies1 = createHobbies("golf");
		//When
		ResultActions resultActions = mockMvc.perform(get("/hobbies/all"));
		//Then
		resultActions.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("tennis"))
				.andExpect(jsonPath("$[1].name").value("golf"))
				.andDo(print());
	}
	@Test
	public void patchHobbiesTest() throws Exception {
		//Given
		Hobbies hobbies = createHobbies("football");
		Hobbies hobbies2 = Hobbies.builder()
				.name("basketball").build();

		Long id = hobbies.getId();
		//When
		ResultActions resultActions = mockMvc.perform(put("/hobbies/patch{id}", id)
				.content(objectMapper.writeValueAsString(hobbies2))
				.contentType(MediaType.APPLICATION_JSON));
		//Then
		resultActions.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	public void deleteHobbiesTest() throws Exception {
		//Given
		Hobbies hobbies= createHobbies("tennis");
		Long id = hobbies.getId();
		//When
		ResultActions resultActions = mockMvc.perform(delete("/hobbies/remove{id}", id));
		//Then
		resultActions.andExpect(status().isOk())
				.andDo(print());

		assertEquals(String.format("element id=%d remove", id), resultActions.andReturn().getResponse().getContentAsString());
	}
}
