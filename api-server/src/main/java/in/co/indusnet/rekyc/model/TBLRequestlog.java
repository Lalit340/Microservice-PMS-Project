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
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Table(name = "tbl_request_log")
@Data
public class TBLRequestlog implements Serializable {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private  long rId;
	
	@NotNull
	@NotEmpty
	private String ssoReqType;
	
	@NotNull
	@NotEmpty
	private String ssoCifType;
	
	@NotNull
	@NotEmpty
	private String ssoCifCode;

	@NotNull
	@NotEmpty
	private String ssoSessionId;

	@NotNull
	@NotEmpty
	private  String ssoChannelId;
	
	@Column(columnDefinition=" TEXT NULL COMMENT ''")
	private  String address;

	@Column(columnDefinition=" BOOLEAN NOT NULL COMMENT '0=No, 1=Yes'")
	private  boolean isAddressModify;
	
	@Column(columnDefinition=" TINYINT(4) NOT NULL COMMENT '0= Get SSO Details, 1= Save All CIF Details, 2= Update Selected CIF, 3= Select Dormant Account, 4= Upload Document, 5= OTP Failed, 6= OTP Success, 7= Update on Talisma'")
	private  int status ;
	
	@NotNull
	@NotEmpty
	@Column(columnDefinition=" TEXT NOT NULL COMMENT 'All encrypted data will be stored in json format'")
	private String ssoData;
	
	@Column(columnDefinition=" BOOLEAN NOT NULL COMMENT '0=No 1=Yes'")
	private boolean requestDormantUpgrade;
	
	private boolean isAddressProofSameAsIdProof;
	
	private LocalDateTime createdAt;
	
	
	private LocalDateTime updatedAt;
	
	
}
