package in.co.indusnet.rekyc.service;

import org.springframework.stereotype.Service;

import in.co.indusnet.rekyc.response.ResponseData;

@Service
public interface ValidateOtpService {
	
	public ResponseData validateOtp(String token, String otp, long otpId);
	
	//public Boolean reKycSubmission(String token, long reqId);
	
	//public Boolean activateDormantAccount(String token, long reqId);
	
	/*public String createTalisma(String token , long reqId, 
			String accountNo, String cifId, String panNo, 
			String accActivationConsent,String addressProofType,
			String idProofType, String mobileNumber,String emailId,
			String newAddress, 
			String interactionState,long cifTableId);*/
}
