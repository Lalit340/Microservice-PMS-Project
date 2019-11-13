package in.co.indusnet.soap.controller;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.co.indusnet.soap.service.TalismaClient;
import in.co.indusnet.soap.utility.AllTokenCreateAndValidate;

@RestController
@RequestMapping("/talisma")
public class TalismaController {

	@Autowired
	private TalismaClient talismaClient;
	
	@Autowired
	private AllTokenCreateAndValidate allTokenCreateAndValidate;

	@PostMapping("/createservice/rekyc")
	public ResponseEntity<String> createServiceRequest(@RequestHeader String token,@RequestHeader String serviceType,
			@RequestParam(value = "accountNo", required = false) String accountNo, @RequestParam String cifId, @RequestParam(value = "panNo", required = false) String panNo,
			@RequestParam String accActivationConsent, @RequestParam(value = "addressProofType", required = false) String addressProofType,
			@RequestParam(value = "idProofType", required = false) String idProofType, @RequestParam(value = "mobileNumber", required = false) String mobileNumber, @RequestParam(value = "emailId", required = false) String emailId,
			@RequestParam String verificationMode, @RequestParam(value = "newAddress", required = false) String newAddress, 
			@RequestParam String interactionState,@RequestParam String cifTableId) {
		
		long reqId = allTokenCreateAndValidate.validateToken(token,serviceType);
		/*IF the token is not valid*/
		if(reqId == 0) {
			JSONObject errorObj = new JSONObject();
			errorObj.put("error", "unauthorized");
			return new ResponseEntity<>(errorObj.toString(), HttpStatus.UNAUTHORIZED);
		}
		else {
			long cifTblId = Long.parseLong(cifTableId);
			String response = talismaClient.createServiceRequest(accountNo, cifId, panNo,
					accActivationConsent, addressProofType, idProofType, mobileNumber, emailId, verificationMode,
					newAddress,interactionState,reqId,serviceType,cifTblId);
			System.out.println("TALISMA RESPONSE======================>>>>>"+response);
			if(response==null) {
				JSONObject errorObj = new JSONObject();
				errorObj.put("error", "unable_to_process");
				return new ResponseEntity<>(errorObj.toString(), HttpStatus.UNAUTHORIZED);
			}
			else {
				return new ResponseEntity<String>(response, HttpStatus.OK);
			}
		}
	}
	@PostMapping("/createservice/fdcmi")
	public ResponseEntity<String> createServReq(@RequestHeader String token,
			@RequestHeader String serviceType,
			@RequestParam String accNo,
			@RequestParam String cifId,
			@RequestParam String interactionState,
			@RequestParam String openingDate,
			@RequestParam String maturityDate,
			@RequestParam String maturityPeriod,
			@RequestParam String depositeAmount,
			@RequestParam String creditToAccount,
			@RequestParam String updatedRenewalType,
			@RequestParam String currentMaturityInstruction,
			@RequestParam String updatedMaturityInstruction,
			@RequestParam String mobileNumber,
			@RequestParam String emailIdForOtp,
			@RequestParam String verficationMode,
			@RequestParam long cifTableId
			) { 
		long reqId = allTokenCreateAndValidate.validateToken(token,serviceType);
		if(reqId == 0) {
			JSONObject errorObj = new JSONObject();
			errorObj.put("error", "unauthorized");
			return new ResponseEntity<>(errorObj.toString(), HttpStatus.UNAUTHORIZED);
		}
		String response = talismaClient.createServiceRequestFdcmi(accNo, cifId, interactionState, openingDate, 
				maturityDate, maturityPeriod, depositeAmount, creditToAccount, 
				updatedRenewalType, currentMaturityInstruction, updatedMaturityInstruction,
				mobileNumber, emailIdForOtp, verficationMode, cifTableId, reqId, serviceType);
		if(response==null) {
			JSONObject errorObj = new JSONObject();
			errorObj.put("error", "unable_to_process");
			return new ResponseEntity<>(errorObj.toString(), HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<String>(response, HttpStatus.OK);
		
	}

}
