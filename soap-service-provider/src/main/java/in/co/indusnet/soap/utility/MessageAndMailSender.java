package in.co.indusnet.soap.utility;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;



@Component
@Configuration
@PropertySource("classpath:constant/constantEnv.properties")
@PropertySource("classpath:constant/constant.properties")
public class MessageAndMailSender {

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private Environment environment;
	
	@Autowired
	RequestResponseLog requestResponseLog;

	public ResponseEntity<String> messageSender(String mobString, String msgString , long reqId , String serviceType) {
		String baseUrl = environment.getProperty("sendSmsWSUrl");
		String refId = "RefId"+Generator.randomNumberGenerator(8);
		String sendSMSChannelId = environment.getProperty("sendSMSChannelId");
		String sendSMSKey = environment.getProperty("sendSMSKey");
		ResponseEntity<String> response = null;
		String responseData = null;
		try {
			URI uri = new URI(baseUrl);
			String xmlData = "<Root>" + "<ChnlId>" + sendSMSChannelId + "</ChnlId>" + "<Key>" + sendSMSKey+ "</Key>" + "<Row>"
					+ "<RefId>" + refId + "</RefId>" + "<MobNo>" + mobString + "</MobNo>"
					+ "<Msg>" + msgString + "</Msg>" + "</Row>" + "</Root>";

			HttpHeaders headers = new HttpHeaders();
			headers.set("X-IBM-Client-Secret", "D8fY4dX8vA8qQ7sJ0aM0bL4yO5aA3sY1qY1mF1yJ6bW5wL7pJ1");
			headers.set("X-IBM-Client-Id", "2b12177f-6663-4155-b47b-859e7e238f3a");
			headers.set("Content-Type", "application/xml; charset=utf-8");	

			String activityType = environment.getProperty("sendSuccessSms");
			long id = requestResponseLog.logEntryProcess(LocalDateTime.now(), reqId, xmlData, null, baseUrl, LocalDateTime.now(), serviceType, activityType);
			
			HttpEntity<String> request = new HttpEntity<>(xmlData, headers);
			response = restTemplate.postForEntity(uri, request, String.class);
						
			System.out.println("Response data after::" + response);					
			
			if(response.getStatusCode() == HttpStatus.OK) {
				responseData = response.getBody();
			requestResponseLog.logEntryUpdate(response.getBody(), LocalDateTime.now(), serviceType, id);
			}
			

			} catch (URISyntaxException e) {
				e.printStackTrace();
			} 
		return new ResponseEntity<>(responseData, HttpStatus.OK);

	}

	/*public XmlResponse sendSms(String mobNo, String msg , long reqId , String serviceType) {
        System.out.println("value object in side");
		ResponseEntity<String> value = this.messageSender(mobNo,msg, reqId, serviceType);
        System.out.println("value object ::"+value);
		JAXBContext jaxbContext;
		String data = value.getBody().replace('R', 'r');
		System.out.println("new Data ::" + data);

		String xmlString = data.replace('E', 'e');
		System.out.println("new Data ::" + xmlString);

		try {

			jaxbContext = JAXBContext.newInstance(XmlResponse.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			XmlResponse response = (XmlResponse) jaxbUnmarshaller.unmarshal(new StringReader(xmlString));

			System.out.println("response data ::" + response.getRspMsg());

			return response;

		} catch (JAXBException e) {
			throw new ServiceException("JAXBException data not found ," + e.getMessage(), HttpStatus.NOT_ACCEPTABLE.value());
		}
	}*/

