package in.co.indusnet.rekyc.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import in.co.indusnet.rekyc.exception.DataDecryptException;
import in.co.indusnet.rekyc.exception.DataEncryptException;
import in.co.indusnet.rekyc.exception.ServiceException;
import in.co.indusnet.rekyc.exception.FileException;
import in.co.indusnet.rekyc.exception.RequestParamException;
import in.co.indusnet.rekyc.exception.SSODataStoreException;
import in.co.indusnet.rekyc.response.ResponseData;
import in.co.indusnet.rekyc.utility.ResponseHelper;

@RestControllerAdvice
@PropertySource("classpath:constant/constant.properties")
@PropertySource("classpath:error.properties")
public class ExceptionHandler {

	@Autowired
	private Environment environment;

	@org.springframework.web.bind.annotation.ExceptionHandler(value = DataEncryptException.class)
	public ResponseEntity<ResponseData> encryptResponse(DataEncryptException re) {
		ResponseData response = ResponseHelper.responseSender(re.getMessage(), HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<ResponseData>(response, HttpStatus.BAD_REQUEST);

	}

	@org.springframework.web.bind.annotation.ExceptionHandler(value = DataDecryptException.class)
	public ResponseEntity<ResponseData> decryptResponse(DataDecryptException re) {
		ResponseData response = ResponseHelper.responseSender(re.getMessage(), HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<ResponseData>(response, HttpStatus.BAD_REQUEST);

	}

	@org.springframework.web.bind.annotation.ExceptionHandler(value = RequestParamException.class)
	public ResponseEntity<ResponseData> requestParamResponse(RequestParamException re) {
		ResponseData response = ResponseHelper.responseSender(re.getMessage(), HttpStatus.UNAUTHORIZED.value());
		return new ResponseEntity<ResponseData>(response, HttpStatus.UNAUTHORIZED);

	}

	@org.springframework.web.bind.annotation.ExceptionHandler(value = SSODataStoreException.class)
	public ResponseEntity<ResponseData> dataStoreResponse(SSODataStoreException re) {
		ResponseData response = ResponseHelper.responseSender(re.getMessage(), HttpStatus.UNAUTHORIZED.value());
		return new ResponseEntity<ResponseData>(response, HttpStatus.UNAUTHORIZED);

	}

	@org.springframework.web.bind.annotation.ExceptionHandler(value = ServiceException.class)
	public ResponseEntity<ResponseData> dataNotFoundResponse(ServiceException re) {
		ResponseData response = ResponseHelper.responseSender(re.getMessage(), HttpStatus.UNAUTHORIZED.value());
		return new ResponseEntity<ResponseData>(response, HttpStatus.UNAUTHORIZED);

	}

	@org.springframework.web.bind.annotation.ExceptionHandler(value = FileException.class)
	public ResponseEntity<ResponseData> fileExceptionHandler(FileException e) {
		ResponseData response = ResponseHelper.responseSender(e.getMessage(), HttpStatus.UNAUTHORIZED.value());
		return new ResponseEntity<ResponseData>(response, HttpStatus.UNAUTHORIZED);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
	public ResponseEntity<ResponseData> globalExceptionHandler(Exception e) {
		ResponseData response = ResponseHelper.responseSender(environment.getProperty("globalExceptionErrorMessage"),
				HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<ResponseData>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
