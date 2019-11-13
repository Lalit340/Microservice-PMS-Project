package in.co.indusnet.soap.service;

import java.time.LocalDateTime;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

import soap.finacle.wsdl.ExecuteService;
import soap.finacle.wsdl.ExecuteServiceResponse;
import in.co.indusnet.soap.utility.RequestResponseLog;

public class FinacleClient extends WebServiceGatewaySupport {

	String soapResponse;
	LocalDateTime responseTime;

	@Autowired
	private RequestResponseLog requestResponseLog;

	public String executeSoap(String data, String endPointUrl) {
		// String endPointUrl = environment.getProperty("finacleEndPointUrl");
		try {
			ExecuteService executeService = new ExecuteService();
			executeService.setArg00(data);

			ExecuteServiceResponse response = (ExecuteServiceResponse) getTemplate().marshalSendAndReceive(endPointUrl,
					executeService, new FinacleSecurityHeader());

			soapResponse = response.getExecuteServiceReturn();
			responseTime = LocalDateTime.now();
			return response.getExecuteServiceReturn();
			// return decoded;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private WebServiceTemplate getTemplate() throws SOAPException {
		// For setting up application/soap+xml; charset=utf-8
		MessageFactory msgFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
		SaajSoapMessageFactory saajSoapMessageFactory = new SaajSoapMessageFactory(msgFactory);

		WebServiceTemplate wsTemplate = getWebServiceTemplate();
		wsTemplate.setMessageFactory(saajSoapMessageFactory);

		return wsTemplate;
	}

	public long logEntryProcess(String data, LocalDateTime requestDateTime, Long reqId, String endPointUrl,
			String serviceType, String activityType) {
		long id = requestResponseLog.logEntryProcess(requestDateTime, reqId, FinacleSecurityHeader.soapMessageData,
				soapResponse, endPointUrl, responseTime, serviceType, activityType);
		return id;
	}
}
