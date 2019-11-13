package in.co.indusnet.rekyc.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CifList {
	
	private long id;
	
	private String cifCode;

	private boolean isSelected;
	
	private int riskProfile;


}
