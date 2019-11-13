package in.co.indusnet.rekyc.service;


import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
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


import in.co.indusnet.rekyc.dto.AccountList;
import in.co.indusnet.rekyc.dto.DormantAccountList;
import in.co.indusnet.rekyc.dto.CifList;
import in.co.indusnet.rekyc.dto.CustomerDetails;
import in.co.indusnet.rekyc.exception.ServiceException;
import in.co.indusnet.rekyc.model.TBLRequestlog;
import in.co.indusnet.rekyc.model.TblAccountDetails;
import in.co.indusnet.rekyc.model.TblCifDetails;
import in.co.indusnet.rekyc.model.TblRequestOtp;
import in.co.indusnet.rekyc.model.TblEmailTemplate;
import in.co.indusnet.rekyc.repository.TblRequestLogRepository;
import in.co.indusnet.rekyc.repository.TblRequestOtpRepository;
import in.co.indusnet.rekyc.response.GetCifDetailsApi;
import in.co.indusnet.rekyc.response.GetCifListApi;
import in.co.indusnet.rekyc.response.GetDormantAccountsApi;
import in.co.indusnet.rekyc.response.ResponseData;
import in.co.indusnet.rekyc.repository.TblAccountDetailsRepository;
import in.co.indusnet.rekyc.repository.TblCifDetailsRepository;
import in.co.indusnet.rekyc.utility.AllTokenCreateAndValidate;
import in.co.indusnet.rekyc.utility.CommonFunctions;
import in.co.indusnet.rekyc.utility.GetErrorMessage;
import in.co.indusnet.rekyc.utility.GetSettings;
import in.co.indusnet.rekyc.utility.ResponseHelper;
import in.co.indusnet.rekyc.utility.SendMessageEmail;
import in.co.indusnet.rekyc.utility.Converter;
import in.co.indusnet.rekyc.utility.GetAccountType;
import in.co.indusnet.rekyc.utility.GetEmailTemplate;

@Configuration
@EnableTransactionManagement
@Service("TblCif")
@PropertySource("classpath:constant/constantEnv.properties")
@PropertySource("classpath:constant/constant.properties")
public class TblCifServiceImplement implements TblCifService {

	@Autowired
	private TblAccountDetailsRepository tblAccountDetailsRepository;

	@Autowired
	private TblCifDetailsRepository tblCifDetailsRepository;
	
	@Autowired
	private TblRequestLogRepository tblRequestLogRepository;
	
	@Autowired
	private TblRequestOtpRepository tblRequestOtpRepository;

	@Autowired
	private AllTokenCreateAndValidate allTokenCreateAndValidate;
	
	@Autowired
	private GetErrorMessage getErrorMessage;
	
	@Autowired
	private GetEmailTemplate getEmailTemplate;
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private Converter  converter;
	
	@Autowired
	private GetSettings getSettings;
	
	@Autowired
	private SendMessageEmail sendMessageEmail;
	
	@Autowired
	private GetAccountType accType;
	
	@Value("${spring.profiles.active}")
	private String activeEnvironment;
	
