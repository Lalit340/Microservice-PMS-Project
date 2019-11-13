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
@Table(name = "tbl_document_mapper")
@Data
public class TblDocumentMapper implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	private int docProofTypeId;
	
	@NotNull
	private long docTypeId;
			
	@Column(columnDefinition=" INT NOT NULL DEFAULT '1'")
	private int displayOrder;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;
}
