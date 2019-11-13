package in.co.indusnet.soap.service;

import java.time.LocalDateTime;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

import in.co.indusnet.soap.utility.RequestResponseLog;
import soap.indusweb.wsdl.ProcessIndusWebServiceReq;
import soap.indusweb.wsdl.ProcessIndusWebServiceReqResponse;


@PropertySource("classpath:constant/constantEnv.properties")
@PropertySource("classpath:constant/constant.properties")
public class IndusWebClient extends WebServiceGatewaySupport {
	
	String soapResponse ;
	LocalDateTime responseTime ; 
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private RequestResponseLog requestResponseLog;
	
	public ProcessIndusWebServiceReqResponse executeSoap(String data,String endPointUrl) {
		//String endPointUrl = environment.getProperty("finacleEndPointUrl");
		try {
			
			String indusWebSoapAction = environment.getProperty("indusWebSoapAction");
			
			ProcessIndusWebServiceReq indusWebServiceReq = new ProcessIndusWebServiceReq();			
			indusWebServiceReq.setInputRequest(data);
			
			JAXBElement<?> jaxbElement = new JAXBElement<>(
					new QName("ser:processIndusWebServiceReq"),
					ProcessIndusWebServiceReq.class, indusWebServiceReq);
			
			JAXBElement<ProcessIndusWebServiceReqResponse> response = (JAXBElement<ProcessIndusWebServiceReqResponse>) getTemplate()
					.marshalSendAndReceive(endPointUrl, jaxbElement, (new IndusWebSecurityHeader(indusWebSoapAction)));
			
			soapResponse = response.getValue().getReturn();
			responseTime = LocalDateTime.now();
			return response.getValue();
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
	
	public long logEntryProcess(
			String data,
			LocalDateTime requestDateTime,
			Long reqId, 
			String endPointUrl,
			String serviceType,
			String activityType) {
		long id = requestResponseLog.logEntryProcess(requestDateTime, reqId, IndusWebSecurityHeader.soapMessageData, soapResponse, endPointUrl, responseTime, serviceType, activityType);
		return id;
	}

}
