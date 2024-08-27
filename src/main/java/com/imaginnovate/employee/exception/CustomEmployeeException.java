package com.imaginnovate.employee.exception;

public class CustomEmployeeException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private final String errorCode;

    public CustomEmployeeException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
