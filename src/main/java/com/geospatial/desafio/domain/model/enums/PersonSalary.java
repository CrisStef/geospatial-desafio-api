package com.geospatial.desafio.domain.model.enums;

public enum PersonSalary {
	MIN,
	FULL;

	public static PersonSalary fromString(String value) {
		if (value == null) {
			throw new IllegalArgumentException("Value cannot be null.");
		}

		for (PersonSalary age : PersonSalary.values()) {
			if (age.name().equalsIgnoreCase(value)) {
				return age;
			}
		}

		throw new IllegalArgumentException(
				String.format("The value '%s' is of an invalid type. Please correct it and provide a value compatible with type PersonSalary.", value));
	}
}