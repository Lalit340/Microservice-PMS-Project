package in.co.indusnet.rekyc.utility;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import in.co.indusnet.rekyc.model.TblErrorMessages;
import in.co.indusnet.rekyc.repository.TblErrorMessagesRepository;

@Component
public class GetErrorMessage {

	@Autowired
	private TblErrorMessagesRepository tblErrorMesRepository;

	/*
	 * this method purpose to get the error messages by error_code return
	 * 'error_message' otherwise return null .
	 */
	public String getErrorMessages(String errorCode) {

		// to get the object with errorCode
		TblErrorMessages tblErrorMessage = tblErrorMesRepository.findByErrorCode(errorCode);
		System.out.println("token class object ::" + tblErrorMessage);

		// if condition for validate data
		if (tblErrorMessage != null) {
			String errorMessage = tblErrorMessage.getErrorMessage();
			return errorMessage;
		} else {
			return null;
		}

	}
	public List<TblErrorMessages> getAllErrorMessages() {

		// to get the object with errorCode
		List<TblErrorMessages> tblErrorMessage = tblErrorMesRepository.findAll();
		System.out.println("token class object ::" + tblErrorMessage);

		// if condition for validate data
		if (tblErrorMessage != null) {
			return tblErrorMessage;
		} else {
			return null;
		}

	}

}
