package com.lp.pmsUserService.exceptions;


public class RegisterException extends RuntimeException{
        

	private static final long serialVersionUID = 1L;
	
	private int statusCode;
	
	public RegisterException(String statusMessage , int errorCode) {
		super(statusMessage);
		this.statusCode=errorCode;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int errorCode) {
		this.statusCode = errorCode;
	}
}
