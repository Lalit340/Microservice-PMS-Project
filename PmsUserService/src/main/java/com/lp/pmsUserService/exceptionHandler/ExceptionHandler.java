package com.lp.pmsUserService.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.lp.pmsUserService.exceptions.InternalException;
import com.lp.pmsUserService.exceptions.LoginException;
import com.lp.pmsUserService.exceptions.PasswordException;
import com.lp.pmsUserService.exceptions.RegisterException;
import com.lp.pmsUserService.response.Response;
import com.lp.pmsUserService.utility.ResponseHelper;



@RestControllerAdvice
public class ExceptionHandler {
	
	@org.springframework.web.bind.annotation.ExceptionHandler(value = RegisterException.class)
	public ResponseEntity<Response> registrationResponse(RegisterException re){
		    Response response= ResponseHelper.responseSender(re.getMessage(), re.getStatusCode()) ;
		return new ResponseEntity<Response>(response ,HttpStatus.OK);
		
	}
	
	@org.springframework.web.bind.annotation.ExceptionHandler(value = LoginException.class)
	public ResponseEntity<Response> loginResponse(LoginException re){
		    Response response= ResponseHelper.responseSender(re.getMessage(), re.getStatusCode()) ;
		return new ResponseEntity<Response>(response ,HttpStatus.OK);
		
	}
	
	@org.springframework.web.bind.annotation.ExceptionHandler(value = PasswordException.class)
	public ResponseEntity<Response> passwordResponse( PasswordException re){
		    Response response= ResponseHelper.responseSender(re.getMessage(), re.getStatusCode()) ;
		return new ResponseEntity<Response>(response ,HttpStatus.OK);
		
	}
	
	@org.springframework.web.bind.annotation.ExceptionHandler(value = InternalException.class)
	public ResponseEntity<Response> internalResponse( InternalException re){
		    Response response= ResponseHelper.responseSender(re.getMessage(), re.getStatusCode()) ;
		return new ResponseEntity<Response>(response ,HttpStatus.OK);
		
	}
	
	
	@org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
	public ResponseEntity<Response> passwordResponse( Exception re){
		    Response response= ResponseHelper.responseSender(re.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()) ;
		return new ResponseEntity<Response>(response ,HttpStatus.OK);
		
	}

}