	/*
	 * this method purpose to get CIF list
	 */
	@Override
	public GetCifListApi getCifList(String token) {
		if (token.length()==0 || token== null)
			throw new ServiceException(getErrorMessage.getErrorMessages("UNABLE_TO_PROCESS_ERR"), HttpStatus.UNAUTHORIZED.value());

		// Response object creation
		GetCifListApi tblCifAndAccountDetails = new GetCifListApi();

		List<TblAccountDetails> tblAccountDetails = null;

		/*long reqId = allTokenCreateAndValidate.validateToken(token);
		// if condition to validate token return data
		*/

		Map< String,String> data = allTokenCreateAndValidate.validateTokenReturnValue(token);
		long reqId = Long.parseLong(data.get("reqId"));
		
		if (reqId == 0)
			throw new ServiceException(getErrorMessage.getErrorMessages("UNABLE_TO_PROCESS_ERR"), HttpStatus.UNAUTHORIZED.value());
		
		/*check if the cif are already saved or not and do the function accordingly*/
		int status = Integer.parseInt(data.get("status"));
		
		if(status<1) {
			Boolean saveStat = this.saveLinkedCifToDb(token, environment.getProperty("myServiceType"));
			System.out.println("saveStat-------------------->>>>>>"+saveStat);
			if (!saveStat)
				throw new ServiceException(getErrorMessage.getErrorMessages("UNABLE_TO_PROCESS_ERR"), HttpStatus.UNAUTHORIZED.value());
		}
		
		
		List<TblCifDetails> tblCifDetails;

		long tblCifDetailsCount = tblCifDetailsRepository.countBytblRequestLog_rId(reqId);
		
		// if condition to validate TblCifDetails class object
		if (tblCifDetailsCount==0)
			throw new ServiceException(getErrorMessage.getErrorMessages("INVALID_CIF"), HttpStatus.UNAUTHORIZED.value() );

		tblCifDetails = tblCifDetailsRepository.findBytblRequestLog_rIdOrderByRiskProfileAsc(reqId);
		
		TBLRequestlog tblRequestLogDet = tblRequestLogRepository.findById(reqId);
		
		
		
		//TBLRequestlog tblRequestLogDet = tblRequestLog.get(); 
		
		String newAddress = tblRequestLogDet.getAddress();
		
		//boolean isAddressModify = tblRequestLogDet.isAddressModify();
		
		boolean isAddressModify = false;
		System.out.println("request log ======================>>>> "+isAddressModify);

		//tblCifDetails = tblCifDetailsRepository.findBytblRequestLog_rId(reqId);
			for (TblCifDetails obj : tblCifDetails) {
				// Object creation
				CifList cifList = new CifList();
				cifList.setId(obj.getId());
				cifList.setCifCode("Customer ID: CIF "+obj.getCifCode().replaceAll("\\d(?=(?:\\D*\\d){4})", "x"));
				cifList.setSelected(obj.isSelected());
				cifList.setRiskProfile(obj.getRiskProfile());
				tblCifAndAccountDetails.getCifList().add(cifList);
				
				
				/* get customer details and account list of selected CIF*/
				if (obj.isSelected()) {
					CustomerDetails customerDetails = new CustomerDetails();
					String oldAddress = obj.getCommunicationAddress();
					
					// insertion to the object					
					customerDetails.setCustomerName(CommonFunctions.capitalizeString(obj.getCustomerName()));
					customerDetails.setPanNumber(obj.getPanNumber());
					customerDetails.setIsPanValidated(obj.getIsPanValidated());
					customerDetails.setOldCommunicationAddress(oldAddress);
					customerDetails.setNewCommunicationAddress(newAddress);
					
					isAddressModify = tblRequestLogDet.isAddressModify();
					customerDetails.setIsAddressModified(isAddressModify);
					tblCifAndAccountDetails.setCustomerDetails(customerDetails);
					
					// get object through selected item
					tblAccountDetails = tblAccountDetailsRepository.findByTblCifDetails(obj);
					for (TblAccountDetails tblAccount : tblAccountDetails) {
						if (tblAccount.isDisplay()) {
							// Object creation
							AccountList accountList = new AccountList();
							// insertion to the object
							accountList.setId(tblAccount.getId());
							accountList.setCustomerName(tblAccount.getTblCifDetails().getCustomerName());
							accountList.setAccountNo(tblAccount.getAccountNumber().replaceAll("\\d(?=(?:\\D*\\d){4})", "x"));
							accountList.setAccountType(accType.getTblAccountType(tblAccount.getAccountType()).get(1));
							accountList.setCifId(obj.getId());
							tblCifAndAccountDetails.getAccountList().add(accountList);
						}
					} // for
				
					long isDormantPresent = tblAccountDetailsRepository.countByCifwithStatus(environment.getProperty("dormantAccountStatus"), obj.getId(), 0);
					
					String isDormantAccountPresent;
					if (isDormantPresent == 0)
						isDormantAccountPresent = "no";
					else
						isDormantAccountPresent = "yes";
					
					tblCifAndAccountDetails.setHasDormantAccount(isDormantAccountPresent);
				} // if
			} // for
			return tblCifAndAccountDetails;

		
		//return tblCifAndAccountDetails;
	}// if else
	
	
	/*
	 * this method purpose to get details of CIF
	 */
	@Override
	public GetCifDetailsApi getCifDetails(String token, long cifId) {
		if (token.length()==0 || token== null)
			throw new ServiceException(getErrorMessage.getErrorMessages("UNABLE_TO_PROCESS_ERR"), HttpStatus.UNAUTHORIZED.value());

		// Response object creation
		GetCifDetailsApi tblCifAndAccountDetails = new GetCifDetailsApi();

		List<TblAccountDetails> tblAccountDetails = null;

		long reqId = allTokenCreateAndValidate.validateToken(token);
		// if condition to validate token return data
		if (reqId == 0)
			throw new ServiceException(getErrorMessage.getErrorMessages("UNABLE_TO_PROCESS_ERR"), HttpStatus.UNAUTHORIZED.value());

		List<TblCifDetails> tblCifDetails;

		long tblCifDetailsCount;
		tblCifDetailsCount = tblCifDetailsRepository.countByidreqid(cifId, reqId);

		// if condition to validate TblCifDetails class object
		if (tblCifDetailsCount==0)
			throw new ServiceException(getErrorMessage.getErrorMessages("INVALID_CIF"), HttpStatus.UNAUTHORIZED.value() );

		tblCifDetails = tblCifDetailsRepository.findByidreqid(cifId, reqId);
		
		TBLRequestlog tblRequestLogDet = tblRequestLogRepository.findById(reqId);
		
		//TBLRequestlog tblRequestLogDet = tblRequestLog.get(); 
		
		String newAddress = tblRequestLogDet.getAddress();
		
		boolean isAddressModify = false;
		
		tblCifDetails = tblCifDetailsRepository.findBytblRequestLog_rId(reqId);
			
		for (TblCifDetails obj : tblCifDetails) {				
				if (obj.getId() == cifId) {
					CustomerDetails customerDetails = new CustomerDetails();
					String oldAddress = obj.getCommunicationAddress();
					// insertion to the object
					String customerName = obj.getCustomerName();
					
					customerDetails.setCustomerName(CommonFunctions.capitalizeString(customerName));
					customerDetails.setPanNumber(obj.getPanNumber());
					customerDetails.setIsPanValidated(obj.getIsPanValidated());
					customerDetails.setOldCommunicationAddress(oldAddress);
					customerDetails.setNewCommunicationAddress(newAddress);
										
					isAddressModify = tblRequestLogDet.isAddressModify();					
					customerDetails.setIsAddressModified(isAddressModify);

					tblCifAndAccountDetails.setCustomerDetails(customerDetails);
					// get object through selected item
					tblAccountDetails = tblAccountDetailsRepository.findByTblCifDetails(obj);
					for (TblAccountDetails tblAccount : tblAccountDetails) {
						if (tblAccount.isDisplay()) {
							// Object creation
							AccountList accountList = new AccountList();
							// insertion to the object
							accountList.setId(tblAccount.getId());
							accountList.setCustomerName(tblAccount.getTblCifDetails().getCustomerName());
							accountList.setAccountNo(tblAccount.getAccountNumber().replaceAll("\\d(?=(?:\\D*\\d){4})", "x"));
							accountList.setAccountType(accType.getTblAccountType(tblAccount.getAccountType()).get(1));
							accountList.setCifId(cifId);
							tblCifAndAccountDetails.getAccountList().add(accountList);
						}
					} // for
				
					long isDormantPresent = tblAccountDetailsRepository.countByCifwithStatus(environment.getProperty("dormantAccountStatus"), cifId, 0);
					
					String isDormantAccountPresent;
					if (isDormantPresent == 0)
						isDormantAccountPresent = "no";
					else
						isDormantAccountPresent = "yes";
					
					tblCifAndAccountDetails.setHasDormantAccount(isDormantAccountPresent);
				} // if
			} // for
			return tblCifAndAccountDetails;

		
		//return tblCifAndAccountDetails;
	}// if else

