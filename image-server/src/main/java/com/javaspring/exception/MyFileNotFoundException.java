package com.javaspring.exception;

public class MyFileNotFoundException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6449084415188642579L;

	public MyFileNotFoundException(String message) {
        super(message);
    }

    public MyFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
