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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_email_template")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblEmailTemplate implements Serializable{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -7183518859363705228L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private  long id;
	
	@NotNull
	@NotEmpty
	private  String emailSubject;
	
	@NotNull
	@NotEmpty
	@Column(columnDefinition=" TEXT NOT NULL COMMENT ''")
	private String emailContent;
	
	@NotNull
	@NotEmpty
	private String emailType;
	
	
	@Column(columnDefinition=" BOOLEAN NOT NULL COMMENT '0=Inactive, 1=Active'")
	private boolean status;

	
	private LocalDateTime createdAt;
	
	
	private LocalDateTime updatedAt;


}
