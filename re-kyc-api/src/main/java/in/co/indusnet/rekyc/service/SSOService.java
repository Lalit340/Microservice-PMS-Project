package in.co.indusnet.rekyc.service;

import org.springframework.stereotype.Service;

import in.co.indusnet.rekyc.response.ResponseData;


@Service
public interface SSOService {

	// this method for sso url data validating and inserting data to the database
	public ResponseData tblRequestData(String requestType, String sessionId, String cifId, String channelId,
			String cifType ,String jsonData);
	
	public ResponseData updateRequestToken(String token, String updatedToken);
	
}
