package in.co.indusnet.rekyc.config;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import in.co.indusnet.rekyc.model.TblAccountType;
import in.co.indusnet.rekyc.model.TblDocType;
import in.co.indusnet.rekyc.model.TblEmailTemplate;
import in.co.indusnet.rekyc.model.TblErrorMessages;
import in.co.indusnet.rekyc.model.TblSettings;
import in.co.indusnet.rekyc.repository.AccountTypeRepository;
import in.co.indusnet.rekyc.repository.DocTypeRepository;
import in.co.indusnet.rekyc.repository.SettingsRepository;
import in.co.indusnet.rekyc.repository.TblEmailTemplateRepository;
import in.co.indusnet.rekyc.repository.TblErrorMessagesRepository;

@Configuration
public class InsertTableBeanConfigution {
	
	@Autowired
	private TblErrorMessagesRepository tblErrorMesRepository;
	
	@Autowired
	private SettingsRepository settingRepository;
	
	@Autowired
    private	DocTypeRepository docTypeRepository;
	
	@Autowired
	private TblEmailTemplateRepository tblEmailTemplateRepository;
	
	@Autowired
	private AccountTypeRepository accountTypeRepository;
	@Bean
	public void saveErrorMessages() {
		tblErrorMesRepository.deleteAll();
		tblErrorMesRepository.save(new TblErrorMessages(1,"COMMON", "INVALID_OTP_ERR" ,null ,"OTP entered is incorrect",true , LocalDateTime.now() ,LocalDateTime.now()));
		tblErrorMesRepository.save(new TblErrorMessages(2,"COMMON", "UNABLE_TO_PROCESS_ERR" ,null ,"Unfortunately, we are unable to process your request now. Please try again in sometime",true , LocalDateTime.now() ,LocalDateTime.now()));
		tblErrorMesRepository.save(new TblErrorMessages(3 ,"COMMON", "MAX_ATTEMPT_ERR" ,null ,"You have exceeded maximum number of attempts. Please try again after sometime",true , LocalDateTime.now() ,LocalDateTime.now()));
		tblErrorMesRepository.save(new TblErrorMessages(4 ,"COMMON", "NO_ELIGIBLE_ACCOUNT_FOUND" ,null ,"Great! You are not due for re-KYC update",true , LocalDateTime.now() ,LocalDateTime.now()));
		tblErrorMesRepository.save(new TblErrorMessages(5 ,"REKYC", "LR_PANV_ANC_SUCCESS" ,null ,"Your request for re-KYC update has been processed successfully",true , LocalDateTime.now() ,LocalDateTime.now()));
		tblErrorMesRepository.save(new TblErrorMessages(6 ,"REKYC", "LR_DA_SUCCESS" ,null ,"Your request for re-KYC update & dormant account activation has been processed successfully",true , LocalDateTime.now() ,LocalDateTime.now()));	
		tblErrorMesRepository.save(new TblErrorMessages(7 ,"REKYC", "LR_AC_SUCCESS" ,null ,"Your request for re-KYC update will be processed in [COMMUNICATION_HOUR] hours",true , LocalDateTime.now() ,LocalDateTime.now()));
		tblErrorMesRepository.save(new TblErrorMessages(8 ,"REKYC", "LR_AC_DA_SUCCESS" ,null ,"Your request for re-KYC update & dormant account activation will be processed in [COMMUNICATION_HOUR] hours",true , LocalDateTime.now() ,LocalDateTime.now()));
		tblErrorMesRepository.save(new TblErrorMessages(9 ,"REKYC", "MRHR_SUCCESS" ,null ,"Your request for re-KYC update will be processed in [COMMUNICATION_HOUR] hours",true , LocalDateTime.now() ,LocalDateTime.now()));
		tblErrorMesRepository.save(new TblErrorMessages(10 ,"REKYC", "MRHR_DA_SUCCESS" ,null ,"Your request for re-KYC update & dormant account activation will be processed in [COMMUNICATION_HOUR] hours",true , LocalDateTime.now() ,LocalDateTime.now()));
		//tblErrorMesRepository.save(new TblErrorMessages(7 ,"COMMON", "DATA_SAVED" ,null ,"Data saved accordingly",true , LocalDateTime.now() ,LocalDateTime.now()));
		//tblErrorMesRepository.save(new TblErrorMessages(7 ,"COMMON", "OTP_SENT" ,null ,"OTP sent successfully",true , LocalDateTime.now() ,LocalDateTime.now()));
	}
	
