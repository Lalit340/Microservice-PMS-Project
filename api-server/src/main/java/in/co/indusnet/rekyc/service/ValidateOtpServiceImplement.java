package in.co.indusnet.rekyc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import in.co.indusnet.rekyc.exception.ServiceException;
import in.co.indusnet.rekyc.model.TBLRequestlog;
import in.co.indusnet.rekyc.model.TblAccountDetails;
import in.co.indusnet.rekyc.model.TblCifDetails;
import in.co.indusnet.rekyc.model.TblRequestOtp;
import in.co.indusnet.rekyc.model.TblDoc;
import in.co.indusnet.rekyc.model.TblDocType;
import in.co.indusnet.rekyc.model.TblEmailTemplate;
import in.co.indusnet.rekyc.repository.TblRequestLogRepository;
import in.co.indusnet.rekyc.repository.TblRequestOtpRepository;
import in.co.indusnet.rekyc.response.ResponseData;
import in.co.indusnet.rekyc.repository.DocTypeRepository;
import in.co.indusnet.rekyc.repository.TblAccountDetailsRepository;
import in.co.indusnet.rekyc.repository.TblCifDetailsRepository;
import in.co.indusnet.rekyc.repository.TblDocRepository;
import in.co.indusnet.rekyc.repository.TblRequestKeyRepository;
import in.co.indusnet.rekyc.utility.AllTokenCreateAndValidate;
import in.co.indusnet.rekyc.utility.CommonFunctions;
import in.co.indusnet.rekyc.utility.GetErrorMessage;
import in.co.indusnet.rekyc.utility.ResponseHelper;
import in.co.indusnet.rekyc.utility.SendMessageEmail;
import in.co.indusnet.rekyc.utility.Converter;
import in.co.indusnet.rekyc.utility.GetEmailTemplate;


@EnableTransactionManagement
@Service("ValidateOtp")
@Configuration
@PropertySource("classpath:constant/constantEnv.properties")
@PropertySource("classpath:constant/constant.properties")
public class ValidateOtpServiceImplement implements ValidateOtpService {

	@Autowired
	private TblAccountDetailsRepository tblAccountDetailsRepository;

	@Autowired
	private TblCifDetailsRepository tblCifDetailsRepository;
	
	@Autowired
	private TblRequestLogRepository tblRequestLogRepository;
	
	@Autowired
	private TblRequestOtpRepository tblRequestOtpRepository;
	
	@Autowired
	private TblRequestKeyRepository tblRequestKeyRepository;

	@Autowired
	private AllTokenCreateAndValidate allTokenCreateAndValidate;
	
	@Autowired
	private TblDocRepository tblDocRepository;
	
	@Autowired
	private DocTypeRepository docTypeRepository;
	
	@Autowired
	private GetErrorMessage getErrorMessage;
	
	@Autowired
	private GetEmailTemplate getEmailTemplate;
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private Converter  converter;
	
	@Autowired
	private SendMessageEmail sendMessageEmail;
	
	@Value("${spring.profiles.active}")
	private String activeEnvironment;
	
	@Override
	public ResponseData validateOtp(String token, String otp, long otpId) {
		String returnvalReKyc = "";
		String returnvalDormant = "";
		ResponseData responseTalismaData = null;
		String successErrorMessage = "";
		String successSmsMessage = "";
		String successEmailMessage = "";
		String successEmailSubject = "";
		String talismaResponse = "";
		if (token=="" || token== null)
			throw new ServiceException(getErrorMessage.getErrorMessages("UNABLE_TO_PROCESS_ERR"), HttpStatus.UNAUTHORIZED.value());
		Map<String, String> reqData = allTokenCreateAndValidate.validateTokenReturnValue(token);
		long reqId = Long.parseLong(reqData.get("reqId"));
		String newAddress = reqData.get("newAddress");
		String isAddressModify = reqData.get("isAddressModify");
		if (reqId == 0)
			throw new ServiceException(getErrorMessage.getErrorMessages("UNABLE_TO_PROCESS_ERR"), HttpStatus.UNAUTHORIZED.value());
		
		TblRequestOtp tblOtp = tblRequestOtpRepository.findById(otpId);
		
		if(tblOtp == null)
			throw new ServiceException(getErrorMessage.getErrorMessages("UNABLE_TO_PROCESS_ERR"), HttpStatus.UNAUTHORIZED.value());
		
		String referenceId = tblOtp.getReferenceId();
		String mobileNo = tblOtp.getMobileNo();
		String email = tblOtp.getEmail();
		String uri = environment.getProperty("soapServiceUrl")+"otp/validateotp";
		
		// create a list the headers 
		HttpHeaders headers = new HttpHeaders();
		headers.set("authToken", token);
		headers.set("serviceType", environment.getProperty("myServiceType"));
			
		MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
		map.add("referenceId", referenceId);
		map.add("mobileNo", mobileNo);
		map.add("otp", otp);
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
		
		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);
		
