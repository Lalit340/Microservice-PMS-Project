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

import org.springframework.data.repository.NoRepositoryBean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_doc_type")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblDocType implements Serializable{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private  long id;
	
	@NotNull
	@NotEmpty
	private  String code;
	
	@NotNull
	@NotEmpty
	private String title;
	
	@Column(columnDefinition=" BOOLEAN NOT NULL COMMENT '0=No, 1=Yes'")
	private  boolean status;
	
	@NotNull
	private int displayOrder;

		
	
	private LocalDateTime createdAt;
	
	
	private LocalDateTime updatedAt;
}
