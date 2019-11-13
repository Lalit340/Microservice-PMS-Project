package in.co.indusnet.soap.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import in.co.indusnet.soap.model.rekyc.RekycTblErrorMessages;
import in.co.indusnet.soap.repository.rekyc.RekycTblErrorMessagesRepository;


import in.co.indusnet.soap.model.fdcmi.FdcmiTblErrorMessages;
import in.co.indusnet.soap.repository.fdcmi.FdcmiTblErrorMessagesRepository;

@Component
public class GetErrorMessage {

	@Autowired
	private RekycTblErrorMessagesRepository rekycTblErrorMesRepository;
	
	@Autowired
	private FdcmiTblErrorMessagesRepository fdcmiTblErrorMesRepository;

	/*
	 * this method purpose to get the error messages by error_code return
	 * 'error_message' otherwise return null .
	 */
	public String getRekycErrorMessages(String errorCode) {

		// to get the object with errorCode
		RekycTblErrorMessages tblErrorMessage = rekycTblErrorMesRepository.findByErrorCode(errorCode);
		System.out.println("token class object ::" + tblErrorMessage);

		// if condition for validate data
		if (tblErrorMessage != null) {
			String errorMessage = tblErrorMessage.getErrorMessage();
			return errorMessage;
		} else {
			return null;
		}

	}
	public String getFdcmiErrorMessages(String errorCode) {

		// to get the object with errorCode
		FdcmiTblErrorMessages tblErrorMessage = fdcmiTblErrorMesRepository.findByErrorCode(errorCode);
		System.out.println("token class object ::" + tblErrorMessage);

		// if condition for validate data
		if (tblErrorMessage != null) {
			String errorMessage = tblErrorMessage.getErrorMessage();
			return errorMessage;
		} else {
			return null;
		}

	}

}
