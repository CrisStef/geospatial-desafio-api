package com.geospatial.desafio.api.model.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SalaryResponse {
	private String description;
	private BigDecimal value;
}