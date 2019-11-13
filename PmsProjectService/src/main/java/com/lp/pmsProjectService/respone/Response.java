package com.lp.pmsProjectService.respone;

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