	/*@Bean
	public void saveDocType() {
		docTypeRepository.deleteAll();
		docTypeRepository.save(new TblDocType(1, "PASSPORT", "Passport", true, 3, LocalDateTime.now() ,LocalDateTime.now()));
		docTypeRepository.save(new TblDocType(2, "DRLC", "Driving License", true, 2, LocalDateTime.now() ,LocalDateTime.now()));
		docTypeRepository.save(new TblDocType(3, "EPIC", "Voter Id / Election Card", true, 4, LocalDateTime.now() ,LocalDateTime.now()));
		docTypeRepository.save(new TblDocType(4, "LINPR", "Letter issued by National Population Register", true, 6, LocalDateTime.now() ,LocalDateTime.now()));
		docTypeRepository.save(new TblDocType(4, "NJC", "NREGA Job Card", true, 5, LocalDateTime.now() ,LocalDateTime.now()));
		docTypeRepository.save(new TblDocType(4, "AADHAR", "Aadhar Card", true, 1, LocalDateTime.now() ,LocalDateTime.now()));
		docTypeRepository.save(new TblDocType(4, "OTHERS", "Others", true, 7, LocalDateTime.now() ,LocalDateTime.now()));
		
	}*/
	
	@Bean
	public void saveSettingsTable() {
		settingRepository.deleteAll();
		settingRepository.save(new TblSettings(1, 5, 2, 10, ".jpg,.jpeg,.png,.pdf", 3, 30, 4, 3, 10, null,null,null,null,null,true, LocalDateTime.now() ,LocalDateTime.now()));
		
	}
	
