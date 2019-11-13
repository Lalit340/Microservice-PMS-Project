package in.co.indusnet.soap.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ServiceException extends RuntimeException{
        

	private static final long serialVersionUID = 1L;

	private int statusCode;
	
	public ServiceException(String statusMessage , int code) {
		super(statusMessage);
		this.statusCode = code;
	}

}
