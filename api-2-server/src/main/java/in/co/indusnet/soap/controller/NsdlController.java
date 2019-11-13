package in.co.indusnet.soap.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.co.indusnet.soap.service.NsdlClient;
import in.co.indusnet.soap.utility.AllTokenCreateAndValidate;
import in.co.indusnet.soap.utility.Converter;
import soap.nsdl.wsdl.Body;
import soap.nsdl.wsdl.DocPANResp;
import soap.nsdl.wsdl.PanDetails;
import soap.nsdl.wsdl.PanResponse;
import soap.nsdl.wsdl.ProcessPANValidationResponse;

@RestController
@Configuration
@PropertySource("classpath:constant/constantEnv.properties")
@PropertySource("classpath:constant/constant.properties")
public class NsdlController {
	
	@Autowired
	private NsdlClient nsdlClient;
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private AllTokenCreateAndValidate allTokenCreateAndValidate;
	
	@PostMapping("panvalidation")
	public ResponseEntity<String> panValidation(
			@RequestParam String panNumber,
			@RequestHeader String authToken,
			@RequestHeader String serviceType
			) {
		long reqId = allTokenCreateAndValidate.validateToken(authToken,serviceType);
		/*IF the token is not valid*/
		if(reqId == 0) {
			JSONObject errorObj = new JSONObject();
			errorObj.put("error", "unauthorized");
			return new ResponseEntity<>(errorObj.toString(), HttpStatus.UNAUTHORIZED);
		} else {
			
			String sourceSys = environment.getProperty("nsdlSourceSys");
			String endPointUrl = environment.getProperty("nsdlEndPointUrl");
			
			LocalDateTime requestDateTime = LocalDateTime.now();
			
			ProcessPANValidationResponse response = nsdlClient.panValidate(sourceSys, panNumber, endPointUrl);
			
			
			
			DocPANResp docPANResp = response.getDocPANResp();
			PanResponse panResponse = docPANResp.getPanResponse();
			Body panBody = panResponse.getBody();
			PanDetails panDetails = panBody.getPanDetails().get(0);
			
			JAXBElement<String> panNumberRes = panDetails.getPanNumber();
			JAXBElement<String> panStatus = panDetails.getPanStatus();
			JAXBElement<String> lastName = panDetails.getLastName();
			JAXBElement<String> firstName = panDetails.getFirstName();
			JAXBElement<String> middleName = panDetails.getMiddleName();
			JAXBElement<String> panTitle = panDetails.getPanTitle();
			JAXBElement<String> lastUpdateDate = panDetails.getLastUpdateDate();
			JAXBElement<String> nameOnCard = panDetails.getNameOnCard();
			
			String panStatusResponse = "{\n" +"\"panNumber\": \""+panNumberRes.getValue()+"\",\n" + 
					"			\"panStatus\": \""+panStatus.getValue()+"\",\n" + 
					"			\"lastName\": \""+lastName.getValue()+"\",\n" + 
					"                        \"firstName\": \""+firstName.getValue()+"\",\n" + 
					"                        \"middleName\": \""+middleName.getValue()+"\",\n" + 
					"                        \"panTitle\": \""+panTitle.getValue()+"\",\n" + 
					"                        \"lastUpdateDate\": \""+lastUpdateDate.getValue()+"\",\n" + 
					"                        \"nameOnCard\": \""+nameOnCard.getValue()+"\"\n" + 
					"                    }";
			

			nsdlClient.logEntryProcess(panStatusResponse, requestDateTime,reqId,endPointUrl,serviceType,environment.getProperty("panValidation"));
			
			return new ResponseEntity<>(panStatusResponse, HttpStatus.OK);
		}
	}

}
