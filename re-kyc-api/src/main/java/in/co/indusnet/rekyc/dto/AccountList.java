package in.co.indusnet.rekyc.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class AccountList {
	
	private long id;
	
	private String customerName;
	
	private String accountNo ;
	
	private String accountType;

	public long cifId;

}
