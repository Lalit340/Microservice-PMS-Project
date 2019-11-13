package in.co.indusnet.rekyc.utility;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import in.co.indusnet.rekyc.model.TblEmailTemplate;
import in.co.indusnet.rekyc.repository.TblEmailTemplateRepository;


@Component
public class GetEmailTemplate {

	@Autowired
	private TblEmailTemplateRepository tblEmailTemRepository;

	/*
	 * this method purpose to get the error messages by error_code return
	 * 'error_message' otherwise return null .
	 */
	public TblEmailTemplate getEmailTemplate(String code) {
		// to get the object with errorCode
		TblEmailTemplate tblEmailMessage = tblEmailTemRepository.findByEmailType(code);
		return tblEmailMessage;

	}
	public ArrayList<String> getEmailTemplateContent(String code) {
		// to get the object with errorCode
		TblEmailTemplate tblEmailMessage = tblEmailTemRepository.findByEmailType(code);
		ArrayList<String> details = new ArrayList<String>();
		if(!tblEmailMessage.equals(null)) {
			details.add(tblEmailMessage.getEmailSubject());
			details.add(tblEmailMessage.getEmailContent());
		}
		else {
			details.add("");
			details.add("");
		}
		return details;
	}

}
