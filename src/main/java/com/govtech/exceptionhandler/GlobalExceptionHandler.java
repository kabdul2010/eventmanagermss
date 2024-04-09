package com.govtech.exceptionhandler;

import java.sql.SQLIntegrityConstraintViolationException;

import org.hibernate.ObjectNotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.govtech.exception.ErrorMessage;
import com.govtech.exception.InvalidFieldException;
import com.govtech.exception.InvalidInputException;
import com.govtech.exception.RoleValidationException;

@RestControllerAdvice
@Order(Integer.MIN_VALUE)
public class GlobalExceptionHandler {




	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidFieldException.class)
	public ResponseEntity<Object> handleInvalidFieldException(InvalidFieldException ex) {
		ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), ex.getLocalizedMessage());
		return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
	}

	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	public ResponseEntity<Object> handleConstraintViolation(SQLIntegrityConstraintViolationException ex) {
		ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), ex.getLocalizedMessage());
		return new ResponseEntity<>(message, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidInputException.class)
	public ResponseEntity<Object> InvalidInputException(InvalidInputException ex) {
		ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), ex.getLocalizedMessage());
		return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<Object> objectNotFoundException(ObjectNotFoundException ex) {
		ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND.value(), ex.getLocalizedMessage());
		return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);


	}
	@ExceptionHandler(RoleValidationException.class)
	public ResponseEntity<ErrorMessage> handleRoleValidationException(RoleValidationException ex) {
		ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), ex.getLocalizedMessage());
		return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);

	}

}
