package com.advance.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.advance.entity.HttpResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UserNotFoundException.class)
	public HttpResponse userNotFoundExceptionHandler(UserNotFoundException e) {
		return HttpResponse.builder().timeStamp(LocalDateTime.now().toString()).message(e.getMessage())
				.status(HttpStatus.BAD_REQUEST).statusCode(HttpStatus.BAD_REQUEST.value()).build();
	}
	
	@ExceptionHandler(UserAlreadyExistException.class)
	public HttpResponse userAlreadyExistExceptionHandler(UserAlreadyExistException e) {
		return HttpResponse.builder().timeStamp(LocalDateTime.now().toString()).message(e.getMessage())
				.status(HttpStatus.BAD_REQUEST).statusCode(HttpStatus.BAD_REQUEST.value()).build();
	}
	
	
	@ExceptionHandler(ApiException.class)
	public HttpResponse apiExceptionHandler(ApiException e) {
		return HttpResponse.builder().timeStamp(LocalDateTime.now().toString()).message(e.getMessage())
				.status(HttpStatus.BAD_REQUEST).statusCode(HttpStatus.BAD_REQUEST.value()).build();
	}
}
