package in.co.indusnet.soap.model.rekyc;

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
@Table(name = "tbl_talisma_log")
@Data
public class RekycTblTalismaLog implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotNull
	@Column(columnDefinition=" BIGINT(20) NOT NULL COMMENT 'Primary id of tbl_request_log'")
	private long reqId;

	@NotNull
	@Column(columnDefinition=" BIGINT(20) NOT NULL COMMENT 'Primary id of tbl_cif_details'")
	private long cifId;

	@NotNull
	@NotEmpty
	private String endUrl;


	@NotNull
	@NotEmpty
	@Column(columnDefinition=" TEXT NOT NULL COMMENT ''")
	private String request;

	@Column(columnDefinition=" TEXT NOT NULL COMMENT ''")
	private String response;

	private LocalDateTime requestTime;

	private LocalDateTime responseTime;
	
	private String responseStatus;

	@NotNull
	private int interactionId;

	@NotNull
	private int errorCode;

	@NotNull
	@Column(columnDefinition=" TEXT NOT NULL COMMENT ''")
	private String errorMessage;

	@NotNull
	@NotEmpty
	@Column(columnDefinition=" VARCHAR(30) NOT NULL COMMENT 'Resolved or Open'")
	private String interactionState;

	@NotNull
	private int apiHitCount;

	@NotNull
	private boolean isAdminUpdated;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;
}
