package in.co.indusnet.rekyc.model;

import java.io.Serializable;
import java.time.LocalDateTime;

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
@Table(name = "tbl_account_types")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblAccountType implements Serializable{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private  long id;
	
	@NotNull
	@NotEmpty
	private  String accountCode;
	
	@NotNull
	@NotEmpty
	private String accountTypeDesc;
	
	@NotNull
	@NotEmpty
	private String accountLabel;
		
	private LocalDateTime createdAt;
	
	
	private LocalDateTime updatedAt;
}
