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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_error_messages")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FdcmiTblErrorMessages implements Serializable{
    

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private  long id;
	
	@NotNull
	@NotEmpty
	private  String serviceCode;
	
	@NotNull
	@NotEmpty
	private String errorCode;
	
	
	private String errorLink;
	
	@NotNull
	@NotEmpty
	@Column(columnDefinition=" TEXT NOT NULL COMMENT ''")
	private String errorMessage;
	
	@Column(columnDefinition=" BOOLEAN NOT NULL COMMENT '0=Inactive, 1=Active'")
	private boolean status;

	
	private LocalDateTime createdAt;
	
	
	private LocalDateTime updatedAt;
}
