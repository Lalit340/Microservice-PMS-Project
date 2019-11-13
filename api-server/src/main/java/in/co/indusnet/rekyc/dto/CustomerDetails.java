package in.co.indusnet.rekyc.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CustomerDetails {

	private String customerName;

	private String panNumber;
	
	private String oldCommunicationAddress;
	
	private String newCommunicationAddress;
	
	private int isPanValidated;
	
	private Boolean isAddressModified;
	
}
