package in.co.indusnet.rekyc.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class DormantAccountList {
	
	private long id;
	
	private String customerName;
	
	private String accountNo ;
	
	private String accountType;

	public boolean requestDormantAccountActive;
	
	public long cifId;

}