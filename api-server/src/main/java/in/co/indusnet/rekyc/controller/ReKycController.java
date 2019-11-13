package in.co.indusnet.rekyc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import in.co.indusnet.rekyc.exception.ServiceException;
import in.co.indusnet.rekyc.model.TblErrorMessages;
import in.co.indusnet.rekyc.model.TblSettings;
import in.co.indusnet.rekyc.response.DocumentUploadedApi;
import in.co.indusnet.rekyc.response.GetCifDetailsApi;
import in.co.indusnet.rekyc.response.GetCifListApi;
import in.co.indusnet.rekyc.response.GetDormantAccountsApi;
import in.co.indusnet.rekyc.response.ResponseData;
import in.co.indusnet.rekyc.service.DocumentUploadService;
import in.co.indusnet.rekyc.service.SSOService;
import in.co.indusnet.rekyc.service.TblCifService;
import in.co.indusnet.rekyc.service.ValidateOtpService;
import in.co.indusnet.rekyc.utility.AllTokenCreateAndValidate;
import in.co.indusnet.rekyc.utility.GetErrorMessage;
import in.co.indusnet.rekyc.utility.GetSettings;
import in.co.indusnet.rekyc.utility.ResponseHelper;

@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class ReKycController {

	@Autowired
	private TblCifService service;

	@Autowired
	private SSOService ssoService;

	@Autowired
	private ValidateOtpService validateotpservice;

	@Autowired
	private DocumentUploadService documentUploadService;
	
	@Autowired
	private GetErrorMessage getErrorMessage;
	
	@Autowired
	private AllTokenCreateAndValidate allTokenCreateAndValidate;
	
	@Autowired
	private GetSettings getSettings;

	/** Get All CIF list with details and it's account list **/
	@GetMapping("/getCifList")
	public GetCifListApi getCifList(@RequestHeader String Authorization) {
		GetCifListApi tblCifAndAccountDetails = service.getCifList(Authorization);
		System.out.println(" tblAccountDetails :: " + tblCifAndAccountDetails);
		return tblCifAndAccountDetails;
	}

	/**
	 * Get All CIF list with details and choose selected cif from database and it's
	 * account list
	 **/
	@GetMapping("/getCifList/{cif}")
	public GetCifDetailsApi getCifDetails(@RequestHeader String Authorization, @PathVariable Long cif) {
		GetCifDetailsApi tblCifAndAccountDetails = service.getCifDetails(Authorization, cif);
		System.out.println(" tblAccountDetails :: " + tblCifAndAccountDetails);
		return tblCifAndAccountDetails;
	}

	/**
	 * Proceed to second step with a CIF. It will validate the token and CIF and
	 * returns if any dormant account present or not. According to this Third step
	 * will be decided.
	 **/
	@RequestMapping(value = "/saveSelectedCif", method = RequestMethod.POST)
	public ResponseData saveSelectedCif(@RequestHeader String Authorization, @RequestParam Long cif,
			@RequestParam(value = "newAddress", required = false) String newAddress,
			@RequestParam Boolean isAddressModify) {
		ResponseData addrModifyandDormantCheck = service.saveSelectedCif(Authorization, cif, newAddress,
				isAddressModify);
		return addrModifyandDormantCheck;
	}

	/** Get Dormant account list of CIF **/
	@RequestMapping(value = "/dormantAccountList", method = RequestMethod.GET)
	public GetDormantAccountsApi dormantAccountList(@RequestHeader String Authorization) {
		GetDormantAccountsApi tblCifAndAccountDetails = service.getDormantList(Authorization);
		return tblCifAndAccountDetails;
	}

	@PostMapping(value = "upload", consumes = { "multipart/form-data" })
	public ResponseEntity<ResponseData> fielUpload(@RequestParam(defaultValue = "0") int idProofDocType,
			@RequestParam(value = "idProofFiles") MultipartFile[] idProofFiles, 
			@RequestParam(defaultValue = "0") int addressProofDocType,
			@RequestParam(value = "addressProofFiles") MultipartFile[] addressProofFiles,
			@RequestParam(defaultValue = "0") int nrProofDocType,
			@RequestParam(value = "nrProofFiles", required = false) MultipartFile[] nrProofFiles,
			@RequestParam(defaultValue = "0") int overseasProofDocType,
			@RequestParam(value = "overseasProofFiles", required = false) MultipartFile[] overseasProofFiles,
			@RequestParam(defaultValue = "false") boolean isAddressProofSameAsIdProof,
			@RequestHeader String authorization) {
		ResponseData responseStatus = documentUploadService.uploadFiles(idProofDocType, idProofFiles,
				addressProofDocType, addressProofFiles, nrProofDocType, nrProofFiles, overseasProofDocType,
				overseasProofFiles, isAddressProofSameAsIdProof, authorization);
		return new ResponseEntity<ResponseData>(responseStatus, HttpStatus.OK);
	}

	@PostMapping("deleteDocument/{fileId}")
	public ResponseEntity<ResponseData> deleteFile(@RequestHeader String authorization, @PathVariable("fileId") long fileId) {
		ResponseData responseStatus = documentUploadService.deleteFile(authorization, fileId);
		return new ResponseEntity<ResponseData>(responseStatus, HttpStatus.OK);
	}

	@PostMapping("/saveDormantAccounts")
	public ResponseData saveSelectedDormantAccounts(@RequestHeader String Authorization, @RequestParam Long cif,
			@RequestParam(value = "accountId", required = false) List<Long> accountId) {
		ResponseData addrModifyandDormantCheck = service.saveDormantAccounts(Authorization, cif, accountId);
		return addrModifyandDormantCheck;
	}

	@GetMapping("/getUploadedDocuments")
	public DocumentUploadedApi getUploadedDocuments(@RequestHeader String Authorization) {
		DocumentUploadedApi response = documentUploadService.getUploadedDocumentList(Authorization);
		return response;
	}

	
	/** Update the existing token **/
	@PostMapping("/updateRequestToken")
	public ResponseData updateRequestToken(@RequestHeader String Authorization, @RequestParam String updatedToken) {
		ResponseData addrModifyandDormantCheck = ssoService.updateRequestToken(Authorization, updatedToken);
		return addrModifyandDormantCheck;
	}

	/** Send OTP **/
	@GetMapping("/sendOtp/{otpAttempts}")
		public ResponseData sendOtp(@RequestHeader String Authorization, @PathVariable("otpAttempts") int otpAttempts) {
		ResponseData responseStatus = service.sendOtp(Authorization, otpAttempts);
		return responseStatus;
	}

	
	/** Validate OTP **/
	@PostMapping("/validateOtp/")
		public ResponseData validateOtp(@RequestHeader String Authorization, @RequestParam String otp, @RequestParam long otpId) {
		ResponseData responseStatus = validateotpservice.validateOtp(Authorization, otp, otpId);
		return responseStatus;
	}
	
	@GetMapping("/getErrorMessages")
	public ResponseData getErrorMessages(@RequestHeader String Authorization) {
		if (Authorization.equals("") || Authorization== null)
			throw new ServiceException(getErrorMessage.getErrorMessages("UNABLE_TO_PROCESS_ERR"), HttpStatus.UNAUTHORIZED.value());
		
		long reqId = allTokenCreateAndValidate.validateToken(Authorization);
		// if condition to validate token return data
		if (reqId == 0)
			throw new ServiceException(getErrorMessage.getErrorMessages("UNABLE_TO_PROCESS_ERR"), HttpStatus.UNAUTHORIZED.value());
		
		List<TblErrorMessages> allError = getErrorMessage.getAllErrorMessages();
		 Map<String, String> responseData = new HashMap<>();
		 for (TblErrorMessages obj : allError) {
			 responseData.put(obj.getErrorCode(), obj.getErrorMessage());
		 }	 
		ResponseData response = ResponseHelper.responseSenderData("ERROR_MESSAGES", HttpStatus.OK.value(), responseData);
		return response;
	}
	@GetMapping("/getSettings")
	public ResponseData getSettings(@RequestHeader String Authorization) {
		
		if (Authorization.equals("") || Authorization== null)
			throw new ServiceException(getErrorMessage.getErrorMessages("UNABLE_TO_PROCESS_ERR"), HttpStatus.UNAUTHORIZED.value());
		
		long reqId = allTokenCreateAndValidate.validateToken(Authorization);
		// if condition to validate token return data
		if (reqId == 0)
			throw new ServiceException(getErrorMessage.getErrorMessages("UNABLE_TO_PROCESS_ERR"), HttpStatus.UNAUTHORIZED.value());
		
		List<TblSettings> allSettings = getSettings.getAllSettings();
		 Map<String, String> responseData = new HashMap<>();
		 for (TblSettings obj : allSettings) {
			 responseData.put("maxOtpResendCount", String.valueOf(obj.getMaxOtpResendCount()));
			 responseData.put("otpTimeOut", String.valueOf(obj.getOtpTimeout()));
			 responseData.put("otpValidation", String.valueOf(obj.getOtpValidation()));
			 responseData.put("otpWrongAttemptsLimit", String.valueOf(obj.getOtpWrongAttemptsLimit()));
		 }	 
		ResponseData response = ResponseHelper.responseSenderData("APP_SETTINGS", HttpStatus.OK.value(), responseData);
		return response;
	}
	
	// will be deleted later
	//		@PostMapping("/getriskrating")
	//		public Integer getRiskRating(@RequestHeader String token, @RequestHeader String serviceType,
	//				@RequestParam String cif) {
	//			Integer responseStatus = service.getRiskRating(token, cif, serviceType);
	//			return responseStatus;
	//		}
		
	//	@PostMapping("/rekycSubmit")
	//	public Boolean reKycSubmission(@RequestHeader String token, @RequestHeader String serviceType,
	//			@RequestParam String cif, @RequestParam long reqId) {
	//		Boolean responseStatus = validateotpservice.reKycSubmission(token, reqId);
	//		return responseStatus;
	//	}
//		@PostMapping("/savecif")
//		public Boolean savecif(@RequestHeader String token, @RequestHeader String serviceType) {
//			service.saveLinkedCifToDb(token, serviceType);
//			return false;
//		}
}