	/** It will be called to proceed second screen. 
	 * It will check if any dormant account is present or not and also save the new address, if changed. **/
	@Override
	public ResponseData saveSelectedCif(String token, long cifId, String newAddress, boolean isAddressModify) {
		
		if (token.length()==0)
			throw new ServiceException(getErrorMessage.getErrorMessages("UNABLE_TO_PROCESS_ERR"), HttpStatus.UNAUTHORIZED.value());
		/**** validate token ****/
		long reqId = allTokenCreateAndValidate.validateToken(token);
		if (reqId == 0)
			throw new ServiceException(getErrorMessage.getErrorMessages("UNABLE_TO_PROCESS_ERR"), HttpStatus.UNAUTHORIZED.value());
		
		long tblCifDetailsCount = tblCifDetailsRepository.countByidreqid(cifId, reqId);
		
		// if CIF is not belongs to this token
		if (tblCifDetailsCount==0)
			throw new ServiceException(getErrorMessage.getErrorMessages("INVALID_CIF"), HttpStatus.UNAUTHORIZED.value());
		
		/*if(isAddressModify && (newAddress!= null))*/
			tblRequestLogRepository.newAddressAdded(newAddress, isAddressModify, reqId);

		/** Set is_selected as true for selected CIF in database **/	
		tblCifDetailsRepository.markEntryAsSelected(cifId);
		List<TblCifDetails> selectedCif = tblCifDetailsRepository.findById(cifId);
		int panValidated = 0;
		for (TblCifDetails obj : selectedCif) {
			panValidated = obj.getIsPanValidated();
		}
		
//		if(panValidated == 0 && !pan.equals("")) {
//			String panValidData = this.isPanValidated(token, pan, environment.getProperty("myServiceType"));
//			
//    		if(panValidData.equals("ERROR")) {
//	    		throw new ServiceException(getErrorMessage.getErrorMessages("UNABLE_TO_PROCESS_ERR"), HttpStatus.UNAUTHORIZED.value());
//    		}
//    		else {
//    			String panStatus = converter.getParamJsonValue(panValidData, "panStatus").textValue();
//    			int setPanValidated = 2;
//    			if(panStatus.equalsIgnoreCase("E")) {
//    				String firstName = converter.getParamJsonValue(panValidData, "firstName").textValue();
//    				String middleName = converter.getParamJsonValue(panValidData, "middleName").textValue();
//    				String lastName = converter.getParamJsonValue(panValidData, "lastName").textValue();
//    				String totalName = firstName+" "+middleName+" "+lastName;
//    				totalName = totalName.replaceAll("\\s{2,}", " ").trim();
//    				String fullName = firstName+" "+lastName;
//    				fullName = fullName.replaceAll("\\s{2,}", " ").trim();
//    				String cifName = tblCifDetailsRepository.getSelectedCifName(reqId);
//    				
//    				if(cifName.equalsIgnoreCase(totalName) || cifName.equalsIgnoreCase(fullName)) {
//    					setPanValidated = 1;
//    				}
//    			}
//	    			List<TblCifDetails> cifDetails = tblCifDetailsRepository.findById(cifId);
//					for (TblCifDetails objcif : cifDetails) {
//						objcif.setIsPanValidated(setPanValidated);
//						tblCifDetailsRepository.save(objcif);
//					}
//					panValidated = setPanValidated;
//    		}
//    			
//    	}
		
		/* update request log status*/
    	tblRequestLogRepository.statusUpdate(reqId,Integer.parseInt(environment.getProperty("statusUpdateSelectedCIF")));	
		 Map<String, String> responseData = new HashMap<>();
		 responseData.put("isPanValidated", String.valueOf(panValidated));
		
		ResponseData response = ResponseHelper.responseSenderData(getErrorMessage.getErrorMessages("DATA_SAVED"), HttpStatus.OK.value(), responseData);
		return response;				
		
	}
	/** It will be called for the dormant account list **/
	@Override
	public GetDormantAccountsApi getDormantList(String token) {
		if (token.length()==0)
			throw new ServiceException(getErrorMessage.getErrorMessages("UNABLE_TO_PROCESS_ERR"), HttpStatus.UNAUTHORIZED.value());
		/**** validate token ****/
		long reqId = allTokenCreateAndValidate.validateToken(token);
		if (reqId == 0)
			throw new ServiceException(getErrorMessage.getErrorMessages("UNABLE_TO_PROCESS_ERR"), HttpStatus.UNAUTHORIZED.value());
		
		// Response object creation
				GetDormantAccountsApi tblCifAndAccountDetails = new GetDormantAccountsApi();
				List<TblAccountDetails> tblAccountDetails = null;
											
				long cifId = tblCifDetailsRepository.selectedCif(reqId);
								
				tblAccountDetails = tblAccountDetailsRepository.findAccountByCifwithStatus(environment.getProperty("dormantAccountStatus"), cifId, 0);
						
					
					//if (isDormantAccountPresent == false)
						//throw new DataNotFoundException(getErrorMessage.getErrorMessages("NO_DORMANT_ACCOUNT_FOUND"), 401);
					
					List<TblCifDetails> tblCifDetails = tblCifDetailsRepository.findById(cifId);
					for (TblCifDetails obj : tblCifDetails) {
						// Object creation
						CifList cifList = new CifList();
						cifList.setId(obj.getId());
						cifList.setCifCode("Customer ID: CIF "+obj.getCifCode().replaceAll("\\d(?=(?:\\D*\\d){4})", "x"));
						cifList.setSelected(obj.isSelected());
						cifList.setRiskProfile(obj.getRiskProfile());
						tblCifAndAccountDetails.getCifList().add(cifList);
					}
					
					for (TblAccountDetails tblAccount : tblAccountDetails) {
						
						// Object creation
						DormantAccountList accountList = new DormantAccountList();
						String accNo = tblAccount.getAccountNumber();
						// insertion to the object
						accountList.setId(tblAccount.getId());
						accountList.setCustomerName(tblAccount.getTblCifDetails().getCustomerName());
						accountList.setAccountNo(accNo.replaceAll("\\d(?=(?:\\D*\\d){4})", "x"));
						accountList.setAccountType(accType.getTblAccountType(tblAccount.getAccountType()).get(0));
						accountList.setRequestDormantAccountActive(tblAccount.isRequestDormantActive());
						accountList.setCifId(cifId);
						tblCifAndAccountDetails.getDormantAccountList().add(accountList);
					} // for
					return tblCifAndAccountDetails;			
		
	}
	/** It will be called to save the activation request of dormant accounts. 
	 * It will save the request flag as true. **/
	@Override
	public ResponseData saveDormantAccounts(String token, Long cifId, List<Long> accountId) {
		
		if (token == null || token == "")
			throw new ServiceException(getErrorMessage.getErrorMessages("UNABLE_TO_PROCESS_ERR"), HttpStatus.UNAUTHORIZED.value());
		
		/**** validate token ****/
		long reqId = allTokenCreateAndValidate.validateToken(token);
		if (reqId == 0)
			throw new ServiceException(getErrorMessage.getErrorMessages("UNABLE_TO_PROCESS_ERR"), HttpStatus.UNAUTHORIZED.value());
		
		long tblCifDetailsCount = tblCifDetailsRepository.countByidreqid(cifId, reqId);
		
		// if CIF is not belongs to this token
		if (tblCifDetailsCount==0)
			throw new ServiceException(getErrorMessage.getErrorMessages("INVALID_CIF"), HttpStatus.UNAUTHORIZED.value());
		
		if(!accountId.isEmpty()) {
			tblAccountDetailsRepository.markAccAsActiveRequest(accountId, cifId);
		
			/* update request log status*/
    		tblRequestLogRepository.statusUpdate(reqId,Integer.parseInt(environment.getProperty("statusSelectDormantAccount")));
		}
	
		Map<String, String> responseDataDormant = new HashMap<>();
		
		ResponseData response = ResponseHelper.responseSenderData(getErrorMessage.getErrorMessages("DATA_SAVED"), HttpStatus.OK.value(), responseDataDormant);
		return response;				
		
	}	

