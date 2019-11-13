package in.co.indusnet.soap.exceptionHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import in.co.indusnet.soap.response.ResponseData;
import in.co.indusnet.soap.utility.ResponseHelper;



@RestControllerAdvice
@PropertySource("classpath:constant/constant.properties")
public class ExceptionHandler {

	@Autowired
	private Environment environment;
	
	@org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
	public ResponseEntity<ResponseData> globalExceptionHandler(Exception e) {
		ResponseData response = ResponseHelper.responseSender(environment.getProperty("globalExceptionErrorMessage"),
				HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<ResponseData>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
