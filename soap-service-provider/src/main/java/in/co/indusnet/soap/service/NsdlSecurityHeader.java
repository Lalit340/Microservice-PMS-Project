package in.co.indusnet.soap.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.soap.saaj.SaajSoapMessage;

@Configuration
@PropertySource("classpath:constant/constantEnv.properties")
public class NsdlSecurityHeader implements WebServiceMessageCallback {
	
	public String nsdlSoapAction;
	public static String soapMessageData;
	
	public NsdlSecurityHeader() {
		super();
	}
	
	public NsdlSecurityHeader(String nsdlSoapAction) {
		this.nsdlSoapAction=nsdlSoapAction;
	}
	
	@Override
	public void doWithMessage(WebServiceMessage message) throws IOException {
		try {

			((SaajSoapMessage) message).setSoapAction(nsdlSoapAction);

			SOAPMessage soapMessage = ((SaajSoapMessage) message).getSaajMessage();
			SOAPHeader header = soapMessage.getSOAPHeader();
			header.setPrefix("soapenv");

			MimeHeaders mimeHeader = soapMessage.getMimeHeaders();
			mimeHeader.setHeader("X-IBM-Client-Id", "2b12177f-6663-4155-b47b-859e7e238f3a");
			mimeHeader.setHeader("X-IBM-Client-Secret", "D8fY4dX8vA8qQ7sJ0aM0bL4yO5aA3sY1qY1mF1yJ6bW5wL7pJ1");

			SOAPPart soapPart = soapMessage.getSOAPPart();
			SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
			soapEnvelope.addNamespaceDeclaration("ind", "http://MDCESBDEV.IBL.COM/IndusInd_RBO_Account_NSDL.ws:PanValidation");
			soapEnvelope.setPrefix("soapenv");

			soapEnvelope.removeNamespaceDeclaration("SOAP-ENV");

			SOAPBody body = soapMessage.getSOAPBody();
			body.setPrefix("soapenv");
			
			soapMessage.writeTo(System.out);
			
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			soapMessage.writeTo(stream);
			soapMessageData = new String(stream.toByteArray(), "utf-8") ;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
