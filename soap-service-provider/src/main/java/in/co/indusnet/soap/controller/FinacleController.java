package in.co.indusnet.soap.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

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
import com.fasterxml.jackson.databind.JsonNode;
import in.co.indusnet.soap.service.FinacleClient;
import in.co.indusnet.soap.utility.AllTokenCreateAndValidate;
import in.co.indusnet.soap.utility.Converter;
import in.co.indusnet.soap.utility.Generator;

@CrossOrigin(origins = "*", allowedHeaders="*", maxAge = 3600)
@RestController
@RequestMapping("/finacle")
@Configuration
@PropertySource("classpath:constant/constantEnv.properties")
@PropertySource("classpath:constant/constant.properties")
public class FinacleController {

	@Autowired
	private FinacleClient finacleClient;
	
	@Autowired
	private Environment environment;

	@Autowired
	private Converter  converter;
	
	@Autowired
	private AllTokenCreateAndValidate allTokenCreateAndValidate;
	
		
	@PostMapping("/getcifdetails")
	public ResponseEntity<String> getCifDetails(
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
			String getCifDetailsFinacleExecuteServiceRequestId = environment.getProperty("getCifDetailsFinacleExecuteServiceRequestId");
			String getCifDetailsFinacleServiceRequestVersion = environment.getProperty("getCifDetailsFinacleServiceRequestVersion");
			String getCifDetailsFinacleChannelId = environment.getProperty("getCifDetailsFinacleChannelId");
			String getCifDetailsrequestId = environment.getProperty("getCifDetailsrequestId");	
			
			String requestUuid = "Req_" + Generator.randomNumberGenerator(13);
			LocalDateTime requestDateTime = LocalDateTime.now(); 
			
			String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
					"<FIXML xmlns=\"http://www.finacle.com/fixml\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.finacle.com/fixml executeFinacleScript.xsd\">\n" + 
					"   <Header>\n" + 
					"      <RequestHeader>\n" + 
					"         <MessageKey>\n" + 
					"            <RequestUUID>" + requestUuid + "</RequestUUID>\n" + 
					"            <ServiceRequestId>" + getCifDetailsFinacleExecuteServiceRequestId + "</ServiceRequestId>\n" + 
					"            <ServiceRequestVersion>" + getCifDetailsFinacleServiceRequestVersion + "</ServiceRequestVersion>\n" + 
					"            <ChannelId>" + getCifDetailsFinacleChannelId + "</ChannelId>\n" + 
					"            <LanguageId />\n" + 
					"         </MessageKey>\n" + 
					"         <RequestMessageInfo>\n" + 
					"            <BankId />\n" + 
					"            <TimeZone />\n" + 
					"            <EntityId />\n" + 
					"            <EntityType />\n" + 
					"            <ArmCorrelationId />\n" + 
					"            <MessageDateTime>"+ requestDateTime +"</MessageDateTime>\n" + 
					"         </RequestMessageInfo>\n" + 
					"         <Security>\n" + 
					"            <Token>\n" + 
					"               <PasswordToken>\n" + 
					"                  <UserId />\n" + 
					"                  <Password />\n" + 
					"               </PasswordToken>\n" + 
					"            </Token>\n" + 
					"            <FICertToken />\n" + 
					"            <RealUserLoginSessionId />\n" + 
					"            <RealUser />\n" + 
					"            <RealUserPwd />\n" + 
					"            <SSOTransferToken />\n" + 
					"         </Security>\n" + 
					"      </RequestHeader>\n" + 
					"   </Header>\n" + 
					"   <Body>\n" + 
					"      <executeFinacleScriptRequest>\n" + 
					"         <ExecuteFinacleScriptInputVO>\n" + 
					"            <requestId>" + getCifDetailsrequestId + "</requestId>\n" + 
					"         </ExecuteFinacleScriptInputVO>\n" + 
					"         <executeFinacleScript_CustomData>\n" + 
					"            <CIF_ID>" + cifId + "</CIF_ID>\n" + 
					"         </executeFinacleScript_CustomData>\n" + 
					"      </executeFinacleScriptRequest>\n" + 
					"   </Body>\n" + 
					"</FIXML>";
			String endPointUrl = environment.getProperty("finacleEndPointUrl");
			String response = finacleClient.executeSoap(data,endPointUrl);

			String jsonResult = Converter.xmlCollectionToJson(response);
			finacleClient.logEntryProcess(data,requestDateTime,reqId,endPointUrl,serviceType,environment.getProperty("getCifDetails"));
			if(response.isEmpty()) {
				return new ResponseEntity<>("", HttpStatus.GATEWAY_TIMEOUT);
			}
			return new ResponseEntity<>(jsonResult, HttpStatus.OK);
		}
	}
	
	@PostMapping("/getriskratingandaddress")
	public ResponseEntity<String> getriskratingandaddress(
			@RequestParam String cifId,
			@RequestHeader String authToken,
			@RequestHeader String serviceType){

		long reqId = allTokenCreateAndValidate.validateToken(authToken,serviceType);
		/*IF the token is not valid*/
		if(reqId == 0) {
			JSONObject errorObj = new JSONObject();
			errorObj.put("error", "unauthorized");
			return new ResponseEntity<>(errorObj.toString(), HttpStatus.UNAUTHORIZED);
		}
		else {
			String getRiskRatingAndAddressFinacleExecuteServiceRequestId = environment.getProperty("getRiskRatingAndAddressFinacleExecuteServiceRequestId");
			String getRiskRatingAndAddressFinacleFinacleServiceRequestVersion = environment.getProperty("getRiskRatingAndAddressFinacleFinacleServiceRequestVersion");
			String getRiskRatingAndAddressFinacleFinacleChannelId = environment.getProperty("getRiskRatingAndAddressFinacleFinacleChannelId");	
			String riskRatingrequestId = environment.getProperty("riskRatingrequestId");
	
			
			String requestUuid = "Req_" + Generator.randomNumberGenerator(13);
			LocalDateTime requestDateTime = LocalDateTime.now(); 
			String data ="<FIXML xsi:schemaLocation=\"http://www.finacle.com/fixml executeFinacleScript.xsd\"\n" + 
					"    xmlns=\"http://www.finacle.com/fixml\"\n" + 
					"    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" + 
					"    <Header>\n" + 
					"        <RequestHeader>\n" + 
					"            <MessageKey>\n" + 
					"                <RequestUUID>" + requestUuid + "</RequestUUID>\n" + 
					"                <ServiceRequestId>" + getRiskRatingAndAddressFinacleExecuteServiceRequestId + "</ServiceRequestId>\n" + 
					"                <ServiceRequestVersion>" + getRiskRatingAndAddressFinacleFinacleServiceRequestVersion + "</ServiceRequestVersion>\n" + 
					"                <ChannelId>" + getRiskRatingAndAddressFinacleFinacleChannelId + "</ChannelId>\n" + 
					"                <LanguageId></LanguageId>\n" + 
					"            </MessageKey>\n" + 
					"            <RequestMessageInfo>\n" + 
					"                <BankId></BankId>\n" + 
					"                <TimeZone></TimeZone>\n" + 
					"                <EntityId></EntityId>\n" + 
					"                <EntityType></EntityType>\n" + 
					"                <ArmCorrelationId></ArmCorrelationId>\n" + 
					"                <MessageDateTime>" + requestDateTime + "</MessageDateTime>\n" + 
					"            </RequestMessageInfo>\n" + 
					"            <Security>\n" + 
					"                <Token>\n" + 
					"                    <PasswordToken>\n" + 
					"                        <UserId></UserId>\n" + 
					"                        <Password></Password>\n" + 
					"                    </PasswordToken>\n" + 
					"                </Token>\n" + 
					"                <FICertToken></FICertToken>\n" + 
					"                <RealUserLoginSessionId></RealUserLoginSessionId>\n" + 
					"                <RealUser></RealUser>\n" + 
					"                <RealUserPwd></RealUserPwd>\n" + 
					"                <SSOTransferToken></SSOTransferToken>\n" + 
					"            </Security>\n" + 
					"        </RequestHeader>\n" + 
					"    </Header>\n" + 
					"    <Body>\n" + 
					"        <executeFinacleScriptRequest>\n" + 
					"            <ExecuteFinacleScriptInputVO>\n" + 
					"                <requestId>" + riskRatingrequestId + "</requestId>\n" + 
					"            </ExecuteFinacleScriptInputVO>\n" + 
					"            <executeFinacleScript_CustomData>\n" + 
					"                <cif_id>"+cifId+"</cif_id>\n" + 
					"            </executeFinacleScript_CustomData>\n" + 
					"        </executeFinacleScriptRequest>\n" + 
					"    </Body>\n" + 
					"</FIXML>";
			String endPointUrl = environment.getProperty("finacleEndPointUrl");
			String response = finacleClient.executeSoap(data,endPointUrl);
			finacleClient.logEntryProcess(data,requestDateTime,reqId,endPointUrl,serviceType,environment.getProperty("getRiskProfile"));
			if(!response.isEmpty())
			{
				String jsonResult = Converter.xmlCollectionToJson(response);
				System.out.println("return Data ::"+response);
				
				//String value ="Risk_Profile";
			    //JsonNode node = converter.getParamJsonValue(jsonResult, value);
			    return new ResponseEntity<>(jsonResult, HttpStatus.OK);
			}
			else
			{
				return new ResponseEntity<>("", HttpStatus.GATEWAY_TIMEOUT);
			}
		}
	}	
	
	@PostMapping("/activatedormantaccount")
	public ResponseEntity<String> activateDormantAccount(
			@RequestParam String accId,
			@RequestHeader String authToken,
			@RequestHeader String serviceType){
		long reqId = allTokenCreateAndValidate.validateToken(authToken,serviceType);
		/*IF the token is not valid*/
		if(reqId == 0) {
			JSONObject errorObj = new JSONObject();
			errorObj.put("error", "unauthorized");
			return new ResponseEntity<>(errorObj.toString(), HttpStatus.UNAUTHORIZED);
		}
		else {
			String activateDormantAccountFinacleExecuteServiceRequestId = environment.getProperty("activateDormantAccountFinacleExecuteServiceRequestId");
			String activateDormantAccountFinacleFinacleServiceRequestVersion = environment.getProperty("activateDormantAccountFinacleFinacleServiceRequestVersion");
			String activateDormantAccountFinacleFinacleChannelId = environment.getProperty("activateDormantAccountFinacleFinacleChannelId");	
			String activeDormantrequestId = environment.getProperty("activeDormantrequestId");
			
			String requestUuid = "Req_" + Generator.randomNumberGenerator(13);
			LocalDateTime requestDateTime = LocalDateTime.now();
			String data ="<FIXML xsi:schemaLocation=\"http://www.finacle.com/fixml ODAcctMod.xsd\"\n" + 
					"    xmlns=\"http://www.finacle.com/fixml\"\n" + 
					"    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" + 
					"    <Header>\n" + 
					"        <RequestHeader>\n" + 
					"            <MessageKey>\n" + 
					"                <RequestUUID>" + requestUuid + "</RequestUUID>\n" + 
					"                <ServiceRequestId>" + activateDormantAccountFinacleExecuteServiceRequestId + "</ServiceRequestId>\n" + 
					"                <ServiceRequestVersion>" + activateDormantAccountFinacleFinacleServiceRequestVersion + "</ServiceRequestVersion>\n" + 
					"                <ChannelId>" + activateDormantAccountFinacleFinacleChannelId + "</ChannelId>\n" + 
					"                <LanguageId></LanguageId>\n" + 
					"            </MessageKey>\n" + 
					"            <RequestMessageInfo>\n" + 
					"                <BankId></BankId>\n" + 
					"                <TimeZone></TimeZone>\n" + 
					"                <EntityId></EntityId>\n" + 
					"                <EntityType></EntityType>\n" + 
					"                <ArmCorrelationId></ArmCorrelationId>\n" + 
					"                <MessageDateTime>" + requestDateTime +"</MessageDateTime>\n" + 
					"            </RequestMessageInfo>\n" + 
					"            <Security>\n" + 
					"                <Token>\n" + 
					"                    <PasswordToken>\n" + 
					"                        <UserId></UserId>\n" + 
					"                        <Password></Password>\n" + 
					"                    </PasswordToken>\n" + 
					"                </Token>\n" + 
					"                <FICertToken></FICertToken>\n" + 
					"                <RealUserLoginSessionId></RealUserLoginSessionId>\n" + 
					"                <RealUser></RealUser>\n" + 
					"                <RealUserPwd></RealUserPwd>\n" + 
					"                <SSOTransferToken></SSOTransferToken>\n" + 
					"            </Security>\n" + 
					"        </RequestHeader>\n" + 
					"    </Header>\n" + 
					"    <Body>\n" + 
					"        <executeFinacleScriptRequest>\n" + 
					"            <ExecuteFinacleScriptInputVO>\n" + 
					"                <requestId>" + activeDormantrequestId + "</requestId>\n" + 
					"            </ExecuteFinacleScriptInputVO>\n" + 
					"            <executeFinacleScript_CustomData>\n" + 
					"                <acctId>"+accId+"</acctId>\n" + 
					"            </executeFinacleScript_CustomData>\n" + 
					"        </executeFinacleScriptRequest>\n" + 
					"    </Body>\n" + 
					"</FIXML>";
			
			String endPointUrl = environment.getProperty("finacleEndPointUrl");
			String response = finacleClient.executeSoap(data,endPointUrl);
			System.out.println(response+"*************************");
			finacleClient.logEntryProcess(data,requestDateTime,reqId,endPointUrl,serviceType,environment.getProperty("activateDormantAccount"));
			if(!response.isEmpty())
			{
				String jsonResult = Converter.xmlCollectionToJson(response);
				System.out.println("return Data ::"+jsonResult);
				
				return new ResponseEntity<>(jsonResult, HttpStatus.OK);
			}
			else
			{
				return new ResponseEntity<>("", HttpStatus.GATEWAY_TIMEOUT);
			}
		}
	}
	
	@PostMapping("/isnre")
	public ResponseEntity<String> getIsNre(
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
			String isNreServiceRequestId = environment.getProperty("isNreServiceRequestId");
			String isNreFinacleFinacleServiceRequestVersion = environment.getProperty("isNreFinacleFinacleServiceRequestVersion");
			String isNreChannelId = environment.getProperty("isNreChannelId");	
			
			String requestUuid = "Req_" + Generator.randomNumberGenerator(13);
			LocalDateTime requestDateTime = LocalDateTime.now();
			
			String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
					"<FIXML xmlns=\"http://www.finacle.com/fixml\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.finacle.com/fixml RetCustInq.xsd\">\n" + 
					"   <Header>\n" + 
					"      <RequestHeader>\n" + 
					"         <MessageKey>\n" + 
					"            <RequestUUID>" + requestUuid + "</RequestUUID>\n" + 
					"            <ServiceRequestId>" + isNreServiceRequestId + "</ServiceRequestId>\n" + 
					"            <ServiceRequestVersion>" + isNreFinacleFinacleServiceRequestVersion + "</ServiceRequestVersion>\n" + 
					"            <ChannelId>" + isNreChannelId + "</ChannelId>\n" + 
					"            <LanguageId />\n" + 
					"         </MessageKey>\n" + 
					"         <RequestMessageInfo>\n" + 
					"            <BankId />\n" + 
					"            <TimeZone />\n" + 
					"            <EntityId />\n" + 
					"            <EntityType />\n" + 
					"            <ArmCorrelationId />\n" + 
					"            <MessageDateTime>" + requestDateTime + "</MessageDateTime>\n" + 
					"         </RequestMessageInfo>\n" + 
					"         <Security>\n" + 
					"            <Token>\n" + 
					"               <PasswordToken>\n" + 
					"                  <UserId />\n" + 
					"                  <Password />\n" + 
					"               </PasswordToken>\n" + 
					"            </Token>\n" + 
					"            <FICertToken />\n" + 
					"            <RealUserLoginSessionId />\n" + 
					"            <RealUser />\n" + 
					"            <RealUserPwd />\n" + 
					"            <SSOTransferToken />\n" + 
					"         </Security>\n" + 
					"      </RequestHeader>\n" + 
					"   </Header>\n" + 
					"   <Body>\n" + 
					"      <RetCustInqRequest>\n" + 
					"         <RetCustInqRq>\n" + 
					"            <CustId>"+ cifId +"</CustId>\n" + 
					"         </RetCustInqRq>\n" + 
					"      </RetCustInqRequest>\n" + 
					"   </Body>\n" + 
					"</FIXML>";
			String endPointUrl = environment.getProperty("finacleEndPointUrl");
			String response = finacleClient.executeSoap(data,endPointUrl);
			finacleClient.logEntryProcess(data,requestDateTime,reqId,endPointUrl,serviceType,environment.getProperty("checkIsNre"));
			if(!response.isEmpty())
			{
				String jsonResult = Converter.xmlCollectionToJson(response);
			
				String value ="IsNRE";
			    JsonNode node = converter.getParamJsonValue(jsonResult, value);
			    return new ResponseEntity<>(node.textValue(), HttpStatus.OK);
			}
			else
			{
				return new ResponseEntity<>("", HttpStatus.GATEWAY_TIMEOUT);
			}
		}
	}	

	@PostMapping("/rekycsubmission")
	public ResponseEntity<String> getReKycSubmission(
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
			String getReKycSubmissionFinacleExecuteServiceRequestId = environment.getProperty("getReKycSubmissionFinacleExecuteServiceRequestId");
			String getReKycSubmissionFinacleServiceRequestVersion = environment.getProperty("getReKycSubmissionFinacleServiceRequestVersion");
			String getReKycSubmissionFinacleChannelId = environment.getProperty("getReKycSubmissionFinacleChannelId");
			String getReKycSubmissionrequestId = environment.getProperty("getReKycSubmissionrequestId");	
			String getReKycSubmissionBankId = environment.getProperty("getReKycSubmissionBankId");
			String getReKycSubmissionkycFunction = environment.getProperty("getReKycSubmissionkycFunction");
			
			String requestUuid = "Req_" + Generator.randomNumberGenerator(13);
			LocalDateTime requestDateTime = LocalDateTime.now();
			
			String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
					"<FIXML xmlns=\"http://www.finacle.com/fixml\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.finacle.com/fixml executeFinacleScript.xsd\">\n" + 
					"   <Header>\n" + 
					"      <RequestHeader>\n" + 
					"         <MessageKey>\n" + 
					"            <RequestUUID>" + requestUuid + "</RequestUUID>\n" + 
					"            <ServiceRequestId>" + getReKycSubmissionFinacleExecuteServiceRequestId + "</ServiceRequestId>\n" + 
					"            <ServiceRequestVersion>" + getReKycSubmissionFinacleServiceRequestVersion + "</ServiceRequestVersion>\n" + 
					"            <ChannelId>" + getReKycSubmissionFinacleChannelId + "</ChannelId>\n" + 
					"            <LanguageId />\n" + 
					"         </MessageKey>\n" + 
					"         <RequestMessageInfo>\n" + 
					"            <BankId>"+ getReKycSubmissionBankId +"</BankId>\n" + 
					"            <TimeZone />\n" + 
					"            <EntityId />\n" + 
					"            <EntityType />\n" + 
					"            <ArmCorrelationId />\n" + 
					"            <MessageDateTime>" + requestDateTime + "</MessageDateTime>\n" + 
					"         </RequestMessageInfo>\n" + 
					"         <Security>\n" + 
					"            <Token>\n" + 
					"               <PasswordToken>\n" + 
					"                  <UserId />\n" + 
					"                  <Password />\n" + 
					"               </PasswordToken>\n" + 
					"            </Token>\n" + 
					"            <FICertToken />\n" + 
					"            <RealUserLoginSessionId />\n" + 
					"            <RealUser />\n" + 
					"            <RealUserPwd />\n" + 
					"            <SSOTransferToken />\n" + 
					"         </Security>\n" + 
					"      </RequestHeader>\n" + 
					"   </Header>\n" + 
					"   <Body>\n" + 
					"      <executeFinacleScriptRequest>\n" + 
					"         <ExecuteFinacleScriptInputVO>\n" + 
					"            <requestId>"+ getReKycSubmissionrequestId +"</requestId>\n" + 
					"         </ExecuteFinacleScriptInputVO>\n" + 
					"         <executeFinacleScript_CustomData>\n" + 
					"            <cifId>" + cifId + "</cifId>\n" + 
					"            <kycFunction>" + getReKycSubmissionkycFunction + "</kycFunction>\n" + 
					"         </executeFinacleScript_CustomData>\n" + 
					"      </executeFinacleScriptRequest>\n" + 
					"   </Body>\n" + 
					"</FIXML>";
			String endPointUrl = environment.getProperty("finacleEndPointUrl");
			String response = finacleClient.executeSoap(data,endPointUrl);
			finacleClient.logEntryProcess(data,requestDateTime,reqId,endPointUrl,serviceType,environment.getProperty("rekycSubmission"));
			if(!response.isEmpty())
			{
				String jsonResult = Converter.xmlCollectionToJson(response);
				
				return new ResponseEntity<>(jsonResult, HttpStatus.OK);
			}
			else
			{
				return new ResponseEntity<>("", HttpStatus.GATEWAY_TIMEOUT);
			}
		}
	}

	@PostMapping("/getcifinquiry")
	public ResponseEntity<String> getInquiryDetails(
			@RequestParam String cifId,
			@RequestHeader String authToken,
			@RequestHeader String serviceType){
		
		long reqId = allTokenCreateAndValidate.validateToken(authToken,serviceType);
		/*IF the token is not valid*/
		if(reqId == 0) 
		{
			JSONObject errorObj = new JSONObject();
			errorObj.put("error", "unauthorized");
			return new ResponseEntity<>(errorObj.toString(), HttpStatus.UNAUTHORIZED);
		}
		else 
		{
			String getInquiryDetailsFinacleExecuteServiceRequestId = environment.getProperty("getInquiryDetailsFinacleExecuteServiceRequestId");
			String getInquiryDetailsFinacleFinacleServiceRequestVersion = environment.getProperty("getInquiryDetailsFinacleFinacleServiceRequestVersion");
			String getInquiryDetailsFinacleFinacleChannelId = environment.getProperty("getInquiryDetailsFinacleFinacleChannelId");
			String getInquiryDetailsrequestId = environment.getProperty("getInquiryDetailsrequestId");	
			String getInquiryDetailsBankId = environment.getProperty("getInquiryDetailsBankId");
			
			String requestUuid = "Req_" + Generator.randomNumberGenerator(13);
			LocalDateTime requestDateTime = LocalDateTime.now();
			
			String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
					" <FIXML xsi:schemaLocation=\"http://www.finacle.com/fixml executeFinacleScript.xsd\" xmlns=\"http://www.finacle.com/fixml\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" + 
					"	 <Header>\n" + 
					"		 <RequestHeader>\n" + 
					"			 <MessageKey>\n" + 
					"				<RequestUUID>" + requestUuid + "</RequestUUID>\n" + 
					"				<ServiceRequestId>" + getInquiryDetailsFinacleExecuteServiceRequestId + "</ServiceRequestId>\n" + 
					"				<ServiceRequestVersion>" + getInquiryDetailsFinacleFinacleServiceRequestVersion + "</ServiceRequestVersion>\n" + 
					"				<ChannelId>" + getInquiryDetailsFinacleFinacleChannelId + "</ChannelId>\n" + 
					"				<LanguageId></LanguageId>\n" + 
					"			 </MessageKey>\n" + 
					"			 <RequestMessageInfo>\n" + 
					"				 <BankId>" + getInquiryDetailsBankId + "</BankId>\n" + 
					"				 <TimeZone></TimeZone>\n" + 
					"				 <EntityId></EntityId>\n" + 
					"				 <EntityType></EntityType>\n" + 
					"				 <ArmCorrelationId></ArmCorrelationId>\n" + 
					"				 <MessageDateTime>" + requestDateTime + "</MessageDateTime>\n" + 
					"			 </RequestMessageInfo>\n" + 
					"			 <Security>\n" + 
					"				 <Token>\n" + 
					"					 <PasswordToken>\n" + 
					"						 <UserId></UserId>\n" + 
					"						 <Password></Password>\n" + 
					"					 </PasswordToken>\n" + 
					"				 </Token>\n" + 
					"				 <FICertToken></FICertToken>\n" + 
					"				 <RealUserLoginSessionId></RealUserLoginSessionId>\n" + 
					"				 <RealUser></RealUser>\n" + 
					"				 <RealUserPwd></RealUserPwd>\n" + 
					"				 <SSOTransferToken></SSOTransferToken>\n" + 
					"			 </Security>\n" + 
					"		 </RequestHeader>\n" + 
					"	 </Header>\n" + 
					"	 <Body>\n" + 
					"		 <executeFinacleScriptRequest>\n" + 
					"			 <ExecuteFinacleScriptInputVO>\n" + 
					"				 <requestId>" + getInquiryDetailsrequestId + "</requestId>\n" + 
					"			 </ExecuteFinacleScriptInputVO>\n" + 
					"			 <executeFinacleScript_CustomData>\n" + 
					"				 <cifId>" + cifId + "</cifId>\n" + 
					"			 </executeFinacleScript_CustomData>\n" + 
					"		 </executeFinacleScriptRequest>\n" + 
					"	 </Body>\n" + 
					" </FIXML>";
			
			String endPointUrl = environment.getProperty("finacleEndPointUrl");
			String response = finacleClient.executeSoap(data,endPointUrl);
			finacleClient.logEntryProcess(data,requestDateTime,reqId,endPointUrl,serviceType,environment.getProperty("inqueryDetails"));
			if(!response.isEmpty())
			{
				String jsonResult = Converter.xmlCollectionToJson(response);
				
				return new ResponseEntity<>(jsonResult, HttpStatus.OK);
			}
			else
			{
				return new ResponseEntity<>("", HttpStatus.GATEWAY_TIMEOUT);
			}
		}
	}
	
	@PostMapping("/inqueryofaccount")
	public ResponseEntity<String> getInqueryOfAccount(@RequestParam String accNo,
			@RequestHeader String authToken,
			@RequestHeader String serviceType){
		long reqId = allTokenCreateAndValidate.validateToken(authToken,serviceType);
		/*IF the token is not valid*/
		if(reqId == 0) 
		{
			JSONObject errorObj = new JSONObject();
			errorObj.put("error", "unauthorized");
			return new ResponseEntity<>(errorObj.toString(), HttpStatus.UNAUTHORIZED);
		}
		else 
		{
			String getInqueryOfAccountFinacleExecuteServiceRequestId = environment.getProperty("getInqueryOfAccountFinacleExecuteServiceRequestId");
			String getInqueryOfAccountFinacleServiceRequestVersion = environment.getProperty("getInqueryOfAccountFinacleServiceRequestVersion");
			String getInqueryOfAccountFinacleFinacleChannelId = environment.getProperty("getInqueryOfAccountFinacleFinacleChannelId");
			
			String requestUuid = "Req_" + Generator.randomNumberGenerator(13);
			LocalDateTime requestDateTime = LocalDateTime.now();
			
			String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
					"<FIXML xsi:schemaLocation=\"http://www.finacle.com/fixml TDAcctInq.xsd\" xmlns=\"http://www.finacle.com/fixml\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" + 
					"    <Header>\n" + 
					"        <RequestHeader>\n" + 
					"            <MessageKey>\n" + 
					"                <RequestUUID>" + requestUuid + "</RequestUUID>\n" + 
					"                <ServiceRequestId>" + getInqueryOfAccountFinacleExecuteServiceRequestId + "</ServiceRequestId>\n" + 
					"                <ServiceRequestVersion>" + getInqueryOfAccountFinacleServiceRequestVersion + "</ServiceRequestVersion>\n" + 
					"                <ChannelId>" + getInqueryOfAccountFinacleFinacleChannelId + "</ChannelId>\n" + 
					"                <LanguageId></LanguageId>\n" + 
					"            </MessageKey>\n" + 
					"            <RequestMessageInfo>\n" + 
					"                <BankId></BankId>\n" + 
					"                <TimeZone></TimeZone>\n" + 
					"                <EntityId></EntityId>\n" + 
					"                <EntityType></EntityType>\n" + 
					"                <ArmCorrelationId></ArmCorrelationId>\n" + 
					"                <MessageDateTime>" + requestDateTime + "</MessageDateTime>\n" + 
					"            </RequestMessageInfo>\n" + 
					"            <Security>\n" + 
					"                <Token>\n" + 
					"                    <PasswordToken>\n" + 
					"                        <UserId></UserId>\n" + 
					"                        <Password></Password>\n" + 
					"                    </PasswordToken>\n" + 
					"                </Token>\n" + 
					"                <FICertToken></FICertToken>\n" + 
					"                <RealUserLoginSessionId></RealUserLoginSessionId>\n" + 
					"                <RealUser></RealUser>\n" + 
					"                <RealUserPwd></RealUserPwd>\n" + 
					"                <SSOTransferToken></SSOTransferToken>\n" + 
					"            </Security>\n" + 
					"        </RequestHeader>\n" + 
					"    </Header>\n" + 
					"    <Body>\n" + 
					"        <TDAcctInqRequest>\n" + 
					"            <TDAcctInqRq>\n" + 
					"                <TDAcctId>\n" + 
					"                    <AcctId>" + accNo + "</AcctId>\n" + 
					"                </TDAcctId>\n" + 
					"            </TDAcctInqRq>\n" + 
					"        </TDAcctInqRequest>\n" + 
					"    </Body>\n" + 
					"</FIXML>";
			
			String endPointUrl = environment.getProperty("finacleEndPointUrl");
			String response =finacleClient.executeSoap(data,endPointUrl);
			finacleClient.logEntryProcess(data,requestDateTime,reqId,endPointUrl,serviceType,environment.getProperty("inqueryAccountDetails"));
			if(!response.isEmpty())
			{
				String jsonResult = Converter.xmlCollectionToJson(response);
			
				return new ResponseEntity<>(jsonResult, HttpStatus.OK);
			}
			else
			{
				return new ResponseEntity<>("", HttpStatus.GATEWAY_TIMEOUT);
			}
		}
	}
	
	@PostMapping("/depositaccount")
	public ResponseEntity<String> getDepositAccountDetails(@RequestParam String accNo,
			@RequestHeader String authToken,
			@RequestHeader String serviceType){
		
		long reqId = allTokenCreateAndValidate.validateToken(authToken,serviceType);
		/*IF the token is not valid*/
		if(reqId == 0) 
		{
			JSONObject errorObj = new JSONObject();
			errorObj.put("error", "unauthorized");
			return new ResponseEntity<>(errorObj.toString(), HttpStatus.UNAUTHORIZED);
		}
		else 
		{
			String getDepositAccountDetailsFinacleExecuteServiceRequestId = environment.getProperty("getDepositAccountDetailsFinacleExecuteServiceRequestId");
			String getDepositAccountDetailsFinacleServiceRequestVersion = environment.getProperty("getDepositAccountDetailsFinacleServiceRequestVersion");
			String getDepositAccountDetailsFinacleFinacleChannelId = environment.getProperty("getDepositAccountDetailsFinacleFinacleChannelId");
			
			String requestUuid = "Req_" + Generator.randomNumberGenerator(13);
			LocalDateTime requestDateTime = LocalDateTime.now();
			
			String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
					"		<FIXML xsi:schemaLocation=\"http://www.finacle.com/fixml getDepositAccountDetails.xsd\" xmlns=\"http://www.finacle.com/fixml\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" + 
					"			<Header>\n" + 
					"				<RequestHeader>\n" + 
					"					<MessageKey>\n" + 
					"						<RequestUUID>" + requestUuid + "</RequestUUID>\n" + 
					"						<ServiceRequestId>" + getDepositAccountDetailsFinacleExecuteServiceRequestId + "</ServiceRequestId>\n" + 
					"						<ServiceRequestVersion>" + getDepositAccountDetailsFinacleServiceRequestVersion + "</ServiceRequestVersion>\n" + 
					"						<ChannelId>" + getDepositAccountDetailsFinacleFinacleChannelId + "</ChannelId>\n" + 
					"						<LanguageId></LanguageId>\n" + 
					"					</MessageKey>\n" + 
					"					<RequestMessageInfo>\n" + 
					"						<BankId></BankId>\n" + 
					"						<TimeZone></TimeZone>\n" + 
					"						<EntityId></EntityId>\n" + 
					"						<EntityType></EntityType>\n" + 
					"						<ArmCorrelationId></ArmCorrelationId>\n" + 
					"						<MessageDateTime>" + requestDateTime + "</MessageDateTime>\n" + 
					"					</RequestMessageInfo>\n" + 
					"					<Security>\n" + 
					"						<Token>\n" + 
					"							<PasswordToken>\n" + 
					"								<UserId></UserId>\n" + 
					"								<Password></Password>\n" + 
					"							</PasswordToken>\n" + 
					"						</Token>\n" + 
					"						<FICertToken></FICertToken>\n" + 
					"						<RealUserLoginSessionId></RealUserLoginSessionId>\n" + 
					"						<RealUser></RealUser>\n" + 
					"						<RealUserPwd></RealUserPwd>\n" + 
					"						<SSOTransferToken></SSOTransferToken>\n" + 
					"					</Security>\n" + 
					"				</RequestHeader>\n" + 
					"			</Header>\n" + 
					"			<Body>\n" + 
					"				<getDepositAccountDetailsRequest>\n" + 
					"					<AccountListElement>\n" + 
					"						<acid>" + accNo + "</acid>\n" + 
					"						<branchId>Dummy</branchId>\n" + 
					"					</AccountListElement>\n" + 
					"				</getDepositAccountDetailsRequest>\n" + 
					"			</Body>\n" + 
					"		</FIXML>";
			
			String endPointUrl = environment.getProperty("finacleEndPointUrl");
			String response = finacleClient.executeSoap(data,endPointUrl);
			if(!response.isEmpty())
			{
				String jsonResult = Converter.xmlCollectionToJson(response);
				finacleClient.logEntryProcess(data,requestDateTime,reqId,endPointUrl,serviceType,environment.getProperty("depositAccount"));
				return new ResponseEntity<>(jsonResult, HttpStatus.OK);
			}
			else
			{
				return new ResponseEntity<>("", HttpStatus.GATEWAY_TIMEOUT);
			}
		}
	}
	
	@PostMapping("/trialprematureclosure")
	public ResponseEntity<String> trialPrematureClosure(@RequestParam String accNo, @RequestHeader String authToken,
			@RequestHeader String serviceType) {
		long reqId = allTokenCreateAndValidate.validateToken(authToken, serviceType);
		if (reqId == 0) {
			JSONObject errorObj = new JSONObject();
			errorObj.put("error", "unauthorized");
			return new ResponseEntity<>(errorObj.toString(), HttpStatus.UNAUTHORIZED);
		}
		String requestUuid = "Req_" + Generator.randomNumberGenerator(13);
		String finacleExecuteServiceRequestId = environment
				.getProperty("getPrematureClosureFinacleExecutrServiceRequestId");
		String getPrematureColuseFinacleFinacleServiceRequestVersion = environment
				.getProperty("getPrematureClosureFinacleFinacleServiceRequestVersion");
		String getPrematureColuseFinacleFinacleChannelId = environment
				.getProperty("getPrematureClosureFinacleFinacleChannelId");
		LocalDateTime requestDateTime = LocalDateTime.now();
		String getPrematureClosureInquiryDetailsrequestId = environment
				.getProperty("getPrematureClosureInquiryDetailsrequestId");
		String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"				<FIXML xsi:schemaLocation=\"http://www.finacle.com/fixml executeFinacleScript.xsd\" xmlns=\"http://www.finacle.com/fixml\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><Header>\n" + 
				"					<RequestHeader>\n" + 
				"						<MessageKey>\n" + 
				"							<RequestUUID>"+ requestUuid +"</RequestUUID>\n" + 
				"							<ServiceRequestId>"+ finacleExecuteServiceRequestId +"</ServiceRequestId>\n" + 
				"							<ServiceRequestVersion>"+ getPrematureColuseFinacleFinacleServiceRequestVersion +"</ServiceRequestVersion>\n" + 
				"							<ChannelId>"+getPrematureColuseFinacleFinacleChannelId+"</ChannelId>\n" + 
				"							<LanguageId></LanguageId>\n" + 
				"						</MessageKey>\n" + 
				"					<RequestMessageInfo>\n" + 
				"						<BankId></BankId>\n" + 
				"						<TimeZone></TimeZone>\n" + 
				"						<EntityId></EntityId>\n" + 
				"						<EntityType></EntityType>\n" + 
				"						<ArmCorrelationId></ArmCorrelationId>\n" + 
				"						<MessageDateTime>"+ requestDateTime +"</MessageDateTime>\n" + 
				"					</RequestMessageInfo>\n" + 
				"					<Security>\n" + 
				"						<Token>\n" + 
				"							<PasswordToken>\n" + 
				"							<UserId></UserId>\n" + 
				"							<Password></Password>\n" + 
				"							</PasswordToken>\n" + 
				"						</Token>\n" + 
				"						<FICertToken></FICertToken>\n" + 
				"						<RealUserLoginSessionId></RealUserLoginSessionId>\n" + 
				"						<RealUser></RealUser>\n" + 
				"						<RealUserPwd></RealUserPwd>\n" + 
				"						<SSOTransferToken></SSOTransferToken>\n" + 
				"					</Security>\n" + 
				"				</RequestHeader>\n" + 
				"			</Header>\n" + 
				"		<Body>\n" + 
				"			<executeFinacleScriptRequest>\n" + 
				"				<ExecuteFinacleScriptInputVO>\n" + 
				"					<requestId>"+ getPrematureClosureInquiryDetailsrequestId +"</requestId>\n" + 
				"				</ExecuteFinacleScriptInputVO>\n" + 
				"				<executeFinacleScript_CustomData>\n" + 
				"					<AccountNumber>" +accNo +"</AccountNumber>\n" + 
				"					<Amount>DUMMY</Amount>\n" + 
				"				</executeFinacleScript_CustomData>\n" + 
				"			</executeFinacleScriptRequest>\n" + 
				"		</Body>\n" + 
				"	</FIXML>";
		
		String endPointUrl = environment.getProperty("finacleEndPointUrl");
		String response = finacleClient.executeSoap(data, endPointUrl);
		finacleClient.logEntryProcess(data, requestDateTime, reqId, endPointUrl, serviceType,
				environment.getProperty("trialPrematureClosure"));
		if (!response.isEmpty()) {
			String jsonResult = Converter.xmlCollectionToJson(response);
			return new ResponseEntity<>(jsonResult, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("", HttpStatus.GATEWAY_TIMEOUT);
		}
	}
	
	@PostMapping("/delink")
	public ResponseEntity<String> deLink(@RequestParam String accNo, @RequestHeader String authToken,
			@RequestHeader String serviceType) {
		long reqId = allTokenCreateAndValidate.validateToken(authToken, serviceType);
		if (reqId == 0) {
			JSONObject errorObj = new JSONObject();
			errorObj.put("error", "unauthorized");
			return new ResponseEntity<>(errorObj.toString(), HttpStatus.UNAUTHORIZED);
		}
		String requestUuid = "Req_" + Generator.randomNumberGenerator(13);
		String getDelinkFinacleExecuteServiceRequestId = environment.getProperty("getDelinkFinacleExecuteServiceRequestId");
		String getDelinkFinacleServiceRequestVersion = environment.getProperty("getDelinkFinacleServiceRequestVersion");
		String getDelinkFinacleChannelId = environment.getProperty("getDelinkFinacleChannelId");
		LocalDateTime messageDateTime = LocalDateTime.now();
		String getDelinkrequestId = environment.getProperty("getDelinkrequestId");
		
		String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"					<FIXML xsi:schemaLocation=\"http://www.finacle.com/fixml executeFinacleScript.xsd\" xmlns=\"http://www.finacle.com/fixml\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" + 
				"					<Header>\n" + 
				"						<RequestHeader>\n" + 
				"						\n" + 
				"							<MessageKey>\n" + 
				"								<RequestUUID>"+ requestUuid +"</RequestUUID>\n" + 
				"								<ServiceRequestId>"+ getDelinkFinacleExecuteServiceRequestId +"</ServiceRequestId>\n" + 
				"								<ServiceRequestVersion>"+ getDelinkFinacleServiceRequestVersion +"</ServiceRequestVersion>\n" + 
				"								<ChannelId>"+ getDelinkFinacleChannelId +"</ChannelId>\n" + 
				"								<LanguageId></LanguageId>\n" + 
				"							</MessageKey>\n" + 
				"							\n" + 
				"							<RequestMessageInfo>\n" + 
				"								<BankId></BankId>\n" + 
				"								<TimeZone></TimeZone>\n" + 
				"								<EntityId></EntityId>\n" + 
				"								<EntityType></EntityType>\n" + 
				"								<ArmCorrelationId></ArmCorrelationId>\n" + 
				"								<MessageDateTime>"+ messageDateTime +"</MessageDateTime>\n" + 
				"							</RequestMessageInfo>\n" + 
				"			\n" + 
				"							<Security>\n" + 
				"								<Token>\n" + 
				"									<PasswordToken>\n" + 
				"										<UserId></UserId>\n" + 
				"										<Password></Password>\n" + 
				"									</PasswordToken>\n" + 
				"								</Token>\n" + 
				"								<FICertToken></FICertToken>\n" + 
				"								<RealUserLoginSessionId></RealUserLoginSessionId>\n" + 
				"								<RealUser></RealUser>\n" + 
				"								<RealUserPwd></RealUserPwd>\n" + 
				"								<SSOTransferToken></SSOTransferToken>\n" + 
				"							</Security>\n" + 
				"						</RequestHeader>\n" + 
				"					</Header>\n" + 
				"					\n" + 
				"					<Body>\n" + 
				"						<executeFinacleScriptRequest>\n" + 
				"							<ExecuteFinacleScriptInputVO>\n" + 
				"								<requestId>"+ getDelinkrequestId +"</requestId>\n" + 
				"							</ExecuteFinacleScriptInputVO>\n" + 
				"							<executeFinacleScript_CustomData>\n" + 
				"								<ACCT_NO>"+ accNo +"</ACCT_NO>\n" + 
				"							</executeFinacleScript_CustomData>\n" + 
				"						</executeFinacleScriptRequest>\n" + 
				"					</Body>\n" + 
				"				</FIXML>";
		String endPointUrl = environment.getProperty("finacleEndPointUrl");
		String response = finacleClient.executeSoap(data, endPointUrl);
		finacleClient.logEntryProcess(data, messageDateTime, reqId, endPointUrl, serviceType,
				environment.getProperty("delinkApi"));
		
		if (!response.isEmpty()) {
			String jsonResult = Converter.xmlCollectionToJson(response);
			return new ResponseEntity<>(jsonResult, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("", HttpStatus.GATEWAY_TIMEOUT);
		}
	}
	
	@PostMapping("/sicancellation")
	public ResponseEntity<String> siCancellation(@RequestParam String accNo, @RequestHeader String authToken,
			@RequestHeader String serviceType) {
		long reqId = allTokenCreateAndValidate.validateToken(authToken, serviceType);
		if (reqId == 0) {
			JSONObject errorObj = new JSONObject();
			errorObj.put("error", "unauthorized");
			return new ResponseEntity<>(errorObj.toString(), HttpStatus.UNAUTHORIZED);
		}
		String requestUuid = "Req_" + Generator.randomNumberGenerator(13);
		String getSiCAncellationFinacleExecuteServiceRequestId = environment.getProperty("getSiCAncellationFinacleExecuteServiceRequestId");
		String getSiCAncellationFinacleServiceRequestVersion = environment.getProperty("getSiCAncellationFinacleServiceRequestVersion");
		String getSiCAncellationFinacleChannelId = environment.getProperty("getSiCAncellationFinacleChannelId");
		String getSiCAncellationrequestId = environment.getProperty("getSiCAncellationrequestId");
		LocalDateTime messageDateTime = LocalDateTime.now();
		String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"			  <FIXML xsi:schemaLocation=\"http://www.finacle.com/fixml BalInq.xsd\" xmlns=\"http://www.finacle.com/fixml\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" + 
				"				  <Header>\n" + 
				"					  <RequestHeader>\n" + 
				"						  <MessageKey>\n" + 
				"							  <RequestUUID>"+ requestUuid +"</RequestUUID>\n" + 
				"							  <ServiceRequestId>" + getSiCAncellationFinacleExecuteServiceRequestId + "</ServiceRequestId>\n" + 
				"							  <ServiceRequestVersion>"+ getSiCAncellationFinacleServiceRequestVersion +"</ServiceRequestVersion>\n" + 
				"							  <ChannelId>"+getSiCAncellationFinacleChannelId+"</ChannelId>\n" + 
				"							  <LanguageId></LanguageId>\n" + 
				"						  </MessageKey>\n" + 
				"						  <RequestMessageInfo>\n" + 
				"							  <BankId></BankId>\n" + 
				"							  <TimeZone></TimeZone>\n" + 
				"							  <EntityId></EntityId>\n" + 
				"							  <EntityType></EntityType>\n" + 
				"							  <ArmCorrelationId></ArmCorrelationId>\n" + 
				"							  <MessageDateTime>"+ messageDateTime +"</MessageDateTime>\n" + 
				"						  </RequestMessageInfo>\n" + 
				"						  <Security>\n" + 
				"							  <Token>\n" + 
				"								  <PasswordToken>\n" + 
				"									  <UserId></UserId>\n" + 
				"									  <Password></Password>\n" + 
				"								  </PasswordToken>\n" + 
				"							  </Token>\n" + 
				"							  <FICertToken></FICertToken>\n" + 
				"							  <RealUserLoginSessionId></RealUserLoginSessionId>\n" + 
				"							  <RealUser></RealUser>\n" + 
				"							  <RealUserPwd></RealUserPwd>\n" + 
				"							  <SSOTransferToken></SSOTransferToken>\n" + 
				"						  </Security>\n" + 
				"					  </RequestHeader>\n" + 
				"				  </Header>\n" + 
				"				  <Body>\n" + 
				"					  <executeFinacleScriptRequest>\n" + 
				"						  <ExecuteFinacleScriptInputVO>\n" + 
				"							  <requestId>"+ getSiCAncellationrequestId +"</requestId>\n" + 
				"						  </ExecuteFinacleScriptInputVO>\n" + 
				"						  <executeFinacleScript_CustomData>\n" + 
				"							  <AcctNum>"+ accNo +"</AcctNum>\n" + 
				"						  </executeFinacleScript_CustomData>\n" + 
				"					  </executeFinacleScriptRequest>\n" + 
				"				  </Body>\n" + 
				"			  </FIXML>";
		String endPointUrl = environment.getProperty("finacleEndPointUrl");
		String response = finacleClient.executeSoap(data, endPointUrl);
		finacleClient.logEntryProcess(data, messageDateTime, reqId, endPointUrl, serviceType,
				environment.getProperty("siCancellation"));
		
		if (!response.isEmpty()) {
			String jsonResult = Converter.xmlCollectionToJson(response);
			return new ResponseEntity<>(jsonResult, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("", HttpStatus.GATEWAY_TIMEOUT);
		}
	}
	
	@PostMapping("/cbspremature")
	public ResponseEntity<String> cbsPreMature(
			@RequestParam String accNo,
			@RequestParam String repayAccNo,
			@RequestParam String prematAmount,
			@RequestHeader String authToken,
			@RequestHeader String serviceType) {
		long reqId = allTokenCreateAndValidate.validateToken(authToken, serviceType);
		if (reqId == 0) {
			JSONObject errorObj = new JSONObject();
			errorObj.put("error", "unauthorized");
			return new ResponseEntity<>(errorObj.toString(), HttpStatus.UNAUTHORIZED);
		}
		
		String requestUuid = "Req_" + Generator.randomNumberGenerator(13);
		String getCbsPreMatureFinacleExecuteServiceRequestId = environment.getProperty("getCbsPreMatureFinacleExecuteServiceRequestId");
		String getCbsPreMatureFinacleServiceRequestVersion = environment.getProperty("getCbsPreMatureFinacleServiceRequestVersion");
		String getCbsPreMatureFinacleChannelId = environment.getProperty("getCbsPreMatureFinacleChannelId");
		String getCbsPreMaturerequestId = environment.getProperty("getCbsPreMaturerequestId");
		LocalDateTime messageDateTime = LocalDateTime.now();
		
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		String strDate= formatter.format(date);
		
		String data = "<FIXML xsi:schemaLocation=\"http://www.finacle.com/fixml executeFinacleScript.xsd\" xmlns=\"http://www.finacle.com/fixml\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" + 
				"		 <Header>\n" + 
				"			 <RequestHeader>\n" + 
				"				 <MessageKey>\n" + 
				"					 <RequestUUID>"+requestUuid+"</RequestUUID>\n" + 
				"					 <ServiceRequestId>"+ getCbsPreMatureFinacleExecuteServiceRequestId +"</ServiceRequestId>\n" + 
				"					 <ServiceRequestVersion>"+ getCbsPreMatureFinacleServiceRequestVersion +"</ServiceRequestVersion>\n" + 
				"					 <ChannelId>"+ getCbsPreMatureFinacleChannelId +"</ChannelId>\n" + 
				"					 <LanguageId></LanguageId>\n" + 
				"				 </MessageKey>\n" + 
				"				 <RequestMessageInfo>\n" + 
				"					 <BankId></BankId>\n" + 
				"					 <TimeZone></TimeZone>\n" + 
				"					 <EntityId></EntityId>\n" + 
				"					 <EntityType></EntityType>\n" + 
				"					 <ArmCorrelationId></ArmCorrelationId>\n" + 
				"					 <MessageDateTime>"+ messageDateTime +"</MessageDateTime>\n" + 
				"				 </RequestMessageInfo>\n" + 
				"				 <Security>\n" + 
				"					 <Token>\n" + 
				"						 <PasswordToken>\n" + 
				"							 <UserId></UserId>\n" + 
				"							 <Password></Password>\n" + 
				"						 </PasswordToken>\n" + 
				"					 </Token>\n" + 
				"					 <FICertToken></FICertToken>\n" + 
				"					 <RealUserLoginSessionId></RealUserLoginSessionId>\n" + 
				"					 <RealUser></RealUser>\n" + 
				"					 <RealUserPwd></RealUserPwd>\n" + 
				"					 <SSOTransferToken></SSOTransferToken>\n" + 
				"				 </Security>\n" + 
				"			 </RequestHeader>\n" + 
				"		 </Header>\n" + 
				"		 <Body>\n" + 
				"			 <executeFinacleScriptRequest>\n" + 
				"				 <ExecuteFinacleScriptInputVO>\n" + 
				"					 <requestId>"+ getCbsPreMaturerequestId +"</requestId>\n" + 
				"				 </ExecuteFinacleScriptInputVO>\n" + 
				"				 <executeFinacleScript_CustomData>\n" + 
				"					 <AcctNum>"+ accNo +"</AcctNum>\n" + 
				"					 <repymntAcctId>"+repayAccNo+"</repymntAcctId>\n" + 
				"					 <clsValDt>"+strDate+"</clsValDt>\n" + 
				"					 <amt>"+prematAmount+"</amt>\n" + 
				"				 </executeFinacleScript_CustomData>\n" + 
				"			 </executeFinacleScriptRequest>\n" + 
				"		 </Body>\n" + 
				"	 </FIXML>";
		String endPointUrl = environment.getProperty("finacleEndPointUrl");
		String response = finacleClient.executeSoap(data, endPointUrl);
		finacleClient.logEntryProcess(data, messageDateTime, reqId, endPointUrl, serviceType,
				environment.getProperty("cbsPremature"));
		
		if (!response.isEmpty()) {
			String jsonResult = Converter.xmlCollectionToJson(response);
			return new ResponseEntity<>(jsonResult, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("", HttpStatus.GATEWAY_TIMEOUT);
		}
	}
	
	@PostMapping("/fdcmi")
	public ResponseEntity<String> fdchangeofmaturityinstruction(
			@RequestParam String accNo,
			@RequestParam String autoCloseFlag,
			@RequestParam String autoRenewalFlag,
			@RequestParam String maxRenewal,
			@RequestParam String termDays,
			@RequestParam String termMonths,
			@RequestParam String renewalScheme,
			@RequestParam String tblCode,
			@RequestParam String renewalOption,
			@RequestHeader String authToken,
			@RequestHeader String serviceType) {
		long reqId = allTokenCreateAndValidate.validateToken(authToken, serviceType);
		if (reqId == 0) {
			JSONObject errorObj = new JSONObject();
			errorObj.put("error", "unauthorized");
			return new ResponseEntity<>(errorObj.toString(), HttpStatus.UNAUTHORIZED);
		}
		
		String requestUuid = "Rea_" + Generator.randomNumberGenerator(12);
		String fdcmiFinacleExecuteServiceRequestId = environment.getProperty("fdcmiFinacleExecuteServiceRequestId");
		String fdcmiFinacleServiceRequestVersion = environment.getProperty("fdcmiFinacleServiceRequestVersion");
		String fdcmiFinacleChannelId = environment.getProperty("fdcmiFinacleChannelId");
		
		LocalDateTime messageDateTime = LocalDateTime.now();
						
		String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<FIXML xmlns=\"http://www.finacle.com/fixml\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.finacle.com/fixml TDAcctMod.xsd\">\n" + 
				"   <Header>\n" + 
				"      <RequestHeader>\n" + 
				"         <MessageKey>\n" + 
				"            <RequestUUID>"+requestUuid+"</RequestUUID>\n" + 
				"            <ServiceRequestId>"+fdcmiFinacleExecuteServiceRequestId+"</ServiceRequestId>\n" + 
				"            <ServiceRequestVersion>"+fdcmiFinacleServiceRequestVersion+"</ServiceRequestVersion>\n" + 
				"            <ChannelId>"+fdcmiFinacleChannelId+"</ChannelId>\n" + 
				"            <LanguageId />\n" + 
				"         </MessageKey>\n" + 
				"         <RequestMessageInfo>\n" + 
				"            <BankId />\n" + 
				"            <TimeZone />\n" + 
				"            <EntityId />\n" + 
				"            <EntityType />\n" + 
				"            <ArmCorrelationId />\n" + 
				"            <MessageDateTime>"+ messageDateTime +"</MessageDateTime>\n" + 
				"         </RequestMessageInfo>\n" + 
				"         <Security>\n" + 
				"            <Token>\n" + 
				"               <PasswordToken>\n" + 
				"                  <UserId />\n" + 
				"                  <Password />\n" + 
				"               </PasswordToken>\n" + 
				"            </Token>\n" + 
				"            <FICertToken />\n" + 
				"            <RealUserLoginSessionId />\n" + 
				"            <RealUser />\n" + 
				"            <RealUserPwd />\n" + 
				"            <SSOTransferToken />\n" + 
				"         </Security>\n" + 
				"      </RequestHeader>\n" + 
				"   </Header>\n" + 
				"   <Body>\n" + 
				"      <TDAcctModRequest>\n" + 
				"         <TDAcctModRq>\n" + 
				"            <TDAcctId>\n" + 
				"               <AcctId>"+accNo+"</AcctId>\n" + 
				"            </TDAcctId>\n" + 
				"            <RenewalDtls>\n" + 
				"               <AutoCloseOnMaturityFlg>"+autoCloseFlag+"</AutoCloseOnMaturityFlg>\n" + 
				"               <AutoRenewalflg>"+autoRenewalFlag+"</AutoRenewalflg>\n" + 
				"               <MaxNumOfRenewalAllwd>"+maxRenewal+"</MaxNumOfRenewalAllwd>\n" + 
				"               <RenewalTerm>\n" + 
				"                  <Days>"+termDays+"</Days>\n" + 
				"                  <Months>"+termMonths+"</Months>\n" + 
				"               </RenewalTerm>\n" + 
				"               <RenewalSchm>\n" + 
				"                  <SchmCode>"+renewalScheme+"</SchmCode>\n" + 
				"               </RenewalSchm>\n" + 
				"               <IntTblCode>"+tblCode+"</IntTblCode>\n" + 
				"               <RenewalOption>"+renewalOption+"</RenewalOption>\n" + 
				"            </RenewalDtls>\n" + 
				"         </TDAcctModRq>\n" + 
				"      </TDAcctModRequest>\n" + 
				"   </Body>\n" + 
				"</FIXML>";
		String endPointUrl = environment.getProperty("finacleEndPointUrl");
		String response = finacleClient.executeSoap(data, endPointUrl);
		finacleClient.logEntryProcess(data, messageDateTime, reqId, endPointUrl, serviceType,
				environment.getProperty("fdcmiApi"));
		
		if (!response.isEmpty()) {
			String jsonResult = Converter.xmlCollectionToJson(response);
			return new ResponseEntity<>(jsonResult, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("", HttpStatus.GATEWAY_TIMEOUT);
		}
	}
}
