package in.co.indusnet.soap.utility;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import in.co.indusnet.soap.model.fdcmi.FdcmiTblRequestResponseLog;
import in.co.indusnet.soap.model.rekyc.RekycTblTalismaLog;
import in.co.indusnet.soap.repository.fdcmi.FdcmiLogRepository;
import in.co.indusnet.soap.repository.rekyc.RekycTblTalismaRepository;

@Component
public class TalismaLog {

	@Autowired
	private RekycTblTalismaRepository rekycTalismaLogRepository;
	
	@Autowired
	private FdcmiLogRepository fdcmiLogRepository;
	
	public void logEntryProcess(
			LocalDateTime requestDateTime,
			long reqId,
			long cifTblId,
			String request,
			String soapResponse,
			int errorCode,
			String endPointUrl,
			String errorMessage,
			int interactionId,
			String interactionState,
			LocalDateTime responseTime,
			String 	responseStatus,
			String serviceType) {
		
		request = request.replaceAll("&lt;", "<");
		request = request.replaceAll("&gt;", ">");
		switch(serviceType) {
		case "rekyc" : 
			RekycTblTalismaLog rekycTblRequestResponseLog = new RekycTblTalismaLog(); 
			rekycTblRequestResponseLog.setCreatedAt(requestDateTime);
			rekycTblRequestResponseLog.setEndUrl(endPointUrl);
			rekycTblRequestResponseLog.setCifId(cifTblId);
			rekycTblRequestResponseLog.setErrorCode(errorCode);
			rekycTblRequestResponseLog.setErrorMessage(errorMessage);
			rekycTblRequestResponseLog.setAdminUpdated(false);
			rekycTblRequestResponseLog.setInteractionId(interactionId);
			rekycTblRequestResponseLog.setInteractionState(interactionState);
			rekycTblRequestResponseLog.setResponseStatus(responseStatus);
			rekycTblRequestResponseLog.setRequest(request);
			rekycTblRequestResponseLog.setResponse(soapResponse);
			rekycTblRequestResponseLog.setRequestTime(requestDateTime);
			rekycTblRequestResponseLog.setResponseTime(responseTime);
			rekycTblRequestResponseLog.setReqId(reqId);
			rekycTblRequestResponseLog.setApiHitCount(1);
			rekycTalismaLogRepository.save(rekycTblRequestResponseLog);
			break;
			
		case "fdrd" :
			FdcmiTblRequestResponseLog fdcmiTblRequestResponseLog = new FdcmiTblRequestResponseLog();
			fdcmiTblRequestResponseLog.setCreatedAt(requestDateTime);
			fdcmiTblRequestResponseLog.setEndUrl(endPointUrl);
			fdcmiTblRequestResponseLog.setRequest(request);
			fdcmiTblRequestResponseLog.setResponse(soapResponse);
			fdcmiTblRequestResponseLog.setRequestTime(requestDateTime);
			fdcmiTblRequestResponseLog.setResponseTime(responseTime);
			fdcmiTblRequestResponseLog.setReqId(reqId);
			fdcmiLogRepository.save(fdcmiTblRequestResponseLog);
			break;
			
		default : 
			System.out.println("Invalid service type");
		}
	}

}
