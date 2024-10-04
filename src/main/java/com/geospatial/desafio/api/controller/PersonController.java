package com.geospatial.desafio.api.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.geospatial.desafio.api.model.request.PersonUpdateRequest;
import com.geospatial.desafio.api.model.response.AgeResponse;
import com.geospatial.desafio.api.model.response.SalaryResponse;
import com.geospatial.desafio.domain.model.enums.PersonAge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.geospatial.desafio.api.model.request.PersonRequest;
import com.geospatial.desafio.api.model.response.PersonResponse;
import com.geospatial.desafio.domain.service.PersonService;

@RestController
@RequestMapping(value = "/person")
public class PersonController {
	@Autowired
	private PersonService personService;

	@GetMapping
	public List<PersonResponse> findAll() {
		return personService.findAll();
	}

	@GetMapping("/{person_id}")
	public PersonResponse getById(@PathVariable("person_id") Long id) {
		return personService.getById(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PersonResponse create(@RequestBody @Valid PersonRequest person) {
		return personService.create(person);
	}

	@PutMapping("/{person_id}")
	public PersonResponse alter(@PathVariable("person_id") Long id, @RequestBody @Valid PersonUpdateRequest person) {
		return personService.alter(person, id);
	}

	@DeleteMapping("/{person_id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remove(@PathVariable("person_id") Long id) {
		personService.remove(id);
	}

	@PatchMapping("/{person_id}")
	public PersonResponse alter(@RequestBody Map<String, Object> fields, @PathVariable("person_id") Long id, HttpServletRequest request) {
		return personService.alter(fields, id, request);
	}

	@GetMapping("/{person_id}/age")
	public AgeResponse getAge(@PathVariable("person_id") Long id, @RequestParam("output") String output) {
		return personService.getAge(id, output);
	}

	@GetMapping("/{person_id}/salary")
	public SalaryResponse getSalary(@PathVariable("person_id") Long id, @RequestParam("output") String output) {
		return personService.getSalary(id, output);
	}
}