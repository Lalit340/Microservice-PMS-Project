package in.co.indusnet.rekyc.dto;




import lombok.Data;


@Data
public class TblDocDto {
	
	private long id;
	
	private String originalFileName;

	private String uploadedFileName;


	private long fileSize;
	
	private long docTypeId;

	//private  int status;
	

}
