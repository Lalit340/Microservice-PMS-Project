package com.lp.pmsUserService.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseWithToken {
	
	private String statusMessage;
	
	private int statusCode;
	
	private String token;
	
	private String username;

}
