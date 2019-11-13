package in.co.indusnet.rekyc.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SSOData {

	String reqType;

	String sessionid;	

	String cifid;

	String channelid;

	String cifTyp;

}
