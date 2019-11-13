package in.co.indusnet.rekyc.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DataDecryptException extends RuntimeException{
        

	private static final long serialVersionUID = 1L;
	
	private int statusCode;
	
	public DataDecryptException(String statusMessage , int errorCode) {
		super(statusMessage);
		this.statusCode=errorCode;
	}

}
