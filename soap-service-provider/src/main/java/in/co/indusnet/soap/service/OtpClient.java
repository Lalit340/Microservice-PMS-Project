package in.co.indusnet.soap.service;

import java.time.LocalDateTime;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

import in.co.indusnet.soap.utility.RequestResponseLog;
import soap.otp.wsdl.GenerateOtpRequest;
import soap.otp.wsdl.GenerateOtpRequestResponse;
import soap.otp.wsdl.ValidateOtpRequest;
import soap.otp.wsdl.ValidateOtpRequestResponse;

public class OtpClient extends WebServiceGatewaySupport {

	String sendOtpRsponse;
	String validateOtpResponse;
	LocalDateTime responseTime;

	@Autowired
	private RequestResponseLog requestResponseLog;

	public String sendOtp(String mobileNo, String sendOtpChannelId, String endPointUrl) {
		try {
			GenerateOtpRequest generateOtpRequest = new GenerateOtpRequest();
			generateOtpRequest.setMobileNum(mobileNo);
			generateOtpRequest.setChannelId(sendOtpChannelId);

			GenerateOtpRequestResponse response = (GenerateOtpRequestResponse) getTemplate()
					.marshalSendAndReceive(endPointUrl, generateOtpRequest, new OtpSecurityHeader());

			sendOtpRsponse = response.getGenerateOtpRequestReturn();
			responseTime = LocalDateTime.now();
			return response.getGenerateOtpRequestReturn();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String validateOtp(String mobileNo, String referenceId, String otp, String endPointUrl) {
		try {
			ValidateOtpRequest validateOtpRequest = new ValidateOtpRequest();
			validateOtpRequest.setMobileNo(mobileNo);
			validateOtpRequest.setReferenceId(referenceId);
			validateOtpRequest.setOtp(otp);

			ValidateOtpRequestResponse response = (ValidateOtpRequestResponse) getTemplate()
					.marshalSendAndReceive(endPointUrl, validateOtpRequest, new OtpSecurityHeader());

			validateOtpResponse = response.getValidateOtpRequestReturn();
			responseTime = LocalDateTime.now();
			return response.getValidateOtpRequestReturn();
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

	public void sendOtpLogEntryProcess(LocalDateTime requestDateTime, long reqId, String endPointUrl,
			String serviceType, String activityType) {
		requestResponseLog.logEntryProcess(requestDateTime, reqId, OtpSecurityHeader.soapMessageData, sendOtpRsponse,
				endPointUrl, responseTime, serviceType, activityType);
	}
	
	public void validateOtpLogEntryProcess(LocalDateTime requestDateTime, long reqId, String endPointUrl,
			String serviceType, String activityType) {
		requestResponseLog.logEntryProcess(requestDateTime, reqId, OtpSecurityHeader.soapMessageData, validateOtpResponse,
				endPointUrl, responseTime, serviceType, activityType);
	}
}