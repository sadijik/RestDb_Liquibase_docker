package com.example.demo.controller;

import com.example.demo.configuration.ConvertToDto;
import com.example.demo.dto.HobbiesDto;
import com.example.demo.dto.PersonDto;
import com.example.demo.entity.Hobbies;
import com.example.demo.entity.Person;
import com.example.demo.exception.ElementNotFoundException;
import com.example.demo.service.HobbiesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/hobbies")
public class HobbiesController {
	private final HobbiesService hobbiesService;
	private final ConvertToDto convertToDto;


	public HobbiesController(HobbiesService hobbiesService, ConvertToDto convertToDto) {
		this.hobbiesService = hobbiesService;
		this.convertToDto = convertToDto;
	}

	@PostMapping("/create")
	public ResponseEntity<HobbiesDto> createHobbies(@RequestBody Hobbies hobbies) {
		Hobbies hobbies1 = hobbiesService.createHobbies(hobbies);
		HobbiesDto hobbiesDto = convertToDto.convertHobbiesToDto(hobbies1);
		return new ResponseEntity<>(hobbiesDto, HttpStatus.OK);
	}

	@GetMapping("/find{id}")
	public ResponseEntity<HobbiesDto> findHobbiesById(@PathVariable("id") Long id) throws Exception {
		Hobbies hobbiesById = hobbiesService.findHobbiesById(id);
		HobbiesDto hobbiesDto = convertToDto.convertHobbiesToDto(hobbiesById);
		return new ResponseEntity<>(hobbiesDto, HttpStatus.OK);

	}

	@GetMapping("/all")
	public ResponseEntity<List<HobbiesDto>> allHobbies() {
		List<Hobbies> hobbiesAll = hobbiesService.findHobbiesAll();
		List<HobbiesDto> hobbiesDtoAll = hobbiesAll.stream().map(convertToDto::convertHobbiesToDto).collect(Collectors.toList());
		return new ResponseEntity<>(hobbiesDtoAll, HttpStatus.OK);
	}

	@PutMapping("/patch{id}")
	public ResponseEntity<HobbiesDto> patchHobbies(@PathVariable("id") Long id, @RequestBody Hobbies hobbies) throws ElementNotFoundException {
		Hobbies hobbies1 = hobbiesService.updateHobbies( hobbies,id);
		HobbiesDto hobbiesDto = convertToDto.convertHobbiesToDto(hobbies1);
		return new ResponseEntity<>(hobbiesDto, HttpStatus.OK);
	}

	@DeleteMapping("/remove{id}")
	public ResponseEntity<String> deleteHobbies(@PathVariable("id") Long id) throws ElementNotFoundException {
		return new ResponseEntity<>(hobbiesService.deleteHobbies(id), HttpStatus.OK);
	}
}