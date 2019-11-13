package com.lp.pmsUserService.utility;

import com.lp.pmsUserService.response.Response;
import com.lp.pmsUserService.response.ResponseWithToken;

public class ResponseHelper {
	
	public static Response responseSender(String statusMessage , int statusCode) {
		Response response = new Response();
		response.setStatusCode(statusCode);
		response.setStatusMessage(statusMessage);
		return response;
	}
	
	public static ResponseWithToken responseTokenSender(String statusMessage , int statusCode,String token ,String username) {
		ResponseWithToken response = new ResponseWithToken();
		response.setStatusCode(statusCode);
		response.setStatusMessage(statusMessage);
		response.setToken(token);
		response.setUsername(username);
		return response;
	}
	
	public static ResponseWithToken response(String statusMessage , int statusCode) {
		ResponseWithToken response = new ResponseWithToken();
		response.setStatusCode(statusCode);
		response.setStatusMessage(statusMessage);
		return response;
	}

}
