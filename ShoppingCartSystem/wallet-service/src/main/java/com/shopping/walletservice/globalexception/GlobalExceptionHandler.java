package com.shopping.walletservice.globalexception;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GlobalExceptionHandler {
	

	
	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<String> handleNoSuchElementException (NoSuchElementException noSuchElementException){
		return new  ResponseEntity<String>("Wallet not found ",HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException noSuchElementException){
		return new  ResponseEntity<String>("Insufficient balance",HttpStatus.BAD_REQUEST);
	}
	

}
