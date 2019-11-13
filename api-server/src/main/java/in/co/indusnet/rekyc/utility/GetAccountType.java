package in.co.indusnet.rekyc.utility;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import in.co.indusnet.rekyc.model.TblAccountType;
import in.co.indusnet.rekyc.repository.AccountTypeRepository;

@Component
public class GetAccountType {

	@Autowired
	private AccountTypeRepository accountTypeRepository;

	public ArrayList<String> getTblAccountType(String accountCode) {
		TblAccountType tblAccountType = accountTypeRepository.findByAccountCode(accountCode);
		ArrayList<String> details = new ArrayList<String>();
		if(tblAccountType!=null) {
			details.add(tblAccountType.getAccountTypeDesc());
			details.add(tblAccountType.getAccountLabel());
		}
		else {
			details.add("");
			details.add("");
		}
		return details;
	}
	

}
