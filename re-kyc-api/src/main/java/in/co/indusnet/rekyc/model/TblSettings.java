package in.co.indusnet.rekyc.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_settings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblSettings implements Serializable{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private  long id;
	
	@NotNull
	private  int fileCountLimit;
	
	@Column(columnDefinition=" INT(11) NOT NULL COMMENT 'size in MB'")
	@NotNull
	private  int singleFileUploadMaxSize;
	
	@Column(columnDefinition=" INT(11) NOT NULL COMMENT 'size in MB'")
	private  int totalFileUploadMaxSize;
	
	@NotNull
	private  String supportedFileFormats;
	
	
	private  int otpWrongAttemptsLimit;
	
	@Column(columnDefinition=" INT(11) NOT NULL COMMENT 'otp_timeout in second'")
	@NotNull
	private  int otpTimeout;
	
	@NotNull
	private  int otpValidation;
	
	@NotNull
	private  int maxOtpResendCount;
	
	@Column(columnDefinition=" INT(11) NOT NULL COMMENT 'time in minutes'")
	@NotNull
	private  int tokenExpirationTime;
	
	@Column(columnDefinition=" VARCHAR(255) NULL DEFAULT NULL COMMENT ''")
	private  String emailToForDocShare;
	
	@Column(columnDefinition=" TEXT NULL DEFAULT NULL COMMENT ''")
	private  String ccEmailsForDocShare;
	
	@Column(columnDefinition=" TEXT NULL DEFAULT NULL COMMENT ''")
	private  String bccEmailsForDocShare;
	
	@Column(columnDefinition=" LONGTEXT NULL DEFAULT NULL COMMENT ''")
	private  String mailBodyForDocShare;
	
	@Column(columnDefinition=" VARCHAR(255) NULL DEFAULT NULL COMMENT ''")
	private  String mailSubject;
	
	@Column(columnDefinition=" BOOLEAN NOT NULL DEFAULT TRUE COMMENT '0=No 1=Yes'")
	private boolean isOverseasShow;
		
	private LocalDateTime createdAt;
		
	private LocalDateTime updatedAt;
}
