package com.geospatial.desafio.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {
	
	
	RESOURCE_NOT_FOUND("/resource-not-found", "Resource not found"),
	ENTITY_ALREADY_EXISTS("/entity-in-use", "Entity already exists"),
	BUSINESS_ERROR("/business-error", "Business Error"),
	INCOMPREENSIBLE_MESSAGE("/incompreensible-message", "Incompreensible message"),
	INVALID_PARAMETER("/invalid-parameter", "Invalid parameter"),
	INTERNAL_SERVER_ERROR("/internal-server-error", "Erro de sistema.");
	
	private static final String URL = "https://geospatial.com.br";
	private String title;
	private String uri;

	ProblemType(String path, String title) {
		StringBuilder uri = new StringBuilder();
		this.uri = uri.append(URL).append(path).toString();
		this.title = title;
	}
}