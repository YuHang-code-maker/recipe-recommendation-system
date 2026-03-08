package com.tus.RRS.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.tus.RRS.dto.ErrorResponseDto;

@ControllerAdvice
public class GlobalLogicExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(ResourceNotFoundException e, WebRequest webRequest){
		ErrorResponseDto errorResponseDto = new ErrorResponseDto(
				webRequest.getDescription(false),
				HttpStatus.NOT_FOUND,
				e.getMessage(),
				LocalDateTime.now()
		);
		return new ResponseEntity<>(errorResponseDto,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(DuplicateResourceException.class)
	public ResponseEntity<ErrorResponseDto> handleDuplicateResourceException(DuplicateResourceException e, WebRequest webRequest){
		ErrorResponseDto errorResponseDto = new ErrorResponseDto(
				webRequest.getDescription(false),
				HttpStatus.BAD_REQUEST,
				e.getMessage(),
				LocalDateTime.now()
		);
		return new ResponseEntity<>(errorResponseDto,HttpStatus.BAD_REQUEST);
	}
}
