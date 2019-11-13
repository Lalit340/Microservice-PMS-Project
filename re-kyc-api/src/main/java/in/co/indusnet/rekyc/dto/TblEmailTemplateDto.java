package in.co.indusnet.rekyc.dto;

import lombok.Data;

@Data
public class TblEmailTemplateDto{
    
	private  String email_subject;
	
	private String email_content;
	
	private String email_type;

	private boolean status;
}
