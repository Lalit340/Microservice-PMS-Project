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

import in.co.indusnet.soap.utility.Converter;
import in.co.indusnet.soap.utility.RequestResponseLog;
import soap.nsdl.wsdl.DocPANReq;
import soap.nsdl.wsdl.PanRequest;
import soap.nsdl.wsdl.ProcessPANValidation;
import soap.nsdl.wsdl.ProcessPANValidationResponse;

@PropertySource("classpath:constant/constantEnv.properties")
@PropertySource("classpath:constant/constant.properties")
public class NsdlClient extends WebServiceGatewaySupport {
	
	// String soapResponse ;
	LocalDateTime responseTime ; 
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private RequestResponseLog requestResponseLog;
	
	@Autowired
	private Converter  converter;
	
	public ProcessPANValidationResponse panValidate(String sourceSys, String panNumber, String endPointUrl) {
		try {
			
			String nsdlSoapAction = environment.getProperty("nsdlSoapAction");
			
			JAXBElement<String> sourceSystem = new JAXBElement<String>(
					new QName("SourceSys"),
					String.class, sourceSys);
			
			JAXBElement<String> panNum = new JAXBElement<String>(
					new QName("PanNumber"),
					String.class, panNumber);
			
			PanRequest panReq = new PanRequest();
			panReq.setPanNumber(panNum);
			
			DocPANReq docPanReq = new DocPANReq();
			docPanReq.setSourceSys(sourceSystem);
			docPanReq.getPanRequest().add(panReq);			
			//docPanReq.setPanRequest(panReqList);
			
			ProcessPANValidation panValidationReq = new ProcessPANValidation();
			panValidationReq.setDocPANReq(docPanReq);
			
			JAXBElement<?> indPanValidationReq = new JAXBElement<>(
					new QName("ind:processPANValidation"),
					ProcessPANValidation.class, panValidationReq);
			
			JAXBElement<ProcessPANValidationResponse> response = (JAXBElement<ProcessPANValidationResponse>) getTemplate()
					.marshalSendAndReceive(endPointUrl, indPanValidationReq, (new NsdlSecurityHeader(nsdlSoapAction)));			
			
			// soapResponse = converter.jsonToXml(response.getValue());
			responseTime = LocalDateTime.now();
			
			return response.getValue();
			
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
	
	public void logEntryProcess(
			String responseData,
			LocalDateTime requestDateTime,
			Long reqId, 
			String endPointUrl,
			String serviceType,
			String activityType) {
		requestResponseLog.logEntryProcess(requestDateTime, reqId, NsdlSecurityHeader.soapMessageData, responseData, endPointUrl, responseTime, serviceType, activityType);
	}

}