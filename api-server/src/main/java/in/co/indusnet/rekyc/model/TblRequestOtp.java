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
@Table(name = "tbl_request_otp")
@Data
public class TblRequestOtp implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private  long id;
	
	@NotNull
	@Column(columnDefinition =" BIGINT(20) NOT NULL COMMENT 'Primary id of tbl_request_log'" )
	private long reqId;
	
	@Column(columnDefinition =" INT(11) NOT NULL COMMENT '1= SMS/EMAIL'" )
	private int otpType;
	
	@Column(columnDefinition=" TEXT NULL COMMENT ''")
	private  String referenceId;
	
	@Column(columnDefinition=" TEXT NULL COMMENT ''")
	private  String mobileNo;
	
	@Column(columnDefinition=" TEXT NULL COMMENT ''")
	private  String email;
	
	@Column(columnDefinition=" TEXT NULL COMMENT ''")
	private  String otp;
	
	@Column(columnDefinition=" TEXT NULL COMMENT ''")
	private  String referenceNumber;
	
	private int otpAttempts;
	
	private LocalDateTime otpSentAt;
	
	@Column(columnDefinition=" TEXT NULL COMMENT ''")
	private  String sentSmsStatus;
	
	private int sentEmailResCode;
	
	@Column(columnDefinition=" BOOLEAN NOT NULL COMMENT '0=Matched 1=Match'")
	private boolean status;
				
	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;
}
