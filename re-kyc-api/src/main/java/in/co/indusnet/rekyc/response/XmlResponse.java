package in.co.indusnet.rekyc.response;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class XmlResponse implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer rspCode;
    
    private String rspMsg;
    
    private Errors errors;

}
