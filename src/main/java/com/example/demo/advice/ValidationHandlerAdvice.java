package com.example.demo.advice;

import com.example.demo.exception.ElementNotFoundException;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;

@ControllerAdvice
public class ValidationHandlerAdvice extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
       HashMap<String, String>errors=new HashMap<>();
	   ex.getBindingResult().getAllErrors().forEach((error)->{
		   String fildName=((FieldError)error).getField();
		   String message = error.getDefaultMessage();
		   errors.put(fildName, message);

	   });
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(ElementNotFoundException.class)
	public ResponseEntity<String> elementNotFoundException (ElementNotFoundException e){
		return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(PSQLException.class)
	public ResponseEntity<String> psqlException (){
		return new ResponseEntity<>("duplicate key value violates the uniqueness constraint", HttpStatus.BAD_REQUEST);
	}



}
