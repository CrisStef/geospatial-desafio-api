package com.geospatial.desafio.api.mapper;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.geospatial.desafio.api.model.request.PersonUpdatePatchRequest;
import com.geospatial.desafio.api.model.request.PersonUpdateRequest;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.geospatial.desafio.api.model.request.PersonRequest;
import com.geospatial.desafio.api.model.response.PersonResponse;
import com.geospatial.desafio.domain.model.Person;

@Component
public class PersonMapper {
	@Autowired
	private ModelMapper modelMapper;

	public Person personRequestForPerson(PersonRequest personRequest) {
		return modelMapper.map(personRequest, Person.class);
	}

	public Person personUpdateRequestForPerson(PersonUpdateRequest personUpdateRequest) {
		return modelMapper.map(personUpdateRequest, Person.class);
	}

	public void copyPersonForCurrentPerson(Person person, Person currentPerson) {
		modelMapper.map(person, currentPerson);
	}

	public PersonResponse personForPersonResponse(Person person) {
		return modelMapper.map(person, PersonResponse.class);
	}

	public List<PersonResponse> personListForPersonListResponse(List<Person> person) {
		return modelMapper.map(person, new TypeToken<List<PersonResponse>>(){}.getType());
	}

	public void copyPersonUpdatePatchRequestForPerson(PersonUpdatePatchRequest personUpdateRequest, Person person) {
		modelMapper.map(personUpdateRequest, person);
	}

	public void copyPersonUpdatePatchRequestForPerson(Map<String, Object> originData, Person person) {
		modelMapper.map(originData, person);
	}

	public PersonUpdatePatchRequest mapToPersonUpdatePatchRequest(Map<String, Object> originData) {
		ObjectMapper objectMapper = new ObjectMapper();
		// Registra o JavaTimeModule para suporte a tipos de data/hora do Java 8
		objectMapper.registerModule(new JavaTimeModule());

		// Converte o Map para o PersonUpdateRequest
		return objectMapper.convertValue(originData, PersonUpdatePatchRequest.class);
	}
}