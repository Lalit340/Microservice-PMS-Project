package in.co.indusnet.soap.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.co.indusnet.soap.response.XmlResponse;
import in.co.indusnet.soap.utility.AllTokenCreateAndValidate;
import in.co.indusnet.soap.utility.MessageAndMailSender;

@RestController
public class SmsEmailController {

	@Autowired
	private AllTokenCreateAndValidate allTokenCreateAndValidate;


	@Autowired
	private MessageAndMailSender messageAndMailSender;

	@PostMapping("/sendSMS")
	public  ResponseEntity<String> sendSms(@RequestParam String mobNo,
			@RequestParam String msg,
			@RequestHeader String authToken,
			@RequestHeader String serviceType) {
		
        long reqId = allTokenCreateAndValidate.validateToken(authToken,serviceType);
		
		if(reqId == 0) {
			JSONObject errorObj = new JSONObject();
			errorObj.put("error", "unauthorized");
			return new ResponseEntity<>(errorObj.toString(), HttpStatus.UNAUTHORIZED);
		}else {
		return messageAndMailSender.messageSender(mobNo, msg ,reqId , serviceType);
		}
	}

	@PostMapping("/sendEmail")
	public ResponseEntity<String> sendEmail(@RequestParam String email,
			@RequestParam String subject, @RequestParam String content, @RequestHeader String authToken,
			@RequestHeader String serviceType) {

		long reqId = allTokenCreateAndValidate.validateToken(authToken, serviceType);

		if (reqId == 0) {
			JSONObject errorObj = new JSONObject();
			errorObj.put("error", "unauthorized");
			return new ResponseEntity<>(errorObj.toString(), HttpStatus.UNAUTHORIZED);
		} else {
			return messageAndMailSender.mailSender(email, subject, content, reqId, serviceType);
		}
	}
	@PostMapping("/sendOtpEmail")
	public ResponseEntity<String> sendOtpEmail(@RequestParam String email,
			@RequestParam String subject, @RequestParam String content, @RequestHeader String authToken,
			@RequestHeader String serviceType) {

		long reqId = allTokenCreateAndValidate.validateToken(authToken, serviceType);

		if (reqId == 0) {
			JSONObject errorObj = new JSONObject();
			errorObj.put("error", "unauthorized");
			return new ResponseEntity<>(errorObj.toString(), HttpStatus.UNAUTHORIZED);
		} else {
			return messageAndMailSender.mailOtpSender(email, subject, content, reqId, serviceType);
		}
	}

}
