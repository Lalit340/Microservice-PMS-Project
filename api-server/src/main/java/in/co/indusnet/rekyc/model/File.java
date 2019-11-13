package in.co.indusnet.rekyc.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table
@Data
public class File {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int fileId;
	
	private String fileRandName;
	
	private String fileName;
	
	private long fileSize;
	
	private String fileType;
}
