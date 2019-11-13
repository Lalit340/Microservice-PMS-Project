package in.co.indusnet.soap.utility;

import java.time.LocalTime;
import java.util.Base64;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import in.co.indusnet.soap.model.fdcmi.FdcmiTblRequestToKey;
import in.co.indusnet.soap.model.rekyc.RekycTblRequestToKey;
import in.co.indusnet.soap.repository.fdcmi.FdcmiTblRequestKeyRepository;
import in.co.indusnet.soap.repository.rekyc.RekycTblRequestKeyRepository;

@Component
public class AllTokenCreateAndValidate {

	@Autowired
	private RekycTblRequestKeyRepository rekycTblKeyRepository;
	
	@Autowired
	private FdcmiTblRequestKeyRepository fdcmiTblKeyRepository;

	/*
	 * this method purpose to generate basic 64 digit random token
	 */
	public String getBasicToken() {
		// to generate random string
		String token = UUID.randomUUID().toString().toUpperCase() + "_" + LocalTime.now();
		// Encode into Base64 format
		String BasicBase64format = Base64.getEncoder().encodeToString(token.getBytes());
		return BasicBase64format;
	}

	/*
	 * this method purpose to Validate request to with the databse token if valid
	 * return 'id' otherwise return 'false' .
	 */
	public long validateToken(String token, String serviceType) {

		// to get the object with token
		long id = 0;
		switch (serviceType) { 
        case "rekyc": 
        	System.out.println("I am in rekyc");
        	RekycTblRequestToKey rekyctblRequest = rekycTblKeyRepository.findByToken(token);
        	if (rekyctblRequest != null) {
    			if (rekyctblRequest.isTokenStatus()) {
    				id = rekyctblRequest.getReqId();
    			}
        	}
        	break;
		case "fdrd": 
        	FdcmiTblRequestToKey fdcmitblRequest = fdcmiTblKeyRepository.findByToken(token);
        	if (fdcmitblRequest != null) {
    			if (fdcmitblRequest.isTokenStatus()) {
    				id = fdcmitblRequest.getReqId();
    			} 
    		}
        break;
        default: 
        	id = 0;
        break;
        }
		return id;
	}

}
