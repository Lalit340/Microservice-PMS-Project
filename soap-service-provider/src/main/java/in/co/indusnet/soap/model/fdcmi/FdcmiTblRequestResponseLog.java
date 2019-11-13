package in.co.indusnet.soap.model.fdcmi;

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
@Table(name = "tbl_request_response_log")
@Data
public class FdcmiTblRequestResponseLog implements Serializable{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private  long id;
	
	@Column(columnDefinition=" BIGINT(20) NOT NULL COMMENT 'Primary id of tbl_request_log'")
	private  long reqId;
	
	private String endUrl;
		
	@Column(columnDefinition=" TEXT NOT NULL COMMENT ''")
	private String request;
	
	@Column(columnDefinition=" TEXT NULL COMMENT ''")
	private String response;

	private String activityType;


	private LocalDateTime requestTime;
	
	private LocalDateTime responseTime;

		
	
	private LocalDateTime createdAt;
	
	
	private LocalDateTime updatedAt;

}