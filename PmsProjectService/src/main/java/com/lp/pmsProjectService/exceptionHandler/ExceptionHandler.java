package com.lp.pmsProjectService.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.lp.pmsProjectService.exception.InternalException;
import com.lp.pmsProjectService.respone.Response;
import com.lp.pmsProjectService.utility.ResponseHelper;



@RestControllerAdvice
public class ExceptionHandler {
	
	
	

	

	
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
