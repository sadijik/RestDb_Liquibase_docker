package com.example.demo.service;

import com.example.demo.configuration.ConvertToDto;
import com.example.demo.entity.Documents;
import com.example.demo.entity.Hobbies;
import com.example.demo.entity.Person;
import com.example.demo.exception.ElementNotFoundException;
import com.example.demo.respository.DocumentRepository;
import com.example.demo.respository.HobbiesRepository;
import com.example.demo.respository.PersonRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PersonService {
	private final PersonRepository personRepository;
	private final DocumentRepository documentRepository;
	private final HobbiesRepository hobbiesRepository;



	public PersonService(PersonRepository personRepository, DocumentRepository documentRepository, HobbiesRepository hobbiesRepository) {
		this.personRepository = personRepository;
		this.documentRepository = documentRepository;

		this.hobbiesRepository = hobbiesRepository;
	}

	@Transactional
	public Person createPerson(Person person) {
		return personRepository.save(person);
	}

	@Transactional
	public Person findPersonById(Long id) throws Exception {
		return personRepository.findById(id).orElseThrow(() -> new ElementNotFoundException(String.format("element id=%d not found", id)));
	}

	@Transactional
	public List<Person> findPersonAll() {
		Iterable<Person> all = personRepository.findAll();
		List<Person> list = new ArrayList<>();
		all.forEach(list::add);
		return list;
	}

	@Transactional
	public Person updatePerson(Long id, Person person) throws ElementNotFoundException {
		Person personByID = personRepository.findById(id).orElseThrow(() -> new ElementNotFoundException(String.format("element id=%d not found", id)));
		personByID.setFirstName(person.getFirstName());
		personByID.setSecondName(person.getSecondName());
		personByID.setAge(person.getAge());
		person.setDocuments(person.getDocuments());
		person.setHobbies(person.getHobbies());
		return personByID;
	}

	@Transactional
	public String deletePerson(Long id) throws ElementNotFoundException {
		Person personByID = personRepository.findById(id).orElseThrow(() -> new ElementNotFoundException(String.format("element id=%d not found", id)));
		personRepository.delete(personByID);
		return String.format("element id=%d remove", id);
	}

	@Transactional
	public Person adDdocumentToPerson(Long idDoc, Long idPerson) throws ElementNotFoundException {
		Documents documents = documentRepository.findById(idDoc).orElseThrow(() -> new ElementNotFoundException(String.format("element id=%d not found", idDoc)));
		Person person = personRepository.findById(idPerson).orElseThrow(() -> new ElementNotFoundException(String.format("element id=%d not found", idPerson)));
		person.setDocuments(documents);
		return personRepository.save(person);
	}
	@Transactional
	public Person adDHobbiesToPerson(Long idHob, Long idPerson) throws ElementNotFoundException {
		Hobbies hobbies1 = hobbiesRepository.findById(idHob).orElseThrow(() -> new ElementNotFoundException(String.format("element id=%d not found", idPerson)));
		Person person = personRepository.findById(idPerson).orElseThrow(() -> new ElementNotFoundException(String.format("element id=%d not found", idPerson)));
		Set<Hobbies> hobbies= person.getHobbies();
		hobbies.add(hobbies1);
		person.setHobbies(hobbies);
		return personRepository.save(person);
	}
}
