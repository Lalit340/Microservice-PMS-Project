package in.co.indusnet.soap.controller;

import java.time.LocalDateTime;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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

import in.co.indusnet.soap.service.IndusWebClient;
import in.co.indusnet.soap.utility.AllTokenCreateAndValidate;
import in.co.indusnet.soap.utility.Converter;
import in.co.indusnet.soap.utility.Generator;
import soap.indusweb.wsdl.ProcessIndusWebServiceReqResponse;

@CrossOrigin(origins = "*", allowedHeaders="*", maxAge = 3600)
@RestController
@RequestMapping("/indusweb")
@Configuration
@PropertySource("classpath:constant/constantEnv.properties")
@PropertySource("classpath:constant/constant.properties")
public class IndusWebController {

	@Autowired
	private IndusWebClient indusWebClient;
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private AllTokenCreateAndValidate allTokenCreateAndValidate;
	
	@PostMapping("/getlinkedcif")
	public ResponseEntity<String> getLinkedCif(
			@RequestParam String cifId,
			@RequestHeader String authToken,
			@RequestHeader String serviceType) {
		long reqId = allTokenCreateAndValidate.validateToken(authToken,serviceType);
		/*IF the token is not valid*/
		if(reqId == 0) {
			JSONObject errorObj = new JSONObject();
			errorObj.put("error", "unauthorized");
			return new ResponseEntity<>(errorObj.toString(), HttpStatus.UNAUTHORIZED);
		}
		else {
			String linkedCifServiceRequestId = environment.getProperty("linkedCifServiceRequestId");
			String linkedCifServiceRequestVersion = environment.getProperty("linkedCifServiceRequestVersion");
			String linkedCifChannelId = environment.getProperty("linkedCifChannelId");
			String endPointUrl = environment.getProperty("linkedCifEndPointUrl");
			
			String requestUuid = "Req_" + Generator.randomNumberGenerator(13);
			LocalDateTime requestDateTime = LocalDateTime.now();
			
			String data = "<NBXML>\n" + 
					" <Header> \n" + 
					" <RequestHeader>\n" + 
					" <MessageKey>\n" + 
					" <RequestUUID>Mob_"+requestUuid+"</RequestUUID>\n" + 
					" <ServiceRequestId>"+linkedCifServiceRequestId+"</ServiceRequestId>\n" + 
					" <ServiceRequestVersion>"+linkedCifServiceRequestVersion+"</ServiceRequestVersion>\n" + 
					" <ChannelId>"+linkedCifChannelId+"</ChannelId>\n" + 
					" <LanguageId></LanguageId>\n" + 
					" </MessageKey>\n" + 
					" <RequestMessageInfo>\n" + 
					" <BankId></BankId>\n" + 
					" <TimeZone></TimeZone>\n" + 
					" <EntityId></EntityId>\n" + 
					" <EntityType></EntityType>\n" + 
					" <ArmCorrelationId></ArmCorrelationId>\n" + 
					" <MessageDateTime>" + requestDateTime + "</MessageDateTime>\n" + 
					" </RequestMessageInfo>\n" + 
					" <Security>\n" + 
					" <Token>\n" + 
					" <PasswordToken>\n" + 
					" <UserId></UserId>\n" + 
					" <Password></Password>\n" + 
					" </PasswordToken>\n" + 
					" </Token>\n" + 
					" <SICertToken></SICertToken>\n" + 
					" <RealUserLoginSessionId></RealUserLoginSessionId>\n" + 
					" <RealUser></RealUser>\n" + 
					" <RealUserPwd></RealUserPwd>\n" + 
					" <SSOTransferToken></SSOTransferToken>\n" + 
					" </Security>\n" + 
					" </RequestHeader>\n" + 
					" </Header>\n" + 
					"\n" + 
					" <Body>\n" + 
					" <InetFetchLinkedCustIdRequest>\n" + 
					" <CIFId>"+cifId+"</CIFId>\n" + 
					" </InetFetchLinkedCustIdRequest>\n" + 
					" </Body>\n" + 
					" </NBXML>";
			
			
			ProcessIndusWebServiceReqResponse response = indusWebClient.executeSoap(data,endPointUrl);
			if(!response.toString().isEmpty()) 
			{
				String jsonResult = Converter.xmlToJson(response.getReturn());
				indusWebClient.logEntryProcess(data,requestDateTime,reqId,endPointUrl,serviceType,environment.getProperty("getLinkedCif"));
				return new ResponseEntity<>(jsonResult, HttpStatus.OK);
			}
			else
			{
				return new ResponseEntity<>("", HttpStatus.GATEWAY_TIMEOUT);
			}
		}
	}
}