		int responseCode = response.getStatusCodeValue();
	    if(responseCode==200) {
			String jsonResult = response.getBody();
		    String status = converter.getParamJsonValue(jsonResult, "Status").textValue();
		    /* 123456 otp will work for other than production*/
		    if(otp.equals(environment.getProperty("penetrationTestOtp")) && !activeEnvironment.equalsIgnoreCase("production")) {
		    	status = "Success";
		    }
		    
		    if(status.equalsIgnoreCase("Success")) {
		    	/* update request log status*/
			    tblRequestLogRepository.statusUpdate(reqId,Integer.parseInt(environment.getProperty("statusOTPSuccess")));
			    
			    long cifTableId = tblCifDetailsRepository.selectedCif(reqId);
		    	String cifId = tblCifDetailsRepository.getCifCode(cifTableId);
		    	List<TblCifDetails> cifDetails = tblCifDetailsRepository.findById(cifTableId);
		    	String panNo = cifDetails.get(0).getPanNumber();
		    	int riskProfile = cifDetails.get(0).getRiskProfile();
		    	int isPanValidatedCif = cifDetails.get(0).getIsPanValidated();
		    	boolean isNr = cifDetails.get(0).isNr();
		    	
		    	/*TALISMA DATA COMMON START*/
		    	String accActivationConsent = "No";
		    	String talismaState = environment.getProperty("talisma_state_failed");
		    	String talismaStateDormant = environment.getProperty("talisma_state_failed");
		    	String idProofType = "";
		    	String addressProofType = "";		    	
		    	TblAccountDetails tblAccountDetails = tblAccountDetailsRepository.findFirstActiveAccountByCif(cifTableId);    	
		    	
				String accountNo = null;
		    	if(tblAccountDetails!=null)
		    		accountNo = tblAccountDetails.getAccountNumber();
		    		
		    	TBLRequestlog tblRequestLog = tblRequestLogRepository.findById(reqId);
		    	Boolean isAddressProofSameAsIdProof = tblRequestLog.isAddressProofSameAsIdProof();
		    	
		    	ArrayList<String> idProofArray = new ArrayList<String>();
		    	List<TblDoc> documentList = tblDocRepository.findByReqIdAndDocProofTypeAndStatus(reqId,1,1);
		    	
		    	if(!documentList.isEmpty())
		    	{
		    		for (TblDoc document : documentList) {
						long docTypeId = document.getDocTypeId();
						Optional<TblDocType> docTypeList = docTypeRepository.findById(docTypeId);
						idProofArray.add(docTypeList.get().getCode());
					}
		    		idProofType = CommonFunctions.convertStringArrayToString(idProofArray, ",");
		    	}
		    	
		    	ArrayList<String> addressProofArray = new ArrayList<>();
		    	List<TblDoc> documentList2 = tblDocRepository.findByReqIdAndDocProofTypeAndStatus(reqId,2,1);
		    	
		    	if(!documentList2.isEmpty())
		    	{
		    		for (TblDoc document2 : documentList2) {
						long docTypeId2 = document2.getDocTypeId();
						Optional<TblDocType> docTypeList2 = docTypeRepository.findById(docTypeId2);
						addressProofArray.add(docTypeList2.get().getCode());
					}
		    		addressProofType = CommonFunctions.convertStringArrayToString(addressProofArray, ",");
		    	}
		    	else
		    	{
		    		if(idProofType.length()!= 0 && isAddressProofSameAsIdProof)
			    	{
			    		addressProofType = idProofType;
			    	}
		    	}
		    	/** TALISMA DATA COMMON END **/
			    System.out.println("cifTableId=======================>>>>>"+cifTableId);
		    	
			    /** CONDITIONS FOR CBS API (REKYC SUBMISSON AND DORMANT ACCOUNT ACTIVE)  START **/
		    	
		    	/* for dormant account activate request*/
		    	String isDormantApiCalled = "no";  
			    List<TblAccountDetails> hasDormantAccount = tblAccountDetailsRepository.findDormantAccountWithActivateRequest(environment.getProperty("dormantAccountStatus"), cifTableId);
			    System.out.println("------------------------->>>> "+hasDormantAccount);
			    String dormantAccountsString;
			    ArrayList<String> dormantAccountsList = new ArrayList<String>(); 
			   
				if(hasDormantAccount!=null && !hasDormantAccount.isEmpty()) {
					 for (TblAccountDetails tblDormantAccount : hasDormantAccount) {
					    dormantAccountsList.add(tblDormantAccount.getAccountNumber()); 
					 }
					dormantAccountsString = String.join(", ", dormantAccountsList);
					accActivationConsent = "Yes "+dormantAccountsString;
					if(isPanValidatedCif == 1) {
						returnvalDormant = this.activateDormantAccount(token, reqId, hasDormantAccount);
						if(returnvalDormant.equals("ERROR"))
				    		throw new ServiceException(getErrorMessage.getErrorMessages("UNABLE_TO_PROCESS_ERR"), HttpStatus.UNAUTHORIZED.value());
						if(returnvalDormant.equals("S")) {
							talismaStateDormant = environment.getProperty("talisma_state_resolved");
						}
						else {
							throw new ServiceException(getErrorMessage.getErrorMessages("UNABLE_TO_PROCESS_ERR"), HttpStatus.UNAUTHORIZED.value());
						}
					}
					else {
						talismaStateDormant = environment.getProperty("talisma_state_failed");
					}
					if(isNr) {
						talismaStateDormant = environment.getProperty("talisma_state_failed");
					}	
					isDormantApiCalled = "yes";
					
				}
			    			    
			    if(riskProfile == Integer.parseInt(environment.getProperty("lowRisk"))) {
			    	
			    	if(isAddressModify=="no" && isPanValidatedCif==1) {
			    		// call rekyc submission
			    		returnvalReKyc = this.reKycSubmission(token, reqId);
				    	
			    		if(returnvalReKyc.equals("ERROR"))
				    		throw new ServiceException(getErrorMessage.getErrorMessages("UNABLE_TO_PROCESS_ERR"), HttpStatus.UNAUTHORIZED.value());		    					    	
				    	
				    	if(returnvalReKyc.equals("S")) {
				    		talismaState = environment.getProperty("talisma_state_resolved");
				    	}
				    	else {
							throw new ServiceException(getErrorMessage.getErrorMessages("UNABLE_TO_PROCESS_ERR"), HttpStatus.UNAUTHORIZED.value());
						}
				    	successErrorMessage = getErrorMessage.getErrorMessages("LR_PANV_ANC_SUCCESS");
				    	if(isDormantApiCalled.equals("yes"))
				    		successErrorMessage = getErrorMessage.getErrorMessages("LR_DA_SUCCESS");
			    	}
			    	else {
			    		if(isAddressModify=="yes" || isPanValidatedCif==2 || isPanValidatedCif==3) {
			    			talismaState = environment.getProperty("talisma_state_failed");
			    			
			    			successErrorMessage = getErrorMessage.getErrorMessages("LR_AC_SUCCESS");
					    	if(isDormantApiCalled.equals("yes"))
					    		successErrorMessage = getErrorMessage.getErrorMessages("LR_AC_DA_SUCCESS");
			    		}
			    		else {
			    			successErrorMessage = getErrorMessage.getErrorMessages("LR_PANV_ANC_SUCCESS");
			    			if(isDormantApiCalled.equals("yes"))
					    		successErrorMessage = getErrorMessage.getErrorMessages("LR_DA_SUCCESS");
			    		}
			    	}
			    	
			    	
			    	
			    }
			    else {
			    	talismaState = environment.getProperty("talisma_state_resolved");
			    	successErrorMessage = getErrorMessage.getErrorMessages("MRHR_SUCCESS");
			    	if(isDormantApiCalled.equals("yes"))
			    		successErrorMessage = getErrorMessage.getErrorMessages("MRHR_DA_SUCCESS");
			    }
			    
			    if(isDormantApiCalled.equals("yes"))
			    	talismaState = talismaStateDormant;
			    
			    
			    			    		    
				 /** CONDITIONS FOR CBS API (REKYC SUBMISSON AND DORMANT ACCOUNT ACTIVE) END **/  		    			    
		    	
		    	
		    	// here talisma will be called. need to be changed
		    	talismaResponse = this.createTalisma(token , reqId,
		    			accountNo, cifId, panNo, 
		    			accActivationConsent,addressProofType,
		    			idProofType, mobileNo,email,
		    			newAddress, 
		    			talismaState,cifTableId);
		    	if(talismaResponse.equals("ERROR"))
		    		throw new ServiceException(getErrorMessage.getErrorMessages("UNABLE_TO_PROCESS_ERR"), HttpStatus.UNAUTHORIZED.value());
		    	
		    	String responseStatus = converter.getParamJsonValue(talismaResponse, "createServiceRequestResult").textValue();
				String interactionId = String.valueOf(converter.getParamJsonValue(talismaResponse, "interactionID"));
				String errorCode = converter.getParamJsonValue(talismaResponse, "errorCode").textValue();
				String errorMessage = converter.getParamJsonValue(talismaResponse, "errorMessage").textValue();
		    	Map<String, String> responseData = new HashMap<>();
		    	responseData.put("responseStatus", responseStatus);
		    	responseData.put("interactionId", interactionId);
		    	responseData.put("errorCode", errorCode);
		    	responseData.put("errorMessage", errorMessage);
		    	successErrorMessage = successErrorMessage.replace("[COMMUNICATION_HOUR]", environment.getProperty("successMessageCommunicationHour"));
				responseTalismaData = ResponseHelper.responseSenderData(successErrorMessage, HttpStatus.OK.value(), responseData);
				
				/***** SMS Message START ****/
			    if(riskProfile == Integer.parseInt(environment.getProperty("lowRisk"))) {
			    	if(talismaState.equals(environment.getProperty("talisma_state_resolved"))) {
			    		if(isDormantApiCalled.equals("yes"))
			    			successSmsMessage = getEmailTemplate.getEmailTemplateContent("REKYC_CONFIRMATION_SMS_LR_STP_DA").get(1);
			    		else
			    			successSmsMessage = getEmailTemplate.getEmailTemplateContent("REKYC_CONFIRMATION_SMS_LR_STP").get(1);
			    	}
			    	else {
			    		if(isAddressModify=="yes") {
				    		successSmsMessage = getEmailTemplate.getEmailTemplateContent("REKYC_CONFIRMATION_SMS_LR_NONSTP_AC").get(1);
			    			if(isDormantApiCalled.equals("yes"))
				    			successSmsMessage = getEmailTemplate.getEmailTemplateContent("REKYC_CONFIRMATION_SMS_LR_NONSTP_AC_DA").get(1);
				    		}
			    	}
			    }
			    else {		    		
		    			successSmsMessage = getEmailTemplate.getEmailTemplateContent("REKYC_CONFIRMATION_SMS_MR_HR_NON_STP").get(1);
		    			if(isDormantApiCalled.equals("yes"))
			    			successSmsMessage = getEmailTemplate.getEmailTemplateContent("REKYC_CONFIRMATION_SMS_MR_HR_NON_STP_DA").get(1);
		    	}
			    
			    successSmsMessage = successSmsMessage.replace("[COMMUNICATION_HOUR]", environment.getProperty("smsCommunicationHour"));
			    successSmsMessage = successSmsMessage.replace("[REFERENCE_ID]", interactionId);
			    /***** SMS Message END ****/
			    
			    
			    /***** EMAIL Message START ****/
			    
			    if(riskProfile == Integer.parseInt(environment.getProperty("lowRisk"))) {
			    	if(talismaState.equals(environment.getProperty("talisma_state_resolved"))) {
			    		if(isDormantApiCalled.equals("yes")) {
			    			successEmailSubject = getEmailTemplate.getEmailTemplateContent("REKYC_CONFIRMATION_MAIL_LR_STP_DA").get(0);
			    			successEmailMessage = getEmailTemplate.getEmailTemplateContent("REKYC_CONFIRMATION_MAIL_LR_STP_DA").get(1);
			    		}
			    		else {
			    			successEmailSubject = getEmailTemplate.getEmailTemplateContent("REKYC_CONFIRMATION_MAIL_LR_STP").get(0);
			    			successEmailMessage = getEmailTemplate.getEmailTemplateContent("REKYC_CONFIRMATION_MAIL_LR_STP").get(1);
			    		}
			    	}
			    	else {
			    		if(isAddressModify=="yes") {
			    			successEmailSubject = getEmailTemplate.getEmailTemplateContent("REKYC_CONFIRMATION_MAIL_LR_NONSTP_AC").get(0);
			    			successEmailMessage = getEmailTemplate.getEmailTemplateContent("REKYC_CONFIRMATION_MAIL_LR_NONSTP_AC").get(1);
			    			if(isDormantApiCalled.equals("yes")) {
			    				successEmailSubject = getEmailTemplate.getEmailTemplateContent("REKYC_CONFIRMATION_MAIL_LR_NONSTP_AC_DA").get(0);
			    				successEmailMessage = getEmailTemplate.getEmailTemplateContent("REKYC_CONFIRMATION_MAIL_LR_NONSTP_AC_DA").get(1);
			    			}	    				
				    	}
			    		
			    	}
			    }
			    
			    else {		    		
			    	successEmailSubject = getEmailTemplate.getEmailTemplateContent("REKYC_CONFIRMATION_MAIL_MR_HR_NON_STP").get(0);
			    	successEmailMessage = getEmailTemplate.getEmailTemplateContent("REKYC_CONFIRMATION_MAIL_MR_HR_NON_STP").get(1);
		    			if(isDormantApiCalled.equals("yes")) {
		    				successEmailSubject = getEmailTemplate.getEmailTemplateContent("REKYC_CONFIRMATION_MAIL_MR_HR_NON_STP_DA").get(0);
		    				successEmailMessage = getEmailTemplate.getEmailTemplateContent("REKYC_CONFIRMATION_MAIL_MR_HR_NON_STP_DA").get(1);
		    			}
		    	}
			    
			    successEmailMessage = successEmailMessage.replace("[COMMUNICATION_HOUR]", environment.getProperty("emailCommunicationHour"));
			    successEmailMessage = successEmailMessage.replace("[REFERENCE_ID]", interactionId);
			    successEmailMessage = successEmailMessage.replace("[REKYC_RELATED_CONTACT_EMAIL]", environment.getProperty("reachUsEmail"));
			    successEmailMessage = successEmailMessage.replace("[REKYC_RELATED_CONTACT_NO]", environment.getProperty("phoneBankingNumber"));
			    
			    /***** EMAIL Message END ****/
			    
			    
				/* SUCCESS SMS AND SUCCESS EMAIL */
				if(mobileNo!=null && !mobileNo.equals("")) {
					this.sendSuccessSMS(token, mobileNo, successSmsMessage);
				}

				if(email!=null && !email.equals("")) {
					this.sendSuccessEmail(token, email, successEmailSubject, successEmailMessage);
				}
				
				/* update token status here*/
		    	tblRequestKeyRepository.statusUpdate(reqId,0);
		    }
		    else {
		    	/* update request log status*/
			    tblRequestLogRepository.statusUpdate(reqId,Integer.parseInt(environment.getProperty("statusOTPFailed")));
		    	throw new ServiceException(getErrorMessage.getErrorMessages("INVALID_OTP_ERR"), HttpStatus.UNAUTHORIZED.value());
		    }
	    }
	    else {
	    	throw new ServiceException(getErrorMessage.getErrorMessages("UNABLE_TO_PROCESS_ERR"), HttpStatus.UNAUTHORIZED.value());
	    }
	    
