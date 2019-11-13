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

import in.co.indusnet.rekyc.model.TblCifDetails;
import lombok.Data;

@Entity
@Table(name = "tbl_account_details")
@Data
public class TblAccountDetails implements Serializable{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 5004952720911367196L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private  long id;
	
	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cif_id", nullable = false ,columnDefinition =" BIGINT(20) NOT NULL COMMENT 'Primary id of tbl_cif_details'")
    private TblCifDetails tblCifDetails;
	

	@NotNull
	@NotEmpty
	private String accountType;
	
	@NotNull
	@NotEmpty
	private String accountNumber;
	
	@Column(columnDefinition=" BOOLEAN NOT NULL COMMENT '0=No, 1=Yes'")
	private boolean isDisplay;


	@NotNull
	@NotEmpty
	private  String schemeCode;
	

	private  String mop;


	private  String c5Code;
	

	private  String emailId;
	

	private  String c5CodeDesc;


	private  String mopDesc;
	
	@Column(columnDefinition=" VARCHAR(30) NOT NULL COMMENT 'Active or Closed or Dormant'")
	private  String status;
	
	@Column(columnDefinition=" BOOLEAN NOT NULL COMMENT '0=No 1=Yes'")
	private boolean requestDormantActive;
		
	
	private LocalDateTime createdAt;
	
	
	private LocalDateTime updatedAt;
}
