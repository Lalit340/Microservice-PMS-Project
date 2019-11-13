package in.co.indusnet.rekyc.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TblRequestOtpDto{
    
	
	private long reqId;
	
	private int otpType;
	
	private String referenceId;
	
	private String mobileNo;
	
	private String email;

	private String otp;
	
	private String referenceNumber;
	
	private int otpAttempts;
	
	private LocalDateTime otpSentAt;
	
	private  String sentSmsStatus;
	
	private int sentEmailResCode;
	
	private boolean status;

}
