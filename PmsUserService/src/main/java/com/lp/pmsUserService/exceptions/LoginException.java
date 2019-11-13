package com.lp.pmsUserService.exceptions;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private int statusCode;

	public LoginException(String statusMessage, int statusCode) {
		super(statusMessage);
		this.statusCode = statusCode;
	}

}
