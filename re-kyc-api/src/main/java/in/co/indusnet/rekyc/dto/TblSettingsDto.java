package in.co.indusnet.rekyc.dto;

import lombok.Data;


@Data
public class TblSettingsDto{
    

	private  int fileCountLimit;
	
	private  int singleFileUploadMaxSize;
	
	private  int totalFileUploadMaxSize;
	
	private  String[] supportedFileFormats;
	
	private boolean isOverseasShow;
	
	//private  int otp_wrong_attempts_limit;
	
	//private  int otp_timeout;
	
	//private  int otp_validation;
	
	//private  int max_otp_resend_count;

	//private  int token_expiration_time;
}
