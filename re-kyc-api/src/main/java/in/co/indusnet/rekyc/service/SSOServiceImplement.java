package in.co.indusnet.rekyc.service;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import in.co.indusnet.rekyc.exception.ServiceException;
import in.co.indusnet.rekyc.model.TBLRequestlog;
import in.co.indusnet.rekyc.model.TblRequestToKey;
import in.co.indusnet.rekyc.repository.TblRequestKeyRepository;
import in.co.indusnet.rekyc.repository.TblRequestLogRepository;
import in.co.indusnet.rekyc.response.ResponseData;
import in.co.indusnet.rekyc.utility.AllTokenCreateAndValidate;
import in.co.indusnet.rekyc.utility.GetErrorMessage;
import in.co.indusnet.rekyc.utility.ResponseHelper;

@Configuration
@Service("service")
@PropertySource("classpath:constant/constantEnv.properties")
public class SSOServiceImplement implements SSOService {

	@Autowired
	private TblRequestLogRepository tblRepository;

	@Autowired
	private TblRequestKeyRepository tblKeyRepository;

	@Autowired
	private HttpServletResponse response;

	@Autowired
	private AllTokenCreateAndValidate allTokenCreateAndValidate;

	@Autowired
	private GetErrorMessage getErrorMessage;
	
	@Autowired
	private Environment environment;

	/*
	 * This method purpose is validating data , inserting data to the database and
	 * redirect to the landing page
	 */
	@Override
	public ResponseData tblRequestData(String requestType, String sessionId, String cifId, String channelId,
			String cifType , String jsonData) {

		//TBLRequestlog  object
		TBLRequestlog requestLog = null;


		/* Mapping the data to model Odject */
		requestLog = new TBLRequestlog();
		/* Mapping all data */
		requestLog.setSsoReqType(requestType);
		requestLog.setSsoCifType(cifType);
		requestLog.setSsoCifCode(cifId);
		requestLog.setSsoSessionId(sessionId);
		requestLog.setSsoChannelId(channelId);
		requestLog.setSsoData(jsonData);
		requestLog.setCreatedAt(LocalDateTime.now());
		requestLog.setUpdatedAt(LocalDateTime.now());

		/* Insert data in request log table */

		TBLRequestlog tblResponse = tblRepository.save(requestLog);

		/* If data inserted in request log table */
		if (tblResponse == null)
			throw new ServiceException(getErrorMessage.getErrorMessages("UNABLE_TO_PROCESS_ERR"), HttpStatus.UNAUTHORIZED.value());
		if (tblResponse != requestLog)
			throw new ServiceException(getErrorMessage.getErrorMessages("UNABLE_TO_PROCESS_ERR"), HttpStatus.UNAUTHORIZED.value());

		String tokenVal = allTokenCreateAndValidate.getBasicToken();
		
		TblRequestToKey requestKey = new TblRequestToKey();
		requestKey.setTblRequestLog(tblResponse);
		requestKey.setToken(tokenVal);
		if (requestKey.isTokenStatus() == false) {
			requestKey.setTokenStatus(true);
		}
		requestKey.setCreatedAt(LocalDateTime.now());
		requestKey.setUpdatedAt(LocalDateTime.now());
		tblKeyRepository.save(requestKey);
		String url = environment.getProperty("ssoRekycUrl")+tokenVal;
		try {
			// redirecting page to user end			
			response.sendRedirect(url);
		} catch (Exception e) {
			throw new ServiceException(getErrorMessage.getErrorMessages("UNABLE_TO_PROCESS_ERR"), HttpStatus.UNAUTHORIZED.value());
		}
		// Response object. To check in postman or swagger
		ResponseData response = ResponseHelper.responseSender(
				"Data saved and redirected to the front-end - " + url, HttpStatus.OK.value());
		return response;

	}

	@Override
	public ResponseData updateRequestToken(String token, String updatedToken) {
		TblRequestToKey requestToKey = tblKeyRepository.findByToken(token);
		String newToken = updatedToken.substring(0,9)+ token.substring(9,token.length()-9) + updatedToken.substring(updatedToken.length()-11, updatedToken.length()-2);
		requestToKey.setToken(newToken);
		requestToKey.setUpdatedAt(LocalDateTime.now());
		tblKeyRepository.save(requestToKey);
		
		ResponseData response = ResponseHelper.responseSender(
				"Token updated successfully.", HttpStatus.OK.value());
		return response;
	}
	

}
