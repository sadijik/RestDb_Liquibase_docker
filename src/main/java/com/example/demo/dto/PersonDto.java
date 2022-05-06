package com.example.demo.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PersonDto {
	private Long id;
	private String firstname;
	private String secondname;
	private int age;
	private List<HobbiesDto> hobbiesDto = new ArrayList<>();
	private DocumentsDto documentsDto;
}
