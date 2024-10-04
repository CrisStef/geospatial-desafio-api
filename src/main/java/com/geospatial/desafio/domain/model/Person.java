package com.geospatial.desafio.domain.model;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@JsonRootName("person")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Person {
    @EqualsAndHashCode.Include
	@Id
	private Long id;

	@NotBlank
	@Column(nullable = false)
	private String name;

	@NotNull
	@Column(columnDefinition = "datetime")
	private LocalDate birthDate;

	@NotNull
	@Column(columnDefinition = "datetime")
	private LocalDate admissionDate;

	public Integer getYearsInCompany() {
		return LocalDate.now().getYear() - admissionDate.getYear();
	}
}