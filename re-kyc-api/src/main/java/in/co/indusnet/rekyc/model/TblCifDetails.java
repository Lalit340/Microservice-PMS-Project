package in.co.indusnet.rekyc.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import in.co.indusnet.rekyc.model.TBLRequestlog;
import lombok.Data;

@Entity
@Table(name = "tbl_cif_details")
@Data
public class TblCifDetails implements Serializable{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private  long id;
	
	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "req_id", nullable = false ,columnDefinition =" BIGINT(20) NOT NULL COMMENT 'Primary id of tbl_request_log'" )
	private TBLRequestlog tblRequestLog;
	
	@NotNull
	@NotEmpty
	private String cifCode;
	
	@NotNull
	@NotEmpty
	private String cifType;
	
	@Column(columnDefinition=" BOOLEAN NOT NULL DEFAULT FALSE COMMENT '0=No 1=Yes'")
	private boolean masterCif;

	@Column(columnDefinition=" BOOLEAN NOT NULL DEFAULT FALSE COMMENT '0=No 1=Yes'")
	private boolean isSelected;
	
	@Column(columnDefinition=" BOOLEAN NULL DEFAULT FALSE COMMENT '0=No 1=Yes'")
	private boolean isNr;
	
	@Column(columnDefinition=" BOOLEAN NULL DEFAULT FALSE COMMENT '0=No 1=Yes'")
	private boolean isNrChecked;

	private  String constitutionType;
	
	private  String customerName;
	
	@Column(columnDefinition=" TEXT NULL COMMENT ''")
	private  String communicationAddress;


	private  String dateOfBirth;

	private  String panNumber;
	
	@Column(columnDefinition="TINYINT(1) NOT NULL DEFAULT FALSE COMMENT '0=Not Validated 1=Valid 2=Invalid 3=Not Available'")
	private int isPanValidated;
	
	private  String gender;
		
	private  String aadhaarNumber;

	private  String mobileNumber;
	
	private  String comEmailId;
	
	private int riskProfile;
			
	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;
}