		return responseTalismaData;
	}
	
	private String reKycSubmission(String token, long reqId){
		
		String returnval = "ERROR";
		long cifId = tblCifDetailsRepository.selectedCif(reqId);
		String cif = tblCifDetailsRepository.getCifCode(cifId);
		
		String uri = environment.getProperty("soapServiceUrl")+"finacle/rekycsubmission";
		
		// create a list the headers 
		HttpHeaders headers = new HttpHeaders();
		headers.set("authToken", token);
		headers.set("serviceType", environment.getProperty("myServiceType"));
			
		MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
		map.add("cifId", cif);
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
		
		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);
		System.out.println(response);
		int responseCode = response.getStatusCodeValue();
		
		if(responseCode==200) {
			/* update request log status*/
		    tblRequestLogRepository.statusUpdate(reqId,Integer.parseInt(environment.getProperty("statusReKycSubmission")));
			String jsonResult = response.getBody();
		    String flag = converter.getParamJsonValue(jsonResult, "SuccessOrFailure").textValue();
		    if(flag.equalsIgnoreCase("Y")) {
		    	returnval = "S";
		    }		    
		    else {
		    	returnval = "F";
		    }
		}
		return returnval;
	}
	
	private String activateDormantAccount(String token, long reqId, List<TblAccountDetails> tblAccountDetails){
		String returnval = "NOT_CALLED";
		
		if(tblAccountDetails!=null && !tblAccountDetails.isEmpty()) {
			for (TblAccountDetails tblAccount : tblAccountDetails) {
				String uri = environment.getProperty("soapServiceUrl")+"finacle/activatedormantaccount";
				
				// create a list the headers 
				HttpHeaders headers = new HttpHeaders();
				headers.set("authToken", token);
				headers.set("serviceType", environment.getProperty("myServiceType"));
				
					
				MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
				map.add("accId", tblAccount.getAccountNumber());
				
				HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
				
				RestTemplate restTemplate = new RestTemplate();
				
				ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);
				
				int responseCode = response.getStatusCodeValue();
				if(responseCode==200) {
					/* update request log status*/
				    tblRequestLogRepository.statusUpdate(reqId,Integer.parseInt(environment.getProperty("statusDormantAccountActive")));
				    String jsonResult = response.getBody();
				    returnval = converter.getParamJsonValue(jsonResult, "STATUS").textValue();
				}
				else {
					return "ERROR";
				}
			}	
		}		
		return returnval;
	}
	private String createTalisma(String token , long reqId,
			String accountNo, String cifId, String panNo, 
			String accActivationConsent,String addressProofType,
			String idProofType, String mobileNumber,String emailId,
			String newAddress, 
			String interactionState,long cifTableId) {
		String uri = environment.getProperty("soapServiceUrl")+"talisma/createservice/rekyc";
		System.out.println("uri----------------------------------"+uri);
		// create a list the headers 
		HttpHeaders headers = new HttpHeaders();
		headers.set("token", token);
		headers.set("serviceType", environment.getProperty("myServiceType"));
					
		MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
		map.add("cifId", cifId);
		map.add("panNo", panNo);
		map.add("accActivationConsent", accActivationConsent);
		map.add("addressProofType", addressProofType);
		map.add("idProofType", idProofType);
		map.add("mobileNumber", mobileNumber);
		map.add("emailId", emailId);
		map.add("verificationMode", environment.getProperty("OTP_VERIFICATION_MODE"));
		map.add("newAddress", newAddress);
		map.add("interactionState", interactionState);
		map.add("cifTableId", String.valueOf(cifTableId));
		map.add("accountNo", accountNo);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
		
		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);
		
		int responseCode = response.getStatusCodeValue();

		if(responseCode==200) {		
			/* update request log status*/
		    tblRequestLogRepository.statusUpdate(reqId,Integer.parseInt(environment.getProperty("statusUpdateonTalisma")));
		    return response.getBody().toString();
		}
		else {
			return "ERROR";
		}
	}
	private void sendSuccessEmail(String token, String email, String subject, String message) {
		String emailType = environment.getProperty("emailTypeSuccess");
		sendMessageEmail.sendEmail(token, email, subject, message, environment.getProperty("myServiceType"), emailType);
	}
	private void sendSuccessSMS(String token, String mobileNo, String message) {
		sendMessageEmail.sendSMS(token, mobileNo, message, environment.getProperty("myServiceType"));
	}

}
