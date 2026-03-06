package com.electronic.store.exceptions;

public class BadApiRequest extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6096507741942777251L;

	public BadApiRequest() {
		super("Bad Request!");

	}

	public BadApiRequest(String message) {
		super(message);

	}

}
