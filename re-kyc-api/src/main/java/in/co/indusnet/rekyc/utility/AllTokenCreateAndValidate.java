package in.co.indusnet.rekyc.utility;

import java.time.LocalTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import in.co.indusnet.rekyc.exception.ServiceException;
import in.co.indusnet.rekyc.model.TblRequestToKey;
import in.co.indusnet.rekyc.repository.TblRequestKeyRepository;

@Component
public class AllTokenCreateAndValidate {

	@Autowired
	private TblRequestKeyRepository tblKeyRepository;
	
	@Autowired
	private GetErrorMessage getErrorMessage;

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
	public long validateToken(String token) {

		// to get the object with token
		TblRequestToKey tblRequest = tblKeyRepository.findByToken(token);
		System.out.println("token class object ::" + tblRequest);

		// if condition for validate data
		if (tblRequest != null) {
			if (tblRequest.isTokenStatus()) {
				long id = tblRequest.getTblRequestLog().getRId();
				return id;
			} else {
				return 0;
			}

		} else
			throw new ServiceException(getErrorMessage.getErrorMessages("UNABLE_TO_PROCESS_ERR"), HttpStatus.UNAUTHORIZED.value());
	}
	
	/*
	 * this method purpose to Validate request to with the databse token if valid
	 * return 'id' otherwise return 'false' .
	 */
	public Map<String,String> validateTokenReturnValue(String token) {

		// to get the object with token
		TblRequestToKey tblRequest = tblKeyRepository.findByToken(token);
		System.out.println("token class object ::" + tblRequest);
		Map< String,String> data = new HashMap< String,String>();
		// if condition for validate data
		if (tblRequest != null) {
			if (tblRequest.isTokenStatus()) {
				long id = tblRequest.getTblRequestLog().getRId();
				String status = Integer.toString(tblRequest.getTblRequestLog().getStatus());
				String isAddressModify = "no";
				if(tblRequest.getTblRequestLog().isAddressModify())
					isAddressModify = "yes";
				data.put("reqId", Long.toString(id));
				data.put("ssoCifCode", tblRequest.getTblRequestLog().getSsoCifCode());
				data.put("ssoCifType", tblRequest.getTblRequestLog().getSsoCifType());
				data.put("newAddress", tblRequest.getTblRequestLog().getAddress());
				data.put("status", status);
				data.put("isAddressModify", isAddressModify);
				return data;
			} else {
				data.put("reqId", Long.toString(0)); 
				return data;
			}

		} else
			throw new ServiceException(getErrorMessage.getErrorMessages("UNABLE_TO_PROCESS_ERR"), HttpStatus.UNAUTHORIZED.value());
	}
}
