package in.co.indusnet.rekyc.dto;

import lombok.Data;

@Data
public class TblErrorMessagesDto{
    
	
	private  String service_code;
	
	private String error_code;
	
	private String error_link;
	
	private String error_message;
	
	private boolean status;
}
