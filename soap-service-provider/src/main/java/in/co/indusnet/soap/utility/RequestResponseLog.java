package in.co.indusnet.soap.utility;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import in.co.indusnet.soap.model.fdcmi.FdcmiTblRequestResponseLog;
import in.co.indusnet.soap.model.rekyc.RekycTblRequestResponseLog;
import in.co.indusnet.soap.repository.fdcmi.FdcmiLogRepository;
import in.co.indusnet.soap.repository.rekyc.RekycLogRepository;

@Component
public class RequestResponseLog {

	@Autowired
	private RekycLogRepository rekycLogRepository;

	@Autowired
	private FdcmiLogRepository fdcmiLogRepository;

	public long logEntryProcess(LocalDateTime requestDateTime, Long reqId, String request, String soapResponse,
			String endPointUrl, LocalDateTime responseTime, String serviceType, String activityType) {
		long id = 0;
		request = request.replaceAll("&lt;", "<");
		request = request.replaceAll("&gt;", ">");
		switch (serviceType) {
		case "rekyc":
			RekycTblRequestResponseLog rekycTblRequestResponseLog = new RekycTblRequestResponseLog();
			rekycTblRequestResponseLog.setCreatedAt(requestDateTime);
			rekycTblRequestResponseLog.setEndUrl(endPointUrl);
			rekycTblRequestResponseLog.setActivityType(activityType);
			rekycTblRequestResponseLog.setRequest(request);
			rekycTblRequestResponseLog.setResponse(soapResponse);
			rekycTblRequestResponseLog.setRequestTime(requestDateTime);
			rekycTblRequestResponseLog.setResponseTime(responseTime);
			rekycTblRequestResponseLog.setReqId(reqId);
			RekycTblRequestResponseLog rekycTblRequestResponse = rekycLogRepository.save(rekycTblRequestResponseLog);
			return id = rekycTblRequestResponse.getId();

		case "fdrd":
			FdcmiTblRequestResponseLog fdcmiTblRequestResponseLog1 = new FdcmiTblRequestResponseLog();
			fdcmiTblRequestResponseLog1.setCreatedAt(requestDateTime);
			fdcmiTblRequestResponseLog1.setEndUrl(endPointUrl);
			fdcmiTblRequestResponseLog1.setActivityType(activityType);
			fdcmiTblRequestResponseLog1.setRequest(request);
			fdcmiTblRequestResponseLog1.setResponse(soapResponse);
			fdcmiTblRequestResponseLog1.setRequestTime(requestDateTime);
			fdcmiTblRequestResponseLog1.setResponseTime(responseTime);
			fdcmiTblRequestResponseLog1.setReqId(reqId);
			FdcmiTblRequestResponseLog fdcmiTblRequestResponse = fdcmiLogRepository.save(fdcmiTblRequestResponseLog1);
			id = fdcmiTblRequestResponse.getId();
			return id;
		default:
			return id;
		}

	}

	public void logEntryUpdate(String soapResponse, LocalDateTime updateTime, String serviceType , long id) {

		switch (serviceType) {
		case "rekyc":
			Optional<RekycTblRequestResponseLog> rekycTblRequestResponse = rekycLogRepository.findById(id);
			rekycTblRequestResponse.get().setResponse(soapResponse);
			rekycTblRequestResponse.get().setUpdatedAt(updateTime);
			rekycLogRepository.save(rekycTblRequestResponse.get());
			break;

		case "fdrd":
			Optional<FdcmiTblRequestResponseLog> fdcmiTblRequestResponse = fdcmiLogRepository.findById(id);
			fdcmiTblRequestResponse.get().setResponse(soapResponse);
			fdcmiTblRequestResponse.get().setUpdatedAt(updateTime);
			fdcmiLogRepository.save(fdcmiTblRequestResponse.get());
            break;
            
		default:
			break;
		}

	}

}
