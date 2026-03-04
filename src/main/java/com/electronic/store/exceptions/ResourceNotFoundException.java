package com.electronic.store.exceptions;


public class ResourceNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2042479051222393854L;

	public ResourceNotFoundException() {
		super("Resource not found !");
	}

	public ResourceNotFoundException(String message) {
		super(message);
	}

}
