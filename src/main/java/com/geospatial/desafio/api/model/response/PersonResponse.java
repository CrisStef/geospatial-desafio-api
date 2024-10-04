package com.geospatial.desafio.api.model.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Setter
public class PersonResponse {
	private Long id;
	private String name;
	private LocalDate birthDate;
	private LocalDate admissionDate;
}