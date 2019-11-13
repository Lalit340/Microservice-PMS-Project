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
@Table(name = "tbl_request_to_key")
@Data
public class RekycTblRequestToKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private long reqId;

	@NotNull
	@NotEmpty
	@Column(columnDefinition=" TEXT NOT NULL COMMENT '64 characters alphanumeric unique token'")
	private String token;

	@Column(columnDefinition = " BOOLEAN NOT NULL COMMENT '0=Active, 1=Inactive'")
	private boolean tokenStatus;

	@Column(columnDefinition = " BOOLEAN NOT NULL COMMENT '0=No, 1=Yes'")
	private boolean isUpdated;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;
}