	@Bean
	public void saveTblEmailTemplate() {
		tblEmailTemplateRepository.deleteAll();
		tblEmailTemplateRepository.save(new TblEmailTemplate(1,"One-Time Password for re-KYC update request","<table width=\"600\" border=\"0\" cellpadding=\"0\"  cellspacing=\"0\" align=\"center\" style=\"margin:0 auto; padding:0;\">\n" + 
				"	<tr>\n" + 
				"		<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; background:#fff; display:block; border:1px solid #ccd2db\">\n" + 
				"			<table border=\"0\" cellpadding=\"0\"  cellspacing=\"0\" align=\"center\" style=\"margin:0; padding:0;\">\n" + 
				"				<tr>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; width:20px;\">&nbsp;</td>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; width:560px;\">&nbsp;</td>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; width:20px;\">&nbsp;</td>\n" + 
				"				</tr>\n" + 
				"				<tr>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; width:20px;\">&nbsp;</td>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">\n" + 
				"						<table width=\"100%\" border=\"0\" align=\"center\" cellpadding=\"0\"  cellspacing=\"0\" style=\"margin:0; padding:0;\">\n" + 
				"							<tr>\n" + 
				"								<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">\n" + 
				"									<p style=\"padding:0; margin:0 0 15px 0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">Dear Customer,</p>\n" + 
				"									<p style=\"padding:0; margin:0 0 15px 0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">[VAR_OTP]\n" + 
				"									</strong> is your One-Time Password for re-KYC update request.\n" + 
				"								</p>\n" + 
				"								<p style=\"padding:0; margin:0 0 15px 0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">We thank you for giving us an opportunity to serve you and we look forward to your continued\n" + 
				"patronage.</p>\n" + 
				"								<p style=\"padding:0; margin:0 0 15px 0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">For any further assistance or guidance, please call our Phone Banking centre at [REKYC_RELATED_CONTACT_NO] or\n" + 
				"write to us at \n" + 
				"									<a href=\"mailto:[REKYC_RELATED_CONTACT_EMAIL]\" target=\"_top\">[REKYC_RELATED_CONTACT_EMAIL]</a>\n" + 
				"								</p>\n" + 
				"								<p style=\"padding:0; margin:0 0 15px 0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">Assuring you of our best services.</p>\n" + 
				"							</td>\n" + 
				"						</tr>\n" + 
				"						<tr>\n" + 
				"							<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">&nbsp;</td>\n" + 
				"						</tr>\n" + 
				"						<tr>\n" + 
				"							<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">\n" + 
				"								<p style=\"padding:0; margin:0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">Sincerely, </p>\n" + 
				"								<p style=\"padding:0; margin:0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">Team IndusInd Bank Limited.</p>\n" + 
				"							</td>\n" + 
				"						</tr>\n" + 
				"					</table>\n" + 
				"				</td>\n" + 
				"				<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; width:20px;\"\">&nbsp;</td>\n" + 
				"			</tr>\n" + 
				"			<tr>\n" + 
				"				<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">&nbsp;</td>\n" + 
				"				<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">&nbsp;</td>\n" + 
				"				<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">&nbsp;</td>\n" + 
				"			</tr>\n" + 
				"		</table>\n" + 
				"	</td>\n" + 
				"</tr>\n" + 
				"<tr>\n" + 
				"	<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; background:#fff; display:block;\"></td>\n" + 
				"</tr>undefined</table>"
				,"SEND_OTP_VIA_EMAIL", true ,LocalDateTime.now() ,LocalDateTime.now()));
		
		
		tblEmailTemplateRepository.save(new TblEmailTemplate(2 ,"Your re-KYC update request has been closed successfully",
				"<table width=\"600\" border=\"0\" cellpadding=\"0\"  cellspacing=\"0\" align=\"center\" style=\"margin:0 auto; padding:0;\">\n" + 
				"	<tr>\n" + 
				"		<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; background:#fff; display:block; border:1px solid #ccd2db\">\n" + 
				"			<table border=\"0\" cellpadding=\"0\"  cellspacing=\"0\" align=\"center\" style=\"margin:0; padding:0;\">\n" + 
				"				<tr>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; width:20px;\">&nbsp;</td>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; width:560px;\">&nbsp;</td>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; width:20px;\">&nbsp;</td>\n" + 
				"				</tr>\n" + 
				"				<tr>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; width:20px;\">&nbsp;</td>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">\n" + 
				"						<table width=\"100%\" border=\"0\" align=\"center\" cellpadding=\"0\"  cellspacing=\"0\" style=\"margin:0; padding:0;\">\n" + 
				"							<tr>\n" + 
				"								<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">\n" + 
				"									<p style=\"padding:0; margin:0 0 15px 0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">Dear Customer,</p>\n" + 
				"									<p style=\"padding:0; margin:0 0 15px 0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">Your Service Request #[REFERENCE_ID] for re-KYC update has been processed successfully.</p>\n" + 
				"									<p style=\"padding:0; margin:0 0 15px 0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">We thank you for giving us an opportunity to serve you and we look forward to your continued\n" + 
				"patronage.</p>\n" + 
				"									<p style=\"padding:0; margin:0 0 15px 0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">For any further assistance or guidance, please call our Phone Banking centre at [REKYC_RELATED_CONTACT_NO] or\n" + 
				"write to us at \n" + 
				"										<a href=\"mailto:[REKYC_RELATED_CONTACT_EMAIL]\" target=\"_top\">[REKYC_RELATED_CONTACT_EMAIL]</a>\n" + 
				"									</p>\n" + 
				"									<p style=\"padding:0; margin:0 0 15px 0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">Assuring you of our best services.</p>\n" + 
				"								</td>\n" + 
				"							</tr>\n" + 
				"							<tr>\n" + 
				"								<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">&nbsp;</td>\n" + 
				"							</tr>\n" + 
				"							<tr>\n" + 
				"								<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">\n" + 
				"									<p style=\"padding:0; margin:0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">Sincerely, </p>\n" + 
				"									<p style=\"padding:0; margin:0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">Team IndusInd Bank Limited.</p>\n" + 
				"								</td>\n" + 
				"							</tr>\n" + 
				"						</table>\n" + 
				"					</td>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; width:20px;\"\">&nbsp;</td>\n" + 
				"				</tr>\n" + 
				"				<tr>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">&nbsp;</td>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">&nbsp;</td>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">&nbsp;</td>\n" + 
				"				</tr>\n" + 
				"			</table>\n" + 
				"		</td>\n" + 
				"	</tr>\n" + 
				"	<tr>\n" + 
				"		<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; background:#fff; display:block;\"></td>\n" + 
				"	</tr>\n" + 
				"	<tr>\n" + 
				"		<td align=\"left\" valign=\"top\" style=\"padding:20px; margin:0; background:#ccd2db; display:block;\">\n" + 
				"			<p style=\"padding:0; margin:0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:12px; line-height:18px; color:#333;\">This Email and files transmitted are confidential and intended solely for\n" + 
				"the use of the individual or entity to whom they are addressed. If you\n" + 
				"have received this Email in error please notify the system manager.\n" + 
				"Please note that any views or opinions presented in this Email are solely\n" + 
				"those of the author and do not necessarily represent those of IndusInd\n" + 
				"Bank Ltd. Finally, the recipient should check this Email and any\n" + 
				"attachments for the presence of viruses. IndusInd Bank Ltd. accepts no\n" + 
				"liability for any damage caused by any virus transmitted by this Email.</p>\n" + 
				"		</td>\n" + 
				"	</tr>\n" + 
				"</table>"
				,"REKYC_CONFIRMATION_MAIL_LR_STP", false ,LocalDateTime.now() ,LocalDateTime.now()));
		
		tblEmailTemplateRepository.save(new TblEmailTemplate(3 ,"Your re-KYC update request has been closed successfully",
				"<table width=\"600\" border=\"0\" cellpadding=\"0\"  cellspacing=\"0\" align=\"center\" style=\"margin:0 auto; padding:0;\">\n" + 
				"	<tr>\n" + 
				"		<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; background:#fff; display:block; border:1px solid #ccd2db\">\n" + 
				"			<table border=\"0\" cellpadding=\"0\"  cellspacing=\"0\" align=\"center\" style=\"margin:0; padding:0;\">\n" + 
				"				<tr>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; width:20px;\">&nbsp;</td>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; width:560px;\">&nbsp;</td>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; width:20px;\">&nbsp;</td>\n" + 
				"				</tr>\n" + 
				"				<tr>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; width:20px;\">&nbsp;</td>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">\n" + 
				"						<table width=\"100%\" border=\"0\" align=\"center\" cellpadding=\"0\"  cellspacing=\"0\" style=\"margin:0; padding:0;\">\n" + 
				"							<tr>\n" + 
				"								<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">\n" + 
				"									<p style=\"padding:0; margin:0 0 15px 0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">Dear Customer,</p>\n" + 
				"									<p style=\"padding:0; margin:0 0 15px 0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">Your Service Request #[REFERENCE_ID] for re-KYC update and activation of dormant account has been\n" + 
				"processed successfully.</p>\n" + 
				"									<p style=\"padding:0; margin:0 0 15px 0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">We thank you for giving us an opportunity to serve you and we look forward to your continued\n" + 
				"patronage.</p>\n" + 
				"									<p style=\"padding:0; margin:0 0 15px 0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">For any further assistance or guidance, please call our Phone Banking centre at [REKYC_RELATED_CONTACT_NO] or\n" + 
				"write to us at \n" + 
				"										<a href=\"mailto:[REKYC_RELATED_CONTACT_EMAIL]\" target=\"_top\">[REKYC_RELATED_CONTACT_EMAIL]</a>\n" + 
				"									</p>\n" + 
				"									<p style=\"padding:0; margin:0 0 15px 0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">Assuring you of our best services.</p>\n" + 
				"								</td>\n" + 
				"							</tr>\n" + 
				"							<tr>\n" + 
				"								<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">&nbsp;</td>\n" + 
				"							</tr>\n" + 
				"							<tr>\n" + 
				"								<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">\n" + 
				"									<p style=\"padding:0; margin:0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">Sincerely, </p>\n" + 
				"									<p style=\"padding:0; margin:0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">Team IndusInd Bank Limited.</p>\n" + 
				"								</td>\n" + 
				"							</tr>\n" + 
				"						</table>\n" + 
				"					</td>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; width:20px;\"\">&nbsp;</td>\n" + 
				"				</tr>\n" + 
				"				<tr>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">&nbsp;</td>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">&nbsp;</td>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">&nbsp;</td>\n" + 
				"				</tr>\n" + 
				"			</table>\n" + 
				"		</td>\n" + 
				"	</tr>\n" + 
				"	<tr>\n" + 
				"		<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; background:#fff; display:block;\"></td>\n" + 
				"	</tr>\n" + 
				"	<tr>\n" + 
				"		<td align=\"left\" valign=\"top\" style=\"padding:20px; margin:0; background:#ccd2db; display:block;\">\n" + 
				"			<p style=\"padding:0; margin:0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:12px; line-height:18px; color:#333;\">This Email and files transmitted are confidential and intended solely for\n" + 
				"the use of the individual or entity to whom they are addressed. If you\n" + 
				"have received this Email in error please notify the system manager.\n" + 
				"Please note that any views or opinions presented in this Email are solely\n" + 
				"those of the author and do not necessarily represent those of IndusInd\n" + 
				"Bank Ltd. Finally, the recipient should check this Email and any\n" + 
				"attachments for the presence of viruses. IndusInd Bank Ltd. accepts no\n" + 
				"liability for any damage caused by any virus transmitted by this Email.</p>\n" + 
				"		</td>\n" + 
				"	</tr>\n" + 
				"</table>"
				,"REKYC_CONFIRMATION_MAIL_LR_STP_DA", false ,LocalDateTime.now() ,LocalDateTime.now()));
		
		tblEmailTemplateRepository.save(new TblEmailTemplate(4 ,"Acknowledgement for Re-KYC Update Request",
				"<table width=\"600\" border=\"0\" cellpadding=\"0\"  cellspacing=\"0\" align=\"center\" style=\"margin:0 auto; padding:0;\">\n" + 
				"	<tr>\n" + 
				"		<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; background:#fff; display:block; border:1px solid #ccd2db\">\n" + 
				"			<table border=\"0\" cellpadding=\"0\"  cellspacing=\"0\" align=\"center\" style=\"margin:0; padding:0;\">\n" + 
				"				<tr>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; width:20px;\">&nbsp;</td>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; width:560px;\">&nbsp;</td>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; width:20px;\">&nbsp;</td>\n" + 
				"				</tr>\n" + 
				"				<tr>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; width:20px;\">&nbsp;</td>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">\n" + 
				"						<table width=\"100%\" border=\"0\" align=\"center\" cellpadding=\"0\"  cellspacing=\"0\" style=\"margin:0; padding:0;\">\n" + 
				"							<tr>\n" + 
				"								<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">\n" + 
				"									<p style=\"padding:0; margin:0 0 15px 0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">Dear Customer,</p>\n" + 
				"									<p style=\"padding:0; margin:0 0 15px 0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">Your Service Request #[REFERENCE_ID] for re-KYC update has been submitted successfully and will be\n" + 
				"processed in [COMMUNICATION_HOUR] hours. As per your request, your address will also be updated as per address in\n" + 
				"address proof document submitted by you.</p>\n" + 
				"									<p style=\"padding:0; margin:0 0 15px 0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">We thank you for giving us an opportunity to serve you and we look forward to your continued\n" + 
				"patronage.</p>\n" + 
				"									<p style=\"padding:0; margin:0 0 15px 0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">For any further assistance or guidance, please call our Phone Banking centre at [REKYC_RELATED_CONTACT_NO] or\n" + 
				"write to us at \n" + 
				"										<a href=\"mailto:[REKYC_RELATED_CONTACT_EMAIL]\" target=\"_top\">[REKYC_RELATED_CONTACT_EMAIL]</a>\n" + 
				"									</p>\n" + 
				"									<p style=\"padding:0; margin:0 0 15px 0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">Assuring you of our best services.</p>\n" + 
				"								</td>\n" + 
				"							</tr>\n" + 
				"							<tr>\n" + 
				"								<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">&nbsp;</td>\n" + 
				"							</tr>\n" + 
				"							<tr>\n" + 
				"								<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">\n" + 
				"									<p style=\"padding:0; margin:0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">Sincerely, </p>\n" + 
				"									<p style=\"padding:0; margin:0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">Team IndusInd Bank Limited.</p>\n" + 
				"								</td>\n" + 
				"							</tr>\n" + 
				"						</table>\n" + 
				"					</td>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; width:20px;\"\">&nbsp;</td>\n" + 
				"				</tr>\n" + 
				"				<tr>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">&nbsp;</td>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">&nbsp;</td>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">&nbsp;</td>\n" + 
				"				</tr>\n" + 
				"			</table>\n" + 
				"		</td>\n" + 
				"	</tr>\n" + 
				"	<tr>\n" + 
				"		<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; background:#fff; display:block;\"></td>\n" + 
				"	</tr>\n" + 
				"	<tr>\n" + 
				"		<td align=\"left\" valign=\"top\" style=\"padding:20px; margin:0; background:#ccd2db; display:block;\">\n" + 
				"			<p style=\"padding:0; margin:0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:12px; line-height:18px; color:#333;\">This Email and files transmitted are confidential and intended solely for\n" + 
				"the use of the individual or entity to whom they are addressed. If you\n" + 
				"have received this Email in error please notify the system manager.\n" + 
				"Please note that any views or opinions presented in this Email are solely\n" + 
				"those of the author and do not necessarily represent those of IndusInd\n" + 
				"Bank Ltd. Finally, the recipient should check this Email and any\n" + 
				"attachments for the presence of viruses. IndusInd Bank Ltd. accepts no\n" + 
				"liability for any damage caused by any virus transmitted by this Email.</p>\n" + 
				"		</td>\n" + 
				"	</tr>\n" + 
				"</table>"
				,"REKYC_CONFIRMATION_MAIL_LR_NONSTP_AC", false ,LocalDateTime.now() ,LocalDateTime.now()));
		
		tblEmailTemplateRepository.save(new TblEmailTemplate(5 ,"Acknowledgement for Re-KYC Update Request",
				"<table width=\"600\" border=\"0\" cellpadding=\"0\"  cellspacing=\"0\" align=\"center\" style=\"margin:0 auto; padding:0;\">\n" + 
				"	<tr>\n" + 
				"		<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; background:#fff; display:block; border:1px solid #ccd2db\">\n" + 
				"			<table border=\"0\" cellpadding=\"0\"  cellspacing=\"0\" align=\"center\" style=\"margin:0; padding:0;\">\n" + 
				"				<tr>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; width:20px;\">&nbsp;</td>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; width:560px;\">&nbsp;</td>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; width:20px;\">&nbsp;</td>\n" + 
				"				</tr>\n" + 
				"				<tr>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; width:20px;\">&nbsp;</td>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">\n" + 
				"						<table width=\"100%\" border=\"0\" align=\"center\" cellpadding=\"0\"  cellspacing=\"0\" style=\"margin:0; padding:0;\">\n" + 
				"							<tr>\n" + 
				"								<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">\n" + 
				"									<p style=\"padding:0; margin:0 0 15px 0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">Dear Customer,</p>\n" + 
				"									<p style=\"padding:0; margin:0 0 15px 0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">Your Service Request #[REFERENCE_ID] for re-KYC update and activation of dormant account has been\n" + 
				"submitted successfully and will be processed in [COMMUNICATION_HOUR] hours. As per your request, your address will\n" + 
				"also be updated as per address in address proof document submitted by you.</p>\n" + 
				"									<p style=\"padding:0; margin:0 0 15px 0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">We thank you for giving us an opportunity to serve you and we look forward to your continued\n" + 
				"patronage.</p>\n" + 
				"									<p style=\"padding:0; margin:0 0 15px 0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">For any further assistance or guidance, please call our Phone Banking centre at [REKYC_RELATED_CONTACT_NO] or\n" + 
				"write to us at \n" + 
				"										<a href=\"mailto:[REKYC_RELATED_CONTACT_EMAIL]\" target=\"_top\">[REKYC_RELATED_CONTACT_EMAIL]</a>\n" + 
				"									</p>\n" + 
				"									<p style=\"padding:0; margin:0 0 15px 0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">Assuring you of our best services.</p>\n" + 
				"								</td>\n" + 
				"							</tr>\n" + 
				"							<tr>\n" + 
				"								<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">&nbsp;</td>\n" + 
				"							</tr>\n" + 
				"							<tr>\n" + 
				"								<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">\n" + 
				"									<p style=\"padding:0; margin:0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">Sincerely, </p>\n" + 
				"									<p style=\"padding:0; margin:0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">Team IndusInd Bank Limited.</p>\n" + 
				"								</td>\n" + 
				"							</tr>\n" + 
				"						</table>\n" + 
				"					</td>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; width:20px;\"\">&nbsp;</td>\n" + 
				"				</tr>\n" + 
				"				<tr>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">&nbsp;</td>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">&nbsp;</td>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">&nbsp;</td>\n" + 
				"				</tr>\n" + 
				"			</table>\n" + 
				"		</td>\n" + 
				"	</tr>\n" + 
				"	<tr>\n" + 
				"		<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; background:#fff; display:block;\"></td>\n" + 
				"	</tr>\n" + 
				"	<tr>\n" + 
				"		<td align=\"left\" valign=\"top\" style=\"padding:20px; margin:0; background:#ccd2db; display:block;\">\n" + 
				"			<p style=\"padding:0; margin:0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:12px; line-height:18px; color:#333;\">This Email and files transmitted are confidential and intended solely for\n" + 
				"the use of the individual or entity to whom they are addressed. If you\n" + 
				"have received this Email in error please notify the system manager.\n" + 
				"Please note that any views or opinions presented in this Email are solely\n" + 
				"those of the author and do not necessarily represent those of IndusInd\n" + 
				"Bank Ltd. Finally, the recipient should check this Email and any\n" + 
				"attachments for the presence of viruses. IndusInd Bank Ltd. accepts no\n" + 
				"liability for any damage caused by any virus transmitted by this Email.</p>\n" + 
				"		</td>\n" + 
				"	</tr>\n" + 
				"</table>"
				,"REKYC_CONFIRMATION_MAIL_LR_NONSTP_AC_DA", false ,LocalDateTime.now() ,LocalDateTime.now()));
		
		tblEmailTemplateRepository.save(new TblEmailTemplate(6 ,"Acknowledgement for Re-KYC Update Request",
				"<table width=\"600\" border=\"0\" cellpadding=\"0\"  cellspacing=\"0\" align=\"center\" style=\"margin:0 auto; padding:0;\">\n" + 
				"	<tr>\n" + 
				"		<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; background:#fff; display:block; border:1px solid #ccd2db\">\n" + 
				"			<table border=\"0\" cellpadding=\"0\"  cellspacing=\"0\" align=\"center\" style=\"margin:0; padding:0;\">\n" + 
				"				<tr>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; width:20px;\">&nbsp;</td>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; width:560px;\">&nbsp;</td>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; width:20px;\">&nbsp;</td>\n" + 
				"				</tr>\n" + 
				"				<tr>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; width:20px;\">&nbsp;</td>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">\n" + 
				"						<table width=\"100%\" border=\"0\" align=\"center\" cellpadding=\"0\"  cellspacing=\"0\" style=\"margin:0; padding:0;\">\n" + 
				"							<tr>\n" + 
				"								<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">\n" + 
				"									<p style=\"padding:0; margin:0 0 15px 0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">Dear Customer,</p>\n" + 
				"									<p style=\"padding:0; margin:0 0 15px 0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">Your Service Request #[REFERENCE_ID] for re-KYC update has been submitted successfully and will be\n" + 
				"processed in [COMMUNICATION_HOUR] hours. Your communication address will also be updated if address in the address\n" + 
				"proof document submitted by you is different from address currently available in our records.</p>\n" + 
				"									<p style=\"padding:0; margin:0 0 15px 0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">We thank you for giving us an opportunity to serve you and we look forward to your continued\n" + 
				"patronage.</p>\n" + 
				"									<p style=\"padding:0; margin:0 0 15px 0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">For any further assistance or guidance, please call our Phone Banking centre at [REKYC_RELATED_CONTACT_NO] or\n" + 
				"write to us at \n" + 
				"										<a href=\"mailto:[REKYC_RELATED_CONTACT_EMAIL]\" target=\"_top\">[REKYC_RELATED_CONTACT_EMAIL]</a>\n" + 
				"									</p>\n" + 
				"									<p style=\"padding:0; margin:0 0 15px 0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">Assuring you of our best services.</p>\n" + 
				"								</td>\n" + 
				"							</tr>\n" + 
				"							<tr>\n" + 
				"								<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">&nbsp;</td>\n" + 
				"							</tr>\n" + 
				"							<tr>\n" + 
				"								<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">\n" + 
				"									<p style=\"padding:0; margin:0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">Sincerely, </p>\n" + 
				"									<p style=\"padding:0; margin:0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">Team IndusInd Bank Limited.</p>\n" + 
				"								</td>\n" + 
				"							</tr>\n" + 
				"						</table>\n" + 
				"					</td>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; width:20px;\"\">&nbsp;</td>\n" + 
				"				</tr>\n" + 
				"				<tr>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">&nbsp;</td>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">&nbsp;</td>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">&nbsp;</td>\n" + 
				"				</tr>\n" + 
				"			</table>\n" + 
				"		</td>\n" + 
				"	</tr>\n" + 
				"	<tr>\n" + 
				"		<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; background:#fff; display:block;\"></td>\n" + 
				"	</tr>\n" + 
				"	<tr>\n" + 
				"		<td align=\"left\" valign=\"top\" style=\"padding:20px; margin:0; background:#ccd2db; display:block;\">\n" + 
				"			<p style=\"padding:0; margin:0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:12px; line-height:18px; color:#333;\">This Email and files transmitted are confidential and intended solely for\n" + 
				"the use of the individual or entity to whom they are addressed. If you\n" + 
				"have received this Email in error please notify the system manager.\n" + 
				"Please note that any views or opinions presented in this Email are solely\n" + 
				"those of the author and do not necessarily represent those of IndusInd\n" + 
				"Bank Ltd. Finally, the recipient should check this Email and any\n" + 
				"attachments for the presence of viruses. IndusInd Bank Ltd. accepts no\n" + 
				"liability for any damage caused by any virus transmitted by this Email.</p>\n" + 
				"		</td>\n" + 
				"	</tr>\n" + 
				"</table>"
				,"REKYC_CONFIRMATION_MAIL_MR_HR_NON_STP", false ,LocalDateTime.now() ,LocalDateTime.now()));
		
		tblEmailTemplateRepository.save(new TblEmailTemplate(7 ,"Acknowledgement for Re-KYC Update Request",
				"<table width=\"600\" border=\"0\" cellpadding=\"0\"  cellspacing=\"0\" align=\"center\" style=\"margin:0 auto; padding:0;\">\n" + 
				"	<tr>\n" + 
				"		<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; background:#fff; display:block; border:1px solid #ccd2db\">\n" + 
				"			<table border=\"0\" cellpadding=\"0\"  cellspacing=\"0\" align=\"center\" style=\"margin:0; padding:0;\">\n" + 
				"				<tr>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; width:20px;\">&nbsp;</td>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; width:560px;\">&nbsp;</td>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; width:20px;\">&nbsp;</td>\n" + 
				"				</tr>\n" + 
				"				<tr>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; width:20px;\">&nbsp;</td>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">\n" + 
				"						<table width=\"100%\" border=\"0\" align=\"center\" cellpadding=\"0\"  cellspacing=\"0\" style=\"margin:0; padding:0;\">\n" + 
				"							<tr>\n" + 
				"								<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">\n" + 
				"									<p style=\"padding:0; margin:0 0 15px 0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">Dear Customer,</p>\n" + 
				"									<p style=\"padding:0; margin:0 0 15px 0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">Your Service Request #[REFERENCE_ID] for re-KYC update and activation of dormant account has been\n" + 
				"submitted successfully and will be processed in [COMMUNICATION_HOUR] hours. Your communication address will also be\n" + 
				"updated if address in the address proof document submitted by you is different from address currently\n" + 
				"available in our records.</p>\n" + 
				"									<p style=\"padding:0; margin:0 0 15px 0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">We thank you for giving us an opportunity to serve you and we look forward to your continued\n" + 
				"patronage.</p>\n" + 
				"									<p style=\"padding:0; margin:0 0 15px 0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">For any further assistance or guidance, please call our Phone Banking centre at [REKYC_RELATED_CONTACT_NO] or\n" + 
				"write to us at \n" + 
				"										<a href=\"mailto:[REKYC_RELATED_CONTACT_EMAIL]\" target=\"_top\">[REKYC_RELATED_CONTACT_EMAIL]</a>\n" + 
				"									</p>\n" + 
				"									<p style=\"padding:0; margin:0 0 15px 0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">Assuring you of our best services.</p>\n" + 
				"								</td>\n" + 
				"							</tr>\n" + 
				"							<tr>\n" + 
				"								<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">&nbsp;</td>\n" + 
				"							</tr>\n" + 
				"							<tr>\n" + 
				"								<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">\n" + 
				"									<p style=\"padding:0; margin:0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">Sincerely, </p>\n" + 
				"									<p style=\"padding:0; margin:0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:14px; line-height:18px; color:#333;\">Team IndusInd Bank Limited.</p>\n" + 
				"								</td>\n" + 
				"							</tr>\n" + 
				"						</table>\n" + 
				"					</td>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; width:20px;\"\">&nbsp;</td>\n" + 
				"				</tr>\n" + 
				"				<tr>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">&nbsp;</td>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">&nbsp;</td>\n" + 
				"					<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0;\">&nbsp;</td>\n" + 
				"				</tr>\n" + 
				"			</table>\n" + 
				"		</td>\n" + 
				"	</tr>\n" + 
				"	<tr>\n" + 
				"		<td align=\"left\" valign=\"top\" style=\"padding:0; margin:0; background:#fff; display:block;\"></td>\n" + 
				"	</tr>\n" + 
				"	<tr>\n" + 
				"		<td align=\"left\" valign=\"top\" style=\"padding:20px; margin:0; background:#ccd2db; display:block;\">\n" + 
				"			<p style=\"padding:0; margin:0; font-family:Arial, Helvetica, sans-serif; font-weight:normal; font-size:12px; line-height:18px; color:#333;\">This Email and files transmitted are confidential and intended solely for\n" + 
				"the use of the individual or entity to whom they are addressed. If you\n" + 
				"have received this Email in error please notify the system manager.\n" + 
				"Please note that any views or opinions presented in this Email are solely\n" + 
				"those of the author and do not necessarily represent those of IndusInd\n" + 
				"Bank Ltd. Finally, the recipient should check this Email and any\n" + 
				"attachments for the presence of viruses. IndusInd Bank Ltd. accepts no\n" + 
				"liability for any damage caused by any virus transmitted by this Email.</p>\n" + 
				"		</td>\n" + 
				"	</tr>\n" + 
				"</table>"
				,"REKYC_CONFIRMATION_MAIL_MR_HR_NON_STP_DA", false ,LocalDateTime.now() ,LocalDateTime.now()));
		
		tblEmailTemplateRepository.save(new TblEmailTemplate(8 ,"Not Applicable",
				"Your One Time Password is [VAR_OTP] for your re-KYC update request. Please do not share OTP with anyone for security reasons"
				,"SEND_OTP_SMS", false ,LocalDateTime.now() ,LocalDateTime.now()));
		
		tblEmailTemplateRepository.save(new TblEmailTemplate(9 ,"Not Applicable",
				"Greetings! Your request for re-KYC update has been processed successfully."
				,"REKYC_CONFIRMATION_SMS_LR_STP", false ,LocalDateTime.now() ,LocalDateTime.now()));
		
		tblEmailTemplateRepository.save(new TblEmailTemplate(10 ,"Not Applicable",
				"Greetings! Your request for re-KYC update and dormant account activation has been\n" + 
				"processed successfully."
				,"REKYC_CONFIRMATION_SMS_LR_STP_DA", false ,LocalDateTime.now() ,LocalDateTime.now()));
		
		tblEmailTemplateRepository.save(new TblEmailTemplate(11 ,"Not Applicable",
				"Greetings! Your request #[REFERENCE_ID] for re-KYC update has been submitted successfully\n" + 
				"and will be processed in [COMMUNICATION_HOUR] hours."
				,"REKYC_CONFIRMATION_SMS_LR_NONSTP_AC", false ,LocalDateTime.now() ,LocalDateTime.now()));
		
		tblEmailTemplateRepository.save(new TblEmailTemplate(12 ,"Not Applicable",
				"Greetings! Your request #[REFERENCE_ID] for re-KYC update and dormant account activation\n" + 
				"has been submitted successfully and will be processed in [COMMUNICATION_HOUR] hours."
				,"REKYC_CONFIRMATION_SMS_LR_NONSTP_AC_DA", false ,LocalDateTime.now() ,LocalDateTime.now()));
		
		tblEmailTemplateRepository.save(new TblEmailTemplate(13 ,"Not Applicable",
				"Greetings! Your request #[REFERENCE_ID]for re-KYC update has been submitted successfully and will be processed in [COMMUNICATION_HOUR] hours."
				,"REKYC_CONFIRMATION_SMS_MR_HR_NON_STP", false ,LocalDateTime.now() ,LocalDateTime.now()));
		
		tblEmailTemplateRepository.save(new TblEmailTemplate(14 ,"Not Applicable",
				"Greetings! Your request #[REFERENCE_ID] for re-KYC update and dormant account activation\n" + 
				"has been submitted successfully and will be processed in [COMMUNICATION_HOUR] hours."
				,"REKYC_CONFIRMATION_SMS_MR_HR_NON_STP_DA", false ,LocalDateTime.now() ,LocalDateTime.now()));
	}
	
	@Bean
	public void saveTblAccountType() {
		accountTypeRepository.deleteAll();
		accountTypeRepository.save(new TblAccountType(1,  "SBA", "Savings Account: ", "SB No.", LocalDateTime.now() ,LocalDateTime.now()));
		accountTypeRepository.save(new TblAccountType(2,  "CAA", "Current Account: ", "CA No.", LocalDateTime.now() ,LocalDateTime.now()));
		accountTypeRepository.save(new TblAccountType(3,  "SAA", "Savings Account: ", "SA No.",LocalDateTime.now() ,LocalDateTime.now()));
		accountTypeRepository.save(new TblAccountType(4,  "FDA", "Fixed Deposit: ", "FD No.",LocalDateTime.now() ,LocalDateTime.now()));
		accountTypeRepository.save(new TblAccountType(5,  "ODA", "Overdraft Account: ", "OD No.",LocalDateTime.now() ,LocalDateTime.now()));
		accountTypeRepository.save(new TblAccountType(6,  "TDA", "Term Deposit Account: ", "TD No.",LocalDateTime.now() ,LocalDateTime.now()));

	}
	
}
