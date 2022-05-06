package com.example.demo.configuration;

import com.example.demo.dto.DocumentsDto;
import com.example.demo.dto.HobbiesDto;
import com.example.demo.dto.PersonDto;
import com.example.demo.entity.Documents;
import com.example.demo.entity.Hobbies;
import com.example.demo.entity.Person;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ConvertToDto {
	private final ModelMapper mapper;




	public ConvertToDto(ModelMapper mapper) {
		this.mapper = mapper;
	}

	public PersonDto convertPersonToDto(Person person) {
		PersonDto map = mapper.map(person, PersonDto.class);
		if(person.getDocuments()!=null) {
			DocumentsDto documentsDto = convertDocumentsToDto(person.getDocuments());
			map.setDocumentsDto(documentsDto);
		} if (person.getHobbies()!=null){
			List<HobbiesDto> listHobbiesDto= person.getHobbies().stream().map(this::convertHobbiesToDto).collect(Collectors.toList());
			map.setHobbiesDto(listHobbiesDto);
		}
		return map;
	}

	public HobbiesDto convertHobbiesToDto(Hobbies hobbies) {
		HobbiesDto map = mapper.map(hobbies, HobbiesDto.class);
		return map;
	}

	public DocumentsDto convertDocumentsToDto(Documents documents) {
		return mapper.map(documents, DocumentsDto.class);
	}
}