	public ResponseEntity<String> mailSender( String email, String subject, String content , long reqId , String serviceType) {
		String baseUrl = environment.getProperty("sendEmailWSUrl");
		System.out.println("url ::"+baseUrl);
		String refId = Generator.randomNumberGenerator(8);
		String sendEmailChannelId = environment.getProperty("sendEmailChannelId");
		String sendEmailKey = environment.getProperty("sendEmailKey");
		String txncode = environment.getProperty("sendEmailTxncode");
		ResponseEntity<String> response = null;
		String responseData = null;
		try {
			URI uri = new URI(baseUrl);
			String xmlData = "<Root>" + 
					"<ChnlId>" + sendEmailChannelId + "</ChnlId>" + 
					"<Key>" + sendEmailKey + "</Key>" + 
					"<Row>" + 
					"<RefId>" + refId + "</RefId>" + 
					"<Txncode>" + txncode + "</Txncode>" + 
					"<Emailid>" + email + "</Emailid>" + 
					"<Subject>" + subject + "</Subject>" + 
					"<Msg>" + 
					"<![CDATA[" + content + "']]>" + 
					"</Msg>" + 
					"<Attachment></Attachment>" + 
					"</Row>" + 
					"</Root>";
			System.out.println("Request data::"+xmlData);		

			HttpHeaders headers = new HttpHeaders();
			headers.set("X-IBM-Client-Secret", "D8fY4dX8vA8qQ7sJ0aM0bL4yO5aA3sY1qY1mF1yJ6bW5wL7pJ1");
			headers.set("X-IBM-Client-Id", "2b12177f-6663-4155-b47b-859e7e238f3a");
			headers.set("content-type", "application/xml");	
			
			String activityType = environment.getProperty("sendSuccessMail");
			long id=requestResponseLog.logEntryProcess(LocalDateTime.now(), reqId, xmlData, null, baseUrl, LocalDateTime.now(), serviceType, activityType);

						
			HttpEntity<String> request = new HttpEntity<>(xmlData, headers);
			
			System.out.println(" hi :: "+id);	
			
			response = restTemplate.postForEntity(uri, request, String.class);
			
			System.out.println("Response data::" + response);					
			
			if(response.getStatusCode() == HttpStatus.OK) {
				responseData = response.getBody();
							requestResponseLog.logEntryUpdate(response.getBody(), LocalDateTime.now(), serviceType, id);
			}

			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		return new ResponseEntity<>(responseData, HttpStatus.OK);

	}
	public ResponseEntity<String> mailOtpSender( String email, String subject, String content , long reqId , String serviceType) {
		String baseUrl = environment.getProperty("sendOtpViaEmailWSURL");
		String refId = Generator.randomNumberGenerator(8);
		String sendEmailChannelId = environment.getProperty("sendOtpViaEmailChnlId");
		String sendEmailKey = environment.getProperty("sendOtpViaEmailKey");
		String txncode = environment.getProperty("sendOtpEmailTxncode");
		ResponseEntity<String> response = null;
		String responseData = null;
		try {
			URI uri = new URI(baseUrl);
			String xmlData = "<Root>" + 
					"<ChnlId>" + sendEmailChannelId + "</ChnlId>" + 
					"<Key>" + sendEmailKey + "</Key>" + 
					"<Row>" + 
					"<RefId>" + refId + "</RefId>" + 
					"<Txncode>" + txncode + "</Txncode>" + 
					"<Emailid>" + email + "</Emailid>" + 
					"<Subject>" + subject + "</Subject>" + 
					"<Msg>" + 
					"<![CDATA[" + content + "']]>" + 
					"</Msg>" + 
					"<Attachment></Attachment>" + 
					"</Row>" + 
					"</Root>";

			System.out.println("Final Response::"+xmlData);
		

			HttpHeaders headers = new HttpHeaders();
			headers.set("X-IBM-Client-Secret", "D8fY4dX8vA8qQ7sJ0aM0bL4yO5aA3sY1qY1mF1yJ6bW5wL7pJ1");
			headers.set("X-IBM-Client-Id", "2b12177f-6663-4155-b47b-859e7e238f3a");
			headers.set("content-type", "application/xml");

			HttpEntity<String> request = new HttpEntity<>(xmlData, headers);
            
            String activityType = environment.getProperty("sendSuccessOTPMail");
			long id = requestResponseLog.logEntryProcess(LocalDateTime.now(), reqId, xmlData, null, baseUrl, LocalDateTime.now(), serviceType, activityType);
			
			response = restTemplate.postForEntity(uri, request, String.class);

			
			System.out.println("Response data::" + response);					
			
			if(response.getStatusCode() == HttpStatus.OK) {
				responseData = response.getBody();
							requestResponseLog.logEntryUpdate(response.getBody(), LocalDateTime.now(), serviceType, id);

			}

			} catch (URISyntaxException e) {
				e.printStackTrace();
			} 
		return new ResponseEntity<>(responseData, HttpStatus.OK);

	}
}
