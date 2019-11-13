package in.co.indusnet.rekyc.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;



@Component
@Configuration
@PropertySource("classpath:constant/constantEnv.properties")
@PropertySource("classpath:constant/constant.properties")
public class SendMessageEmail {
	
	@Autowired
	private Environment environment;

	public String sendSMS(String token, String mobileNo, String msg, String serviceType) {
		String uri = environment.getProperty("soapServiceUrl")+"sendSMS";
		// create a list the headers 
		HttpHeaders headers = new HttpHeaders();
		headers.set("authToken", token);
		headers.set("serviceType", serviceType);
		
		MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
		map.add("mobNo", mobileNo);
		map.add("msg", msg);
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

	    // initialize RestTemplate
	    RestTemplate restTemplate = new RestTemplate();
	    // post the request. The response should be JSON string
	    ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);
	    System.out.println("email eror=============================>>>>"+response);
	    int responseCode = response.getStatusCodeValue();
	    if(responseCode==200) {
		    String jsonResult = response.getBody();
		    return jsonResult;
	    }
	    else {
	    	return "ERROR";
	    }
	}
	public String sendEmail(String token, String email, String subject, String content, String serviceType, String emailType) {
		String uri;
		if(emailType.equals(environment.getProperty("emailTypeSuccess"))) {
			uri = environment.getProperty("soapServiceUrl")+"sendEmail";
		}
		else {
			uri = environment.getProperty("soapServiceUrl")+"sendOtpEmail";
		}
		
		// create a list the headers 
		HttpHeaders headers = new HttpHeaders();
		headers.set("authToken", token);
		headers.set("serviceType", serviceType);
		
		MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
		map.add("email", email);
		map.add("subject", subject);
		map.add("content", content);
				
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
	    RestTemplate restTemplate = new RestTemplate();
	    ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);
	    int responseCode = response.getStatusCodeValue();
	    if(responseCode==200) {
		    String jsonResult = response.getBody();
		    return jsonResult;
	    }
	    else {
	    	return "ERROR";
	    }
	}
}
