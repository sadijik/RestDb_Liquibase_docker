package com.example.demo.service;

import com.example.demo.entity.Hobbies;
import com.example.demo.exception.ElementNotFoundException;
import com.example.demo.respository.HobbiesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class HobbiesService {
	private final HobbiesRepository hobbiesRepository;


	public HobbiesService(HobbiesRepository hobbiesRepository) {
		this.hobbiesRepository = hobbiesRepository;

	}

	@Transactional
	public Hobbies createHobbies(Hobbies hobbies) {
		return hobbiesRepository.save(hobbies);
	}


	@Transactional
	public Hobbies findHobbiesById(Long id) throws Exception {
		return hobbiesRepository.findById(id).orElseThrow(() -> new ElementNotFoundException(String.format("element id=%d not found", id)));
	}

	@Transactional
	public List<Hobbies> findHobbiesAll() {
		Iterable<Hobbies> all = hobbiesRepository.findAll();
		List<Hobbies> list = new ArrayList<>();
		all.forEach(list::add);
		return list;
	}

	@Transactional
	public Hobbies updateHobbies(Hobbies hobbies, Long id) throws ElementNotFoundException {
		Hobbies hobbies1 = hobbiesRepository.findById(id).orElseThrow(() -> new ElementNotFoundException(String.format("element id=%d not found", id)));
		hobbies1.setName(hobbies.getName());
		return hobbies1;
	}

	@Transactional
	public String deleteHobbies(Long id) throws ElementNotFoundException {
		Hobbies hobbies = hobbiesRepository.findById(id).orElseThrow(() -> new ElementNotFoundException(String.format("element id=%d not found", id)));
		hobbiesRepository.delete(hobbies);
		return String.format("element id=%d remove", id);
	}
}
