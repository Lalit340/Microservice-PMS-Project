package in.co.indusnet.rekyc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import in.co.indusnet.rekyc.dto.SSOData;
import in.co.indusnet.rekyc.response.ResponseData;
import in.co.indusnet.rekyc.service.CronJobService;
import in.co.indusnet.rekyc.service.SSOService;
import in.co.indusnet.rekyc.utility.CommonFunctions;
import in.co.indusnet.rekyc.utility.EncryptAndDecryptData;

@RestController
public class SSOController {

	@Autowired
	private SSOService service;

	@Autowired
	private CommonFunctions commonFunctions;
  
	@Autowired
	CronJobService cronJobService;
	/*
	 * purpose of getSSOData(- ,-, -, -, -) is getting data to the sso url
	 */

	@GetMapping("/sso")
	public ResponseEntity<ResponseData> getSSOData(@RequestParam String RequestType, @RequestParam String SessionId,
			@RequestParam String CIFID, @RequestParam String ChannelId, @RequestParam String CIFType) {

		
		/* All local variables **/
		String reqType = null;
		String sessionid = null;
		String cifid = null;
		String channelid = null;
		String cifTyp = null;

		/* Decrypt all data */
		reqType = EncryptAndDecryptData.decryptWithAESKey(RequestType);

		sessionid = EncryptAndDecryptData.decryptWithAESKey(SessionId);

		cifid = EncryptAndDecryptData.decryptWithAESKey(CIFID);

		channelid = EncryptAndDecryptData.decryptWithAESKey(ChannelId);

		cifTyp = EncryptAndDecryptData.decryptWithAESKey(CIFType);
		//mapping encrypted data to object
		SSOData sso= new SSOData();
		sso.setChannelid(ChannelId);
		sso.setCifid(CIFID);
		sso.setCifTyp(CIFType);
		sso.setReqType(RequestType);
		sso.setSessionid(SessionId);
		
		//common function call
		String jsonData =commonFunctions.getJson(sso);
		System.out.println("my data ::"+jsonData);
		
		// calling service class method for business logic
		ResponseData response = service.tblRequestData(reqType, sessionid, cifid , channelid, cifTyp , jsonData);
		return new ResponseEntity<ResponseData>(response, HttpStatus.OK);
	}
}
