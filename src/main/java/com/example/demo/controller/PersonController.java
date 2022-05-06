package com.example.demo.controller;

import com.example.demo.configuration.ConvertToDto;
import com.example.demo.dto.DocumentsDto;
import com.example.demo.dto.PersonDto;
import com.example.demo.entity.Person;
import com.example.demo.exception.ElementNotFoundException;
import com.example.demo.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/person")
public class PersonController {
	private final PersonService personService;
	private final ConvertToDto convertToDto;


	public PersonController(PersonService personService, ConvertToDto convertToDto) {
		this.personService = personService;
		this.convertToDto = convertToDto;
	}
	@GetMapping
	public String hi(){
		return "personperson";
	}

	@PostMapping("/create")
	public ResponseEntity<PersonDto> createPerson(@RequestBody @Valid Person person) {
		Person person1 = personService.createPerson(person);
		PersonDto personDto = convertToDto.convertPersonToDto(person1);
		return new ResponseEntity<>(personDto, HttpStatus.OK);
	}

	@GetMapping("/find{id}")
	public ResponseEntity<PersonDto> findPersonById(@PathVariable("id") Long id) throws Exception {
		Person personById = personService.findPersonById(id);
		PersonDto personDto = convertToDto.convertPersonToDto(personById);
		return new ResponseEntity<>(personDto, HttpStatus.OK);

	}

	@GetMapping("/all")
	public ResponseEntity<List<PersonDto>> findAllPerson() {
		List<Person> personAll = personService.findPersonAll();
		List<PersonDto> personDtoAll = personAll.stream().map(convertToDto::convertPersonToDto).collect(Collectors.toList());
		return new ResponseEntity<>(personDtoAll, HttpStatus.OK);
	}

	@PutMapping("/patch{id}")
	public ResponseEntity<PersonDto> patchPerson(@PathVariable("id") Long id, @RequestBody @Valid Person person) throws ElementNotFoundException {
		Person person1 = personService.updatePerson(id, person);
		PersonDto personDto = convertToDto.convertPersonToDto(person1);
		return new ResponseEntity<>(personDto, HttpStatus.OK);
	}
	@DeleteMapping("/remove{id}")
	public ResponseEntity<String> deletePerson(@PathVariable("id")Long id) throws ElementNotFoundException {
		return new ResponseEntity<>(personService.deletePerson(id), HttpStatus.OK);
	}
	@PostMapping("/addDoc{idDoc}toPerson{idPerson}")
	public ResponseEntity<PersonDto> addDocToPerson(@PathVariable("idDoc")Long idDoc,@PathVariable("idPerson")Long idPerson) throws ElementNotFoundException {
		Person person = personService.adDdocumentToPerson(idDoc, idPerson);
		PersonDto personDto = convertToDto.convertPersonToDto(person);
		return new ResponseEntity<>(personDto, HttpStatus.OK);
	}
	@PostMapping("/addHob{idHob}toPerson{idPerson}")
	public ResponseEntity<PersonDto> addHobToPerson(@PathVariable("idHob")Long idHob,@PathVariable("idPerson")Long idPerson) throws ElementNotFoundException {
		Person person = personService.adDHobbiesToPerson(idHob, idPerson);
		PersonDto personDto = convertToDto.convertPersonToDto(person);
		return new ResponseEntity<>(personDto, HttpStatus.OK);
	}
}