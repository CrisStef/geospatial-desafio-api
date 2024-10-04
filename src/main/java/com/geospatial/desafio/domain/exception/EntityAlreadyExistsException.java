package com.geospatial.desafio.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class EntityAlreadyExistsException extends BusinessException {
	private static final long serialVersionUID = -8113729813378571040L;

	public EntityAlreadyExistsException(String message) {
		super(message);
	}
}