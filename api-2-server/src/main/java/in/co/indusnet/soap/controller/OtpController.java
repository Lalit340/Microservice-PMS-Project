package in.co.indusnet.soap.controller;

import java.time.LocalDateTime;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.co.indusnet.soap.service.OtpClient;
import in.co.indusnet.soap.utility.AllTokenCreateAndValidate;
import in.co.indusnet.soap.utility.Converter;

@CrossOrigin(origins = "*", allowedHeaders="*", maxAge = 3600)
@RestController
@RequestMapping("/otp")
@Configuration
@EnableAutoConfiguration
@PropertySource("classpath:constant/constantEnv.properties")
@PropertySource("classpath:constant/constant.properties")
public class OtpController {
	
	@Autowired
	private OtpClient otpClient;
	
	@Autowired
	private Environment environment;

	@Autowired
	private AllTokenCreateAndValidate allTokenCreateAndValidate;
	
	@Value("${spring.profiles.active}")
	private String activeEnvironment;
	
	@PostMapping("/sendotp")
	public ResponseEntity<String> sendOtp(
		@RequestParam String mobileNo,
		@RequestHeader String authToken,
		@RequestHeader String serviceType) {
		long reqId = allTokenCreateAndValidate.validateToken(authToken,serviceType);
		/*IF the token is not valid*/
		if(reqId == 0) {
			JSONObject errorObj = new JSONObject();
			errorObj.put("error", "unauthorized");
			return new ResponseEntity<>(errorObj.toString(), HttpStatus.UNAUTHORIZED);
		} else {
			String sendOtpChannelId = environment.getProperty("sendOtpChannelId");
			String endPointUrl = environment.getProperty("sendOtpEndPointUrl");
			
			String response = otpClient.sendOtp(mobileNo, sendOtpChannelId, endPointUrl);
			otpClient.sendOtpLogEntryProcess(LocalDateTime.now(), reqId, endPointUrl, serviceType, environment.getProperty("sendOtp"));
			if(!response.isEmpty())
			{				
				String jsonResult = Converter.xmlToJson(response);
				return new ResponseEntity<>(jsonResult, HttpStatus.OK);
			}
			else
			{
				return new ResponseEntity<>("", HttpStatus.GATEWAY_TIMEOUT);
			}
			
		}
	}
	
	@PostMapping("/validateotp")
	public ResponseEntity<String> validateOtp(
		@RequestParam String mobileNo,
		@RequestParam String referenceId,
		@RequestParam String otp,
		@RequestHeader String authToken,
		@RequestHeader String serviceType) {
		long reqId = allTokenCreateAndValidate.validateToken(authToken,serviceType);
		/*IF the token is not valid*/
		if(reqId == 0) {
			JSONObject errorObj = new JSONObject();
			errorObj.put("error", "unauthorized");
			return new ResponseEntity<>(errorObj.toString(), HttpStatus.UNAUTHORIZED);
		} else {
			String sendOtpChannelId = environment.getProperty("sendOtpChannelId");
			String endPointUrl = environment.getProperty("sendOtpEndPointUrl");
			
			String response = otpClient.validateOtp(mobileNo, referenceId, otp, endPointUrl);
			otpClient.validateOtpLogEntryProcess(LocalDateTime.now(), reqId, endPointUrl, serviceType, environment.getProperty("validateOtp"));
			if(!response.isEmpty()) 
			{
				
				String jsonResult = Converter.xmlToJson(response);
				return new ResponseEntity<>(jsonResult, HttpStatus.OK);
			}
			else
			{
				return new ResponseEntity<>("", HttpStatus.GATEWAY_TIMEOUT);
			}
			
		}
	}
}
