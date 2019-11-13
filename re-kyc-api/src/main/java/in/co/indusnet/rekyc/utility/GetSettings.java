package in.co.indusnet.rekyc.utility;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import in.co.indusnet.rekyc.exception.ServiceException;
import in.co.indusnet.rekyc.model.TblSettings;
import in.co.indusnet.rekyc.repository.SettingsRepository;

@Component
public class GetSettings {

	@Autowired
	private SettingsRepository tblSettingsRepo;

	/*
	 * this method purpose to get the error messages by error_code return
	 * 'error_message' otherwise return null .
	 */
	public List<TblSettings> getAllSettings() {

		// to get the object with errorCode
		List<TblSettings> tblSettings = tblSettingsRepo.findAll();
		
		// if condition for validate data
		if (tblSettings != null) {			
			return tblSettings;
		} else {
			return null;
		}

	}
	public String getSettingsVal(String field) {

		// to get the object with errorCode
		List<TblSettings> tblSettings = tblSettingsRepo.findAll();
		String returnType = null;
		
		// if condition for validate data
		if (tblSettings != null) {
			for (TblSettings obj : tblSettings) {
				 switch (field) { 
			        case "supportedFileFormats": 
			        	returnType = obj.getSupportedFileFormats(); 
			        break; 
			        case "fileCountLimit": 
			        	returnType = String.valueOf(obj.getFileCountLimit()); 
			        break;
			        case "maxOtpResendCount": 
			        	returnType = String.valueOf(obj.getMaxOtpResendCount()); 
			        break;
			        case "otpValidation": 
			        	returnType = String.valueOf(obj.getOtpValidation()); 
			        break;
			        case "otpWrongAttemptsLimit": 
			        	returnType = String.valueOf(obj.getOtpWrongAttemptsLimit()); 
			        break;
			        case "singleFileUploadMaxSize": 
			        	returnType = String.valueOf(obj.getSingleFileUploadMaxSize()); 
			        break;
			        case "tokenExpirationTime": 
			        	returnType = String.valueOf(obj.getTokenExpirationTime()); 
			        break; 
			        case "totalFileUploadMaxSize": 
			        	returnType = String.valueOf(obj.getTotalFileUploadMaxSize()); 
			        break; 
			        case "otpTimeout": 
			        	returnType = String.valueOf(obj.getOtpTimeout()); 
			        break; 
			        default: 
			        	returnType = ""; 
			        	throw new ServiceException("INVALID INPUT", HttpStatus.BAD_REQUEST.value());
			        } 
			 }	
		} else {
			returnType = ""; 
		}
		return returnType;
	}
	

}