	private Integer getRiskRating(String result) {
	    if(!result.equals("ERROR")) {
		    String value ="Risk_Profile";
		    String riskRate = converter.getParamJsonValue(result, value).toString();
		    int rate = 0;
		    if(riskRate!="" && riskRate!=null)
		    	rate = Integer.parseInt(riskRate);
		    return rate;
	    }
	    else {
	    	return -1;
	    }
	}
	public String getAddress(String result) {
		String totalAddress = null;
	    if(!result.equals("ERROR")) {
		    String CommunicationAddrress1 = converter.getParamJsonValue(result, "Communication_Addrress1").textValue();
		    String CommunicationAddrress2 = converter.getParamJsonValue(result, "Communication_Addrress2").textValue();
		    String CommunicationAddrress3 = converter.getParamJsonValue(result, "Communication_Addrress3").textValue();
		    String CommunicationLandmark = converter.getParamJsonValue(result, "Communication_Landmark").textValue();
		    String CommunicationState = converter.getParamJsonValue(result, "Communication_state").textValue();
		    String CommunicationCountry = converter.getParamJsonValue(result, "Communication_Country").textValue();
		    String CommunicationPinCode = converter.getParamJsonValue(result, "Communication_PinCode").textValue();
		    totalAddress = CommunicationAddrress1+", "+CommunicationAddrress2+", "+CommunicationAddrress3+", "+CommunicationLandmark+", "+CommunicationState+", "+CommunicationCountry+", "+CommunicationPinCode;
	    }
	    return totalAddress;
	}
	private String getRiskRatingandAddress(String token, String cif, String serviceType) {
		String uri = environment.getProperty("soapServiceUrl")+"finacle/getriskratingandaddress";
		// create a list the headers 
		HttpHeaders headers = new HttpHeaders();
		headers.set("authToken", token);
		headers.set("serviceType", serviceType);
		
		MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
		map.add("cifId", cif);
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

	    // initialize RestTemplate
	    RestTemplate restTemplate = new RestTemplate();
	    // post the request. The response should be JSON string
	    ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);
	    System.out.println(response);
	    int responseCode = response.getStatusCodeValue();
	    if(responseCode==200) {
		    String jsonResult = response.getBody();
		    return jsonResult;
	    }
	    else {
	    	return "ERROR";
	    }
	}
	private String isPanValidated(String token, String pan, String serviceType) {
		String uri = environment.getProperty("soapServiceUrl")+"panvalidation";
		// create a list the headers 
		HttpHeaders headers = new HttpHeaders();
		headers.set("authToken", token);
		headers.set("serviceType", serviceType);
		
		MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
		map.add("panNumber", pan);
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
	    // initialize RestTemplate
	    RestTemplate restTemplate = new RestTemplate();
	    // post the request. The response should be JSON string
	    ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);
	    int responseCode = response.getStatusCodeValue();
	    if(responseCode==200) {
		    String isPanValidated = response.getBody(); 
		    return isPanValidated;
	    }
	    else {
	    	return "ERROR";
	    }
	}
	private Boolean saveLinkedCifToDb(String token, String serviceType) {
		Boolean returnval = false;
		Map< String,String> data = allTokenCreateAndValidate.validateTokenReturnValue(token);
		long reqId = Long.parseLong(data.get("reqId"));
		String cif = data.get("ssoCifCode");
		String cifType = data.get("ssoCifType");
		String uri = environment.getProperty("soapServiceUrl")+"indusweb/getlinkedcif";
		// create a list the headers 
		HttpHeaders headers = new HttpHeaders();
		headers.set("authToken", token);
		headers.set("serviceType", serviceType);
		String cifId = "";
		if(cifType.equals("CC"))
			cifId = "CC/"+cif;
		else
			cifId = "CRM/"+cif;
		
		MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
		map.add("cifId", cifId);
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
		
		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);
		System.out.println(response);
		int responseCode = response.getStatusCodeValue();
		String masterCif = cif;
		
		if(responseCode==200) {
			String jsonResult = response.getBody();			 
			String inetFetchLinkedCustIdResponse = converter.getParamJsonValue(jsonResult, "InetFetchLinkedCustIdResponse").toString();
		    String flag = converter.getParamJsonValue(inetFetchLinkedCustIdResponse, "Status").textValue();
		    if(flag.equalsIgnoreCase("SUCCESS")) {		
		    	
		    	String bankingCIF = converter.getParamJsonValue(inetFetchLinkedCustIdResponse, "BankingCIF").textValue();
		    	if(!bankingCIF.isEmpty()) {
		    		String[] splitBankingCifAll = bankingCIF.split(",");
		    		String []splitBankingCif = CommonFunctions.removeDuplicateVal(splitBankingCifAll);
		    		for (int i = 0; i < splitBankingCif.length; i++) {
			    		this.saveCifToDb(token, reqId, splitBankingCif[i], serviceType, "BankingCIF", masterCif);
			    	}
		    	}
		    	
		    	/*String creditCIF = converter.getParamJsonValue(inetFetchLinkedCustIdResponse, "CreditCIF").textValue();
		    	if(!creditCIF.isEmpty()) {
		    		String[] splitCreditCifAll = creditCIF.split(",");
		    		String []splitCreditCif = CommonFunctions.removeDuplicateVal(splitCreditCifAll);
		    		for (int i = 0; i < splitCreditCif.length; i++) {
			    		this.saveCifToDb(token, reqId, splitCreditCif[i], serviceType, "CreditCIF", masterCif);
			    	}
		    	} */
		    	/* mark a CIF as selected as default according to risk profile*/
		    	tblCifDetailsRepository.markEntryAsSelectedFirstTime(reqId);
		    	/* update request log status*/
		    	tblRequestLogRepository.statusUpdate(reqId,Integer.parseInt(environment.getProperty("statusSaveAllCIFDetails")));
		    	
		    	/* PAN validation for other than master cif */
		    	TblCifDetails tblCifDetails = tblCifDetailsRepository.findBytblRequestLog_rIdAndMasterCif(reqId,true);
				if(tblCifDetails != null) {
					int isPanValidated = tblCifDetails.getIsPanValidated();
					tblCifDetailsRepository.updatePanValidation(reqId, isPanValidated);					
				}
		    	returnval = true;
		    }
		    
		}
		return returnval;
	}
	private Boolean saveCifToDb(String token, long reqId, String cif, String serviceType, String cifType, String masterCif) {
		Boolean returnval = false;
		TBLRequestlog tblRequest = tblRequestLogRepository.findById(reqId);
		int isMasterPresent = tblCifDetailsRepository.isMasterPresent(reqId);
		String uri = environment.getProperty("soapServiceUrl")+"finacle/getcifdetails";
		// create a list the headers 
		HttpHeaders headers = new HttpHeaders();
		headers.set("authToken", token);
		headers.set("serviceType", serviceType);
		
		MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
		map.add("cifId", cif);
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
		
		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);
		
		int responseCode = response.getStatusCodeValue();
		if(responseCode==200) {
			String jsonResult = response.getBody();
			String value ="sucFaiFlag";	
		    String flag = converter.getParamJsonValue(jsonResult, value).textValue();
		    if(flag.equalsIgnoreCase("S")) {
		    	String pan = converter.getParamJsonValue(jsonResult, "PAN_Number").textValue();
		    	boolean setmasterCif = false;
		    	int isPanValidated = 0;
		    	if(isMasterPresent == 0 && cif.equals(masterCif)) {
		    		setmasterCif = true;
		    		
			    	/* PAN VALIDATION*/
			    	if(pan==null || pan.equals(""))
			    		isPanValidated = 3;
		    		if(!pan.equals("")) {
		    			String panValidData = this.isPanValidated(token, pan, environment.getProperty("myServiceType"));
		        		if(panValidData.equals("ERROR")) {
		    	    		throw new ServiceException(getErrorMessage.getErrorMessages("UNABLE_TO_PROCESS_ERR"), HttpStatus.UNAUTHORIZED.value());
		        		}
		        		else {
		        			String panStatus = converter.getParamJsonValue(panValidData, "panStatus").textValue();
		        			isPanValidated = 2;
		        			if(panStatus.equalsIgnoreCase("E")) {
		        				String firstName = converter.getParamJsonValue(panValidData, "firstName").textValue();
		        				String middleName = converter.getParamJsonValue(panValidData, "middleName").textValue();
		        				String lastName = converter.getParamJsonValue(panValidData, "lastName").textValue();
		        				String totalName = firstName+" "+middleName+" "+lastName;
		        				totalName = totalName.replaceAll("\\s{2,}", " ").trim();
		        				String fullName = firstName+" "+lastName;
		        				fullName = fullName.replaceAll("\\s{2,}", " ").trim();
		        				String cifName = converter.getParamJsonValue(jsonResult, "Customer_Name").textValue();
		        				
		        				if(cifName.equalsIgnoreCase(totalName) || cifName.equalsIgnoreCase(fullName)) {
		        					isPanValidated = 1;
		        				}
		        			}		    	    			
		        		}	        			
		        	}	    		
		    		
		    	}
		    	String riskRatingandAddress = this.getRiskRatingandAddress(token, cif, serviceType);
		    	if(riskRatingandAddress.equals("ERROR"))
		    		throw new ServiceException(getErrorMessage.getErrorMessages("UNABLE_TO_PROCESS_ERR"), HttpStatus.UNAUTHORIZED.value());
		    	int riskProfile = this.getRiskRating(riskRatingandAddress);
		    	String communicationAddress = this.getAddress(riskRatingandAddress);
		    	
		    	String mobno = converter.getParamJsonValue(jsonResult, "Mobile_Number").textValue();
		    	/** will be deleted later. for testing **/
		    	if(activeEnvironment.equalsIgnoreCase("staging") || activeEnvironment.equalsIgnoreCase("local")) {
		    		if(mobno == null || mobno.equals(""))
		    			mobno = "9888811111";
		    	}
		    	
		    	/*Entry to CIF details table*/
		    	TblCifDetails cifDetails = new TblCifDetails();
		    	cifDetails.setCreatedAt(LocalDateTime.now());
		    	cifDetails.setUpdatedAt(LocalDateTime.now());
		    	cifDetails.setConstitutionType(converter.getParamJsonValue(jsonResult, "Constitution_Type").textValue());
		    	cifDetails.setCustomerName(converter.getParamJsonValue(jsonResult, "Customer_Name").textValue());
		    	cifDetails.setDateOfBirth(converter.getParamJsonValue(jsonResult, "Date_of_Birth").textValue());
		    	cifDetails.setPanNumber(pan);
		    	cifDetails.setGender(converter.getParamJsonValue(jsonResult, "Gender").textValue());
		    	cifDetails.setAadhaarNumber(converter.getParamJsonValue(jsonResult, "Aadhaar_Number").textValue());
		    	cifDetails.setMobileNumber(mobno);
		    	cifDetails.setComEmailId(converter.getParamJsonValue(jsonResult, "Com_Emailid").textValue());
		    	cifDetails.setComEmailId(converter.getParamJsonValue(jsonResult, "Com_Emailid").textValue());
		    	cifDetails.setCifCode(cif);
		    	cifDetails.setCifType(cifType);
		    	cifDetails.setIsPanValidated(isPanValidated);// need to be changed later	
		    	cifDetails.setSelected(false);
		    	cifDetails.setMasterCif(setmasterCif);
		    	cifDetails.setRiskProfile(riskProfile);
		    	cifDetails.setTblRequestLog(tblRequest);
		    	cifDetails.setCommunicationAddress(communicationAddress);
		    	TblCifDetails tblCifData = tblCifDetailsRepository.save(cifDetails);
		    	/*Entry to CIF details table*/
		    	
		    	
		    	/*Entry to Account details table*/
		    	String accountDetails = converter.getParamJsonValue(jsonResult, "AccountDetails").toString();
		    	
		    	String[] custTypes = new String[]{"CP", "EC", "SP", "S5", "S1", "AG", "EA", "EG", "EM", "ES", "RT", "SA", "SD"};
		 		List<String> custTypesList = Arrays.asList(custTypes);
		 		
		 		String[] accTypes = new String[]{"SBA", "SAA", "CAA"};
		 		List<String> accTypesList = Arrays.asList(accTypes);
		 		
		 		String[] inactiveMop = new String[]{"99999"};
		 		List<String> inactiveMopList = Arrays.asList(inactiveMop);
		 		
		    	if (accountDetails.startsWith("[")) {
		    		JSONArray jsonArr = new JSONArray(accountDetails);
			    	System.out.println(jsonArr);
			    	for (int i = 0; i < jsonArr.length(); i++)
			        {
			            JSONObject jsonObj = jsonArr.getJSONObject(i);
			            
			            TblAccountDetails tblAccountDetails = new TblAccountDetails();
			            
			            String mop = converter.getParamJsonValue(jsonObj.toString(), "MOP").textValue();
	    			 	String accType = converter.getParamJsonValue(jsonObj.toString(), "Account_Type").textValue();
	    			 	String c5Code = converter.getParamJsonValue(jsonObj.toString(), "C5_Code").textValue();
	    			 	String accountStatus = converter.getParamJsonValue(jsonObj.toString(), "Account_Status").textValue();
	    			 	
	    			 	Boolean isDisplay = true;
	    			 	
	    			 	
	    			 	/***** ACCOUNT FILTER ******/
	    			 	if(!accountStatus.equals(environment.getProperty("activeAccountStatus"))){ // only active account will show
	    			 		isDisplay = false;
	    			 	}
	    			 	else {
	    			 		if(!accTypesList.contains(accType)) { // check account types
	    			 			isDisplay = false;
	    			 		}
	    			 		else {
	    			 			if(accType.equals("CAA")) { // if account type caa, check c5Code
	    	    			 		if(!custTypesList.contains(c5Code)){
	    	    			 			isDisplay = false;
	    	    			 		}
	    	    			 	}
	    			 			if(mop == "" || mop == null || inactiveMopList.contains(mop)) { // mop check
	    	    			 		isDisplay = false;
	    	    			 	}
	    			 		}
	    			 	}
	    			 	
			            tblAccountDetails.setAccountNumber(converter.getParamJsonValue(jsonObj.toString(), "Account_Number").toString());
			            tblAccountDetails.setAccountType(accType);
			            tblAccountDetails.setC5Code(c5Code);
			            tblAccountDetails.setC5CodeDesc(converter.getParamJsonValue(jsonObj.toString(), "C5_Code_Desc").textValue());
			            tblAccountDetails.setEmailId(converter.getParamJsonValue(jsonObj.toString(), "Email_ID").textValue());
			            tblAccountDetails.setMopDesc(converter.getParamJsonValue(jsonObj.toString(), "MOP_Desc").textValue());
			            tblAccountDetails.setSchemeCode(converter.getParamJsonValue(jsonObj.toString(), "Scheme_Code").textValue());
			            tblAccountDetails.setStatus(accountStatus);
			            tblAccountDetails.setMop(mop);
			            tblAccountDetails.setDisplay(isDisplay);
			            tblAccountDetails.setRequestDormantActive(false);
			            tblAccountDetails.setCreatedAt(LocalDateTime.now());
			            tblAccountDetails.setUpdatedAt(LocalDateTime.now());
			            tblAccountDetails.setTblCifDetails(tblCifData);
			            tblAccountDetailsRepository.save(tblAccountDetails);
			            
			        }
	    		} else {
	    			 	TblAccountDetails tblAccountDetails = new TblAccountDetails();
	    			 	
	    			 	String mop = converter.getParamJsonValue(accountDetails.toString(), "MOP").textValue();
	    			 	String accType = converter.getParamJsonValue(accountDetails.toString(), "Account_Type").textValue();
	    			 	String c5Code = converter.getParamJsonValue(accountDetails.toString(), "C5_Code").textValue();
	    			 	String accountStatus = converter.getParamJsonValue(accountDetails.toString(), "Account_Status").textValue();
	    			 	
	    			 	Boolean isDisplay = true;
	    			 	
	    			 	/***** ACCOUNT FILTER ******/
	    			 	if(!accountStatus.equals(environment.getProperty("activeAccountStatus"))){ // only active account will show
	    			 		isDisplay = false;
	    			 	}
	    			 	else {
	    			 		if(!accTypesList.contains(accType)) { // check account types
	    			 			isDisplay = false;
	    			 		}
	    			 		else {
	    			 			if(accType.equals("CAA")) { // if account type caa, check c5Code
	    	    			 		if(!custTypesList.contains(c5Code)){
	    	    			 			isDisplay = false;
	    	    			 		}
	    	    			 	}
	    			 			if(mop == "" || mop == null || inactiveMopList.contains(mop)) { // mop check
	    	    			 		isDisplay = false;
	    	    			 	}
	    			 		}
	    			 	}
	    		        
	    		        
			            tblAccountDetails.setAccountNumber(converter.getParamJsonValue(accountDetails.toString(), "Account_Number").toString());
			            tblAccountDetails.setAccountType(accType);
			            tblAccountDetails.setC5Code(c5Code);
			            tblAccountDetails.setC5CodeDesc(converter.getParamJsonValue(accountDetails.toString(), "C5_Code_Desc").textValue());
			            tblAccountDetails.setEmailId(converter.getParamJsonValue(accountDetails.toString(), "Email_ID").textValue());
			            tblAccountDetails.setMopDesc(converter.getParamJsonValue(accountDetails.toString(), "MOP_Desc").textValue());
			            tblAccountDetails.setSchemeCode(converter.getParamJsonValue(accountDetails.toString(), "Scheme_Code").textValue());
			            tblAccountDetails.setStatus(accountStatus);
			            tblAccountDetails.setMop(mop);
			            tblAccountDetails.setDisplay(isDisplay);// need to be changed later	
			            tblAccountDetails.setRequestDormantActive(false);
			            tblAccountDetails.setCreatedAt(LocalDateTime.now());
			            tblAccountDetails.setUpdatedAt(LocalDateTime.now());
			            tblAccountDetails.setTblCifDetails(tblCifData);
			            tblAccountDetailsRepository.save(tblAccountDetails);
	    		}
		    	
		    	/*Entry to Account details table*/
		    	
		    	returnval = true;
		    }
		    
		}
		
		return returnval;
	}
	
	public ResponseData sendOtp(String token, int otpAttempts) {
		ResponseData otpResponse = null;
		Map<String, String> responseData = new HashMap<>();
		String errorMessage = getErrorMessage.getErrorMessages("UNABLE_TO_PROCESS_ERR");
		Map<String,String> data = allTokenCreateAndValidate.validateTokenReturnValue(token);
		long reqId = Long.parseLong(data.get("reqId"));
		
		if (token == null || token == "")
			throw new ServiceException(getErrorMessage.getErrorMessages("UNABLE_TO_PROCESS_ERR"), HttpStatus.UNAUTHORIZED.value());
		
		/**** validate token ****/
		if (reqId == 0)
			throw new ServiceException(getErrorMessage.getErrorMessages("UNABLE_TO_PROCESS_ERR"), HttpStatus.UNAUTHORIZED.value());
		
		
		int status = Integer.parseInt(data.get("status"));
		if(status >= Integer.parseInt(environment.getProperty("statusUpdateSelectedCIF"))){
			TblCifDetails tblCifDetails = tblCifDetailsRepository.findBytblRequestLog_rIdAndMasterCif(reqId,true);
			if(tblCifDetails != null) {
				String cifMobileNumber = tblCifDetails.getMobileNumber();
				if(!cifMobileNumber.isEmpty() && cifMobileNumber!="" && cifMobileNumber!=null) {
					String cifEmailId = tblCifDetails.getComEmailId();
					String emailid = cifEmailId;
					//String masterCifCode = tblCifDetails.getCifCode();
					long cifId = tblCifDetailsRepository.selectedCif(reqId);
					List<TblCifDetails> selectedCif = tblCifDetailsRepository.findById(cifId);
					boolean isNre = false;
					for (TblCifDetails obj : selectedCif) {
						isNre = obj.isNr();
					}
					String uri = environment.getProperty("soapServiceUrl")+"/otp/sendotp";
					// create a list the headers 
					HttpHeaders headers = new HttpHeaders();
					headers.set("authToken", token);
					headers.set("serviceType", environment.getProperty("myServiceType"));
					
					MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
					map.add("mobileNo", cifMobileNumber);
					
					HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

				    // initialize RestTemplate
				    RestTemplate restTemplate = new RestTemplate();
				    // post the request. The response should be JSON string
				    ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);
				    System.out.println(response);
				    int responseCode = response.getStatusCodeValue();
				    if(responseCode==200) {
					    String responseBody = response.getBody();
					    System.out.println(responseBody);
					    String referenceId = converter.getParamJsonValue(responseBody, "ReferenceId").textValue();
					    String otp = converter.getParamJsonValue(responseBody, "OTPNumber").textValue();
					    TblRequestOtp tblRequestOtp = new TblRequestOtp();
					    tblRequestOtp.setCreatedAt(LocalDateTime.now());
					    tblRequestOtp.setUpdatedAt(LocalDateTime.now());
					    tblRequestOtp.setOtpSentAt(LocalDateTime.now());
					    tblRequestOtp.setMobileNo(cifMobileNumber);
					    tblRequestOtp.setEmail(cifEmailId);
					    tblRequestOtp.setReferenceId(referenceId);
					    tblRequestOtp.setOtpAttempts(otpAttempts);
					    tblRequestOtp.setReqId(reqId);
					    tblRequestOtp.setSentEmailResCode(0);
					    tblRequestOtp.setOtp(otp);
					    tblRequestOtp.setSentSmsStatus(converter.getParamJsonValue(responseBody, "Status").textValue());
					    tblRequestOtp.setStatus(true);
					    tblRequestOtp.setOtpType(1);
					    TblRequestOtp tblOtpsavedData = tblRequestOtpRepository.save(tblRequestOtp);
					    
					    /* update request log status*/
					    tblRequestLogRepository.statusUpdate(reqId,Integer.parseInt(environment.getProperty("statusSendOTP")));
					    
					    if(isNre) {
					    	if(cifEmailId!=null && !cifEmailId.equals(""))
					    		this.sendOtpViaEmail(token, otp, cifEmailId);
						}
					    
					    otpAttempts = otpAttempts + 1;
						responseData.put("otpId", String.valueOf(tblOtpsavedData.getId()));
						responseData.put("referenceId", referenceId);
						String mob = cifMobileNumber.replaceAll("\\d(?=(?:\\D*\\d){4})", "x");
						responseData.put("mobileNo", mob);
						
						if(cifEmailId!=null && !cifEmailId.equals(""))
							emailid = cifEmailId.replaceAll("(^[^@]{0}|(?!^)\\G)[^@]", "$1x");
						responseData.put("email", emailid);
						String isNrRes = "0";
						if(isNre) isNrRes = "1";
						responseData.put("isNRE",isNrRes);
						responseData.put("otpAttempts", String.valueOf(otpAttempts));
						responseData.put("otpTimeout", getSettings.getSettingsVal("otpTimeout"));
						responseData.put("statusMessage", "");
						otpResponse = ResponseHelper.responseSenderData("", HttpStatus.OK.value(), responseData);
				    }
				    else {
				    	
				    	throw new ServiceException(errorMessage, HttpStatus.UNAUTHORIZED.value());
				    }
					
				}
				else {
					throw new ServiceException(errorMessage, HttpStatus.UNAUTHORIZED.value());
			    }
			}
		}
		else {
			throw new ServiceException(errorMessage, HttpStatus.UNAUTHORIZED.value());
		}
		return otpResponse;
	}

	private void sendOtpViaEmail(String token, String otpNum, String email) {
		String emailType = environment.getProperty("emailTypeOTP");
		TblEmailTemplate templates = getEmailTemplate.getEmailTemplate("SEND_OTP_VIA_EMAIL");
		String subject = templates.getEmailSubject();
		String content = templates.getEmailContent();
		content = content.replace("[VAR_OTP]", otpNum);
		content = content.replace("[REKYC_RELATED_CONTACT_EMAIL]", environment.getProperty("reachUsEmail"));
		content = content.replace("[REKYC_RELATED_CONTACT_NO]", environment.getProperty("phoneBankingNumber"));
		sendMessageEmail.sendEmail(token, email, subject, content, environment.getProperty("myServiceType"), emailType);
	}
}
