package com.govtech.exception;

public class RoleValidationException extends RuntimeException{
	
    private static final long serialVersionUID = 1L;

    public RoleValidationException(String message) {
    	
        super(message);
    }
}
