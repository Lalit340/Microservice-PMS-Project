package in.co.indusnet.soap.service;

import java.time.LocalDateTime;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

import in.co.indusnet.soap.utility.Converter;
import in.co.indusnet.soap.utility.TalismaLog;
import soap.talisma.wsdl.CreateServiceRequest;
import soap.talisma.wsdl.CreateServiceRequestResponse;


@PropertySource("classpath:constant/constantEnv.properties")
@PropertySource("classpath:constant/constant.properties")
public class TalismaClient extends WebServiceGatewaySupport {

	@Autowired
	private Environment environment;
	
	@Autowired
	private TalismaLog talismaLog;
	
	@Autowired
	private Converter  converter;
	
	
	
	String soapResponse;
	LocalDateTime responseTime;
	LocalDateTime requestDateTime;

	public String createServiceRequest(String accountNo, String cifId, String panNo,
			String accActivationConsent, String addressProofType, String idProofType, String mobileNumber,
			String emailId, String verificationMode, String newAddress, String interactionState, long reqId, String serviceType, long cifTableId) {
		CreateServiceRequest creServiceReq = new CreateServiceRequest();
		
		String talismaSoapAction = environment.getProperty("talismaSoapAction");
		System.out.println("Talisma soap action :: " + talismaSoapAction);
		
		creServiceReq.setAccountNo(accountNo);
		creServiceReq.setCIFID(cifId);
		creServiceReq.setCallRelatedTo(environment.getProperty("callRelatedTo"));
		creServiceReq.setCallType(environment.getProperty("callType"));
		creServiceReq.setCallSubType(environment.getProperty("callSubType"));
		creServiceReq.setMedia(environment.getProperty("media"));
		creServiceReq.setInteractionState(interactionState);
		creServiceReq.setTeam(environment.getProperty("team"));
		creServiceReq.setSubject(environment.getProperty("subject"));
		creServiceReq.setMessage(environment.getProperty("message"));
		creServiceReq.setSMSFlag(true);
		creServiceReq.setEmailFlag(false);
		creServiceReq.setXMLStr("<Talisma>\n" + "<TalismaPropertiesDetails>\n" + "<Property ID=\""
				+ environment.getProperty("talismaPropertyIdF1") + "\">CIF ID:" + cifId + "</Property>\n" + "<Property ID=\""
				+ environment.getProperty("talismaPropertyIdF2") + "\">Account No.:" + accountNo + "</Property>\n"
				+ "<Property ID=\"" + environment.getProperty("talismaPropertyIdF3") + "\">PAN No: " + panNo + "</Property>\n"
				+ "<Property ID=\"" + environment.getProperty("talismaPropertyIdF4") + "\">Dormant A/c Activation consent: "
				+ accActivationConsent + "</Property>\n" + "<Property ID=\"" + environment.getProperty("talismaPropertyIdF5")
				+ "\">Type of Address Proof: " + addressProofType + "</Property>\n" + "<Property ID=\""
				+ environment.getProperty("propertyId6") + "\">Type of ID Proof: " + idProofType + "</Property>\n"
				+ "<Property ID=\"" + environment.getProperty("talismaPropertyIdF7") + "\">Mobile Number for OTP:"
				+ mobileNumber + "</Property>\n" + "<Property ID=\"" + environment.getProperty("talismaPropertyIdF8")
				+ "\">Email ID for OTP:" + emailId + "</Property>\n" + "<Property ID=\""
				+ environment.getProperty("talismaPropertyIdF9") + "\">Verification Mode:" + verificationMode + "</Property>\n"
				+ "<Property ID=\"" + environment.getProperty("talismaPropertyIdF10") + "\">New Address:" + newAddress
				+ "</Property>\n" + "</TalismaPropertiesDetails>\n" + "</Talisma>");
		JAXBElement<?> jaxbElement = new JAXBElement<>(new QName("tem:CreateServiceRequest"),
				CreateServiceRequest.class, creServiceReq);

		CreateServiceRequestResponse response = null;
		String createServiceRequestResult = null;
		long interactionID = 0;
		String errorCode = null;
		String errorMessage = null;
		String jsonData = null;
		try {
			String endPointUrl = environment.getProperty("talismaUrl");
			response = (CreateServiceRequestResponse) getTemplate().marshalSendAndReceive(
					endPointUrl, jaxbElement, (new TalismaSecurityHeader(talismaSoapAction)));

			createServiceRequestResult = response.getCreateServiceRequestResult();
			interactionID = response.getInteractionID();
			errorCode = response.getErrorCode();
			errorMessage = response.getErrorMessage();
			
			requestDateTime = LocalDateTime.now();
			int interactionId = (int)interactionID;
			jsonData = "{" + "\"" + "createServiceRequestResult\"" + ":\"" + createServiceRequestResult + "\"" + ","
					+ "\"interactionID\"" + ":" + interactionID + "," + "\"errorCode\"" + ":" + "\"" + errorCode + "\""
					+ "," + "\"errorMessage\"" + ":" + "\"" + errorMessage + "\"" + "}";
			System.out.println("\nJson Data :: " + jsonData);
			soapResponse = jsonData;
			this.logEntryProcess(
					reqId, 
					endPointUrl,
					serviceType,
					cifTableId,
					Integer.parseInt(errorCode),
					errorMessage,
					interactionId,
					interactionState,
					responseTime,
					createServiceRequestResult);
			
			
		} catch (SOAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return jsonData;

	}
	
	public String createServiceRequestFdcmi(String accNo,String cifId,String interactionState,String openingDate,
			String maturityDate,String maturityPeriod,String depositeAmount,String creditToAccount,
			String updatedRenewalType, String currentMaturityInstruction, String updatedMaturityInstruction,
			String mobileNumber,String emailIdForOtp,String verficationMode,
			long cifTableId, long reqId, String serviceType) {
		
		CreateServiceRequest creServiceReq = new CreateServiceRequest();
		creServiceReq.setAccountNo(accNo);
		creServiceReq.setCIFID(cifId);
		creServiceReq.setCallRelatedTo(environment.getProperty("callRelatedTo"));
		creServiceReq.setCallType(environment.getProperty("callType"));
		creServiceReq.setCallSubType(environment.getProperty("callSubTypeFdcmi"));
		creServiceReq.setMedia(environment.getProperty("media"));
		creServiceReq.setInteractionState(interactionState);
		creServiceReq.setTeam(environment.getProperty("team"));
		creServiceReq.setSubject(environment.getProperty("subjectFdcmi"));
		creServiceReq.setMessage(environment.getProperty("messageFdcmi"));
		creServiceReq.setSMSFlag(true);
		creServiceReq.setEmailFlag(false);
		String xmlStr = "<Talisma>\n" + 
				"	<TalismaPropertiesDetails>\n" + 
				"		<Property ID=\""+ environment.getProperty("talismaPropertyIdF1") +"\">Account No.:"+accNo+"</Property>\n" + 
				"		<Property ID=\""+ environment.getProperty("talismaPropertyIdF2") +"\">CIF ID:"+cifId+"</Property>\n" + 
				"		<Property ID=\""+ environment.getProperty("talismaPropertyIdF3") +"\">FD/RD Account No.: "+ accNo +"</Property>\n" + 
				"		<Property ID=\""+  environment.getProperty("talismaPropertyIdF4") +"\">FD/RD Opening Date: "+ openingDate +"</Property>\n" + 
				"		<Property ID=\""+ environment.getProperty("talismaPropertyIdF5") +"\">FD/RD Maturity Date: "+ maturityDate + "</Property>\n" + 
				"		<Property ID=\""+  environment.getProperty("talismaPropertyIdF6") +"\">FD/RD Maturity Period: "+ maturityPeriod +"</Property>\n" + 
				"		<Property ID=\""+ environment.getProperty("talismaPropertyIdF7") +"\">Deposit Amount: "+ depositeAmount +"</Property>\n" + 
				"		<Property ID=\""+ environment.getProperty("talismaPropertyIdF8")+"\">Credit to Account: "+ creditToAccount +"</Property>\n" + 
				"		<Property ID=\""+  environment.getProperty("talismaPropertyIdF9") +"\">Current Maturity Instruction: "+currentMaturityInstruction+"</Property>\n" + 
				"		<Property ID=\""+ environment.getProperty("talismaPropertyIdF10") +"\">Updated Maturity Instruction: "+updatedMaturityInstruction+"</Property>\n" + 
				"		<Property ID=\""+ environment.getProperty("talismaPropertyIdF15") +"\">Updated Renewal type:"+ updatedRenewalType +"</Property>\n" + 
				"		<Property ID=\""+ environment.getProperty("talismaPropertyIdF11") +"\">Mobile Number:"+ mobileNumber +"</Property>\n" + 
				"		<Property ID=\""+ environment.getProperty("talismaPropertyIdF12") +"\">Mobile Number for OTP:"+ mobileNumber +"</Property>\n" + 
				"		<Property ID=\""+ environment.getProperty("talismaPropertyIdF13")+"\">Email ID for OTP:"+ emailIdForOtp +"</Property>\n" + 
				"		<Property ID=\""+environment.getProperty("talismaPropertyIdF14")+"\">Verification Mode:"+verficationMode+"</Property>\n" + 
				"	</TalismaPropertiesDetails>\n" + 
				"</Talisma>";
		creServiceReq.setXMLStr(xmlStr);
		
		JAXBElement<?> jaxbElement = new JAXBElement<>(new QName("tem:CreateServiceRequest"),
				CreateServiceRequest.class, creServiceReq);
		
		CreateServiceRequestResponse response = null;
		String createServiceRequestResult = null;
		long interactionID = 0;
		String errorCode = null;
		String errorMessage = null;
		String jsonData = null;
		
		
		try {
			String endPointUrl = environment.getProperty("talismaUrl");
			response = (CreateServiceRequestResponse) getTemplate().marshalSendAndReceive(
					endPointUrl, jaxbElement, (new TalismaSecurityHeader()));

			createServiceRequestResult = response.getCreateServiceRequestResult();
			interactionID = response.getInteractionID();
			errorCode = response.getErrorCode();
			errorMessage = response.getErrorMessage();
			
			requestDateTime = LocalDateTime.now();
			int interactionId = (int)interactionID;
			jsonData = "{" + "\"" + "createServiceRequestResult\"" + ":\"" + createServiceRequestResult + "\"" + ","
					+ "\"interactionID\"" + ":" + interactionID + "," + "\"errorCode\"" + ":" + "\"" + errorCode + "\""
					+ "," + "\"errorMessage\"" + ":" + "\"" + errorMessage + "\"" + "}";
			System.out.println("\nJson Data :: " + jsonData);
			soapResponse = jsonData;
			this.logEntryProcess(
					reqId, 
					endPointUrl,
					serviceType,
					cifTableId,
					Integer.parseInt(errorCode),
					errorMessage,
					interactionId,
					interactionState,
					responseTime,
					createServiceRequestResult);
			
			
		} catch (SOAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return jsonData;
		
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
			long reqId, 
			String endPointUrl,
			String serviceType,
			long cifTblId,
			int errorCode,
			String errorMessage,
			int interactionId,
			String interactionState,
			LocalDateTime responseTime,
			String responseStatus)
	{
		
		talismaLog.logEntryProcess(
				requestDateTime,
				reqId,
				cifTblId,
				TalismaSecurityHeader.soapMessageData,
				soapResponse,
				errorCode,
				endPointUrl,
				errorMessage,
				interactionId,
				interactionState,
				responseTime,
				responseStatus,
				serviceType);
	}

}
