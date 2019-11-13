package in.co.indusnet.rekyc.exception;

import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestParamException extends RuntimeException{
        

	private static final long serialVersionUID = 1L;
	
	private int statusCode;
	
	public RequestParamException(String statusMessage , int errorCode) {
		super(statusMessage);
		this.statusCode=errorCode;
	}

}
