package com.lp.pmsUserService.response;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class Response {

private String statusMessage;
	
	private int statusCode;
}
