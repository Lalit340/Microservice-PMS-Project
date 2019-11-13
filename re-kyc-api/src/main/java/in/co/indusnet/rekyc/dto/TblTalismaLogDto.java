package in.co.indusnet.rekyc.dto;



import lombok.Data;

@Data
public class TblTalismaLogDto {




	private String endUrl;


	private String request;

	
	private String response;

	
	

	
	private String responseStatus;

	private int interactionId;


	private int errorCode;

	
	private String errorMessage;

	
	private String interactionState;

	
	private int apiHitCount;


	

}
