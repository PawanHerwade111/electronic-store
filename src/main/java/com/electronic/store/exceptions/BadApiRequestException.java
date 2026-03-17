package com.electronic.store.exceptions;

public class BadApiRequestException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6096507741942777251L;

	public BadApiRequestException() {
		super("Bad Request!");

	}

	public BadApiRequestException(String message) {
		super(message);

	}

}
