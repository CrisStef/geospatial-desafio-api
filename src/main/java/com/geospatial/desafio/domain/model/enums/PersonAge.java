package com.geospatial.desafio.domain.model.enums;

public enum PersonAge {
	DAYS,
	MONTHS,
	YEARS;

	public static PersonAge fromString(String value) {
		if (value == null) {
			throw new IllegalArgumentException("Value cannot be null.");
		}

		for (PersonAge age : PersonAge.values()) {
			if (age.name().equalsIgnoreCase(value)) {
				return age;
			}
		}

		throw new IllegalArgumentException(
				String.format("The value '%s' is of an invalid type. Please correct it and provide a value compatible with type PersonAge.", value));
	}
}