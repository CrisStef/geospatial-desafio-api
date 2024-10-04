package com.geospatial.desafio.domain.exception;

public class PersonNotFoundException extends EntityNotFoundException {
	private static final long serialVersionUID = 4879555631582992766L;

	public PersonNotFoundException(String message) {
		super(message);
	}

	public PersonNotFoundException(Long id) {
		super(String.format("Person not found! Id: %d", id));
	}
}