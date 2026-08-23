package com.footdablit2310.footlib.exceptions;

/**
 * This Exception is made for stating that something is null where null is not allowed.
 */
public class IllegalNullValueException extends Exception {

	public IllegalNullValueException() {
		super();
	}

	public IllegalNullValueException(String message) {
		super(message);
	}
	public IllegalNullValueException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalNullValueException(Throwable cause) {
		super(cause);
	}
}