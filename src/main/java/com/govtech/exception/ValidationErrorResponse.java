package com.govtech.exception;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ValidationErrorResponse {
	
	private String message;
	
	private Map<String, String> errors;

	public ValidationErrorResponse(String message) {
		
		this.message = message;
		this.errors = new HashMap<>();
	}

	public void addError(String field, String errorMessage) {
		errors.put(field, errorMessage);
	}
}
