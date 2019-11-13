package in.co.indusnet.rekyc.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Table(name = "tbl_doc")
@Data
public class TblDoc implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	
	@Column(columnDefinition = " BIGINT(20) NOT NULL COMMENT 'Primary id of tbl_request_log'")
	private long reqId;

	@Column(columnDefinition = " BIGINT(20) NOT NULL COMMENT 'Primary id of tbl_doc_type'")
	private long docTypeId;

	@Column(columnDefinition = " TEXT NOT NULL COMMENT ''")
	private String originalFileName;

	@Column(columnDefinition = " TEXT NOT NULL COMMENT ''")
	private String uploadedFileName;

	private long fileSize;
	
	@Column(columnDefinition = " TINYINT(4) NOT NULL COMMENT '1= Id Proof, 2= Address Proof, 3= NR Status Proof, 4= Overseas Address Proof'")
	private int docProofType;

	@Column(columnDefinition = " TINYINT(4) NOT NULL COMMENT '0= Deleted, 1= Available, 2= Uploaded on Talisma, 3= Sent attachment email'")
	private int status;
	

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;
}
