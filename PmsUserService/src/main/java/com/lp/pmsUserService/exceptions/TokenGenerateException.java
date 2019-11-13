package com.lp.pmsUserService.exceptions;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TokenGenerateException extends RuntimeException {


	private static final long serialVersionUID = 1L;
	private int statusCode;

	public TokenGenerateException(String statusMessage, int errorCode) {
		super(statusMessage);
		this.statusCode = errorCode;
	}
}
