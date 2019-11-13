package in.co.indusnet.rekyc.response;

import java.util.ArrayList;
import java.util.List;

import in.co.indusnet.rekyc.dto.AccountList;
import in.co.indusnet.rekyc.dto.CustomerDetails;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class GetCifDetailsApi {

	private List<AccountList> accountList = new ArrayList<>();

	private CustomerDetails customerDetails;
	
	private String hasDormantAccount;

}
